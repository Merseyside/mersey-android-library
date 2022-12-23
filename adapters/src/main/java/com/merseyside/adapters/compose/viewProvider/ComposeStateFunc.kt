package com.merseyside.adapters.compose.viewProvider

import androidx.lifecycle.asLiveData
import com.merseyside.adapters.compose.dsl.context.ComposeContext
import com.merseyside.merseyLib.kotlin.utils.safeLet
import kotlinx.coroutines.flow.StateFlow
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//@Suppress("UNCHECKED_CAST")
//fun <T> StateFlow<T>.asComposeState(): ReadWriteProperty<ComposeViewProvider<out SCV>?, T> =
//    object : ReadWriteProperty<ComposeViewProvider<out SCV>?, T> {
//
//        val key: (KProperty<*>) -> String = KProperty<*>::name
//
//        override fun getValue(thisRef: ComposeViewProvider<out SCV>?, property: KProperty<*>): T {
//            thisRef.log()
//            return requireProvider(thisRef) {
//                val propName = key(property)
//                val composeState = getComposeState(propName) as? ComposeState<T>
//                    ?: asComposeState(this, propName).also { addComposeState(it) }
//                composeState.value!!
//            }
//        }
//
//        override fun setValue(thisRef: ComposeViewProvider<out SCV>?, property: KProperty<*>, value: T) {
//            requireProvider(thisRef) {
//                (getComposeState(key(property)) as? MutableComposeState<T>)?.value = value
//            }
//        }
//
//        private fun <R> requireProvider(provider: ComposeViewProvider<out SCV>?, block: ComposeViewProvider<out SCV>.() -> R): R {
//            return safeLet(provider, block) ?: throw IllegalArgumentException("ComposeViewProvider<out SCV> is null!")
//        }
//
//        private fun <T> StateFlow<T>.asComposeState(provider: ComposeViewProvider<out SCV>, propertyName: String): MutableComposeState<T> {
//            val composeState = MutableComposeState(propertyName, value)
////            asLiveData().observe(provider.viewLifecycleOwner) { newValue ->
////                composeState.value = newValue
////            }
//
//            return composeState
//        }
//    }

@Suppress("UNCHECKED_CAST")
fun <T> StateFlow<T>.asComposeState(context: ComposeContext): ComposeStateDelegate<T> =
    // try too find an answer why thisRes is null when call from method
    object : ComposeStateDelegate<T>(context) {

        override fun createComposeState(propertyName: String): MutableComposeState<T> {
            val composeState = MutableComposeState(propertyName, value)
            asLiveData().observe(context.viewLifecycleOwner) { newValue ->
                composeState.value = newValue
            }

            return composeState
        }
    }

@Suppress("UNCHECKED_CAST")
abstract class ComposeStateDelegate<T>(val context: ComposeContext) :
    ReadWriteProperty<ComposeContext?, T> {

    val key: (KProperty<*>) -> String = KProperty<*>::name

    override fun getValue(
        thisRef: ComposeContext?,
        property: KProperty<*>
    ): T {
        return requireContext(context) {
            val propName = key(property)
            val composeState = getComposeState(propName) as? ComposeState<T>
                ?: createComposeState(propName).also { addComposeState(it) }
            composeState.value!!
        }
    }

    override fun setValue(
        thisRef: ComposeContext?,
        property: KProperty<*>,
        value: T
    ) {
        requireContext(context) {
            (getComposeState(key(property)) as? MutableComposeState<T>)?.value = value
        }
    }

    protected abstract fun createComposeState(propertyName: String): MutableComposeState<T>

    protected fun <R> requireContext(
        provider: ComposeContext?,
        block: ComposeContext.() -> R
    ): R {
        return safeLet(provider, block)
            ?: throw IllegalArgumentException("ComposeViewProvider<out SCV> is null!")
    }
}

fun <T> composeState(
    context: ComposeContext,
    initValue: () -> T
): ComposeStateDelegate<T> =
    object : ComposeStateDelegate<T>(context) {
        override fun createComposeState(propertyName: String): MutableComposeState<T> {
            return MutableComposeState(propertyName, initValue())
        }
    }