package com.merseyside.adapters.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.merseyLib.kotlin.logger.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

fun <Parent> BaseAdapter<Parent, *>.setFlow(
    flow: Flow<Parent>,
    viewLifecycleOwner: LifecycleOwner,
    behaviour: Behaviour = Behaviour.ADD_UPDATE()
) {
    setListFlow(flow.map { listOf(it) }, viewLifecycleOwner, behaviour)
}

fun <Parent> BaseAdapter<Parent, *>.setListFlow(
    flow: Flow<List<Parent>>,
    viewLifecycleOwner: LifecycleOwner,
    behaviour: Behaviour = Behaviour.ADD_UPDATE()
) {
    with(viewLifecycleOwner) {
        lifecycleScope.launch {
            flow.flowWithLifecycle(lifecycle).collect { items ->
                doAsync {
                    when (behaviour) {
                        is Behaviour.ADD -> {
                            add(items)
                        }

                        else -> {
                            if (isEmpty()) {
                                if (behaviour is Behaviour.ADD_UPDATE) {
                                    add(items)
                                    return@doAsync
                                }
                            }

                            update(UpdateRequest.fromBehaviour(items, behaviour))
                        }
                    }
                }
            }
        }
    }
}

sealed class Behaviour {
    object ADD: Behaviour()
    open class UPDATE(val removeOld: Boolean = true, val addNew: Boolean = true): Behaviour()
    class ADD_UPDATE(removeOld: Boolean = true, addNew: Boolean = true): UPDATE(removeOld, addNew)
}