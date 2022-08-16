package com.merseyside.archy.presentation.view.valueSwitcher

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.ViewStubProxy
import com.merseyside.archy.R
import com.merseyside.archy.databinding.ValueSwitcherViewBinding
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.delegate.*
import com.merseyside.utils.ext.setImageColor
import com.merseyside.utils.getClassName
import com.merseyside.utils.view.ext.onClick


class ValueSwitcher(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attributeSet, defStyleAttr) {

    private val attrs = AttributeHelper(
        context,
        attributeSet,
        R.styleable.ValueSwitcher,
        getClassName(),
        defStyleAttr,
        R.style.Mersey_ValueSwitcher_Theme,
        styleableNamePrefix = "switch"
    )

    private val binding: ValueSwitcherViewBinding by viewBinding(R.layout.value_switcher_view)
    private lateinit var valueView: View

    private val title: String by attrs.string()
    private val entryValues: List<String> by attrs.textArray()
    private val entries: List<String> by attrs.textArray()
    private val type: Type by attrs.enum { id -> Type.fromId(id) }
    private val buttons: Buttons by attrs.enum { id -> Buttons.fromId(id) }

    private val textColor: Int? by attrs.colorOrNull()
    private val buttonColor: Int by attrs.color()

    private var entryValueIndex = 0

    constructor(context: Context, attributeSet: AttributeSet?) : this(
        context, attributeSet, R.attr.valueSwitcherStyle
    )

    private var listener: OnValueChangeListener? = null
    private val clickListener: (View) -> Unit = { view ->
        if (entryValues.size != 1) {
            when (view.id) {
                R.id.prev -> if (entryValueIndex == 0) entryValueIndex =
                    entryValues.size - 1 else entryValueIndex--
                R.id.next -> if (entryValueIndex == entryValues.size - 1) entryValueIndex =
                    0 else entryValueIndex++
            }

            fillView()
            listener?.valueChanged(entryValues[entryValueIndex])
        }
    }

    init {
        initializeView()
    }

    private fun initializeView() {
        with(binding) {
            applyViewStub(viewStub)

            textColor?.let {
                if (valueView is TextView) {
                    (valueView as TextView).setTextColor(it)
                }
            }

            next.apply {
                setImageColor(buttonColor)
                onClick(clickListener)
            }
            prev.apply {
                setImageColor(buttonColor)
                onClick(clickListener)
            }

        }

        fillView()
    }

    private fun applyViewStub(viewStubProxy: ViewStubProxy) {
        val id = if (type == Type.TEXT) {
            R.layout.value_switcher_text_stub
        } else {
            R.layout.value_switcher_image_stub
        }

        valueView = viewStubProxy.viewStub?.run {
            layoutResource = id
            inflate()
        } ?: throw NullPointerException()
    }

    private fun fillView() {
        binding.title.text = title
        if (valueView is TextView) {
            (valueView as TextView).text = entries[entryValueIndex]
        } else if (valueView is ImageView) {
            val id = context.resources.getIdentifier(
                entries[entryValueIndex],
                "drawable",
                context.packageName
            )
            (valueView as ImageView).setImageResource(id)
        }
    }

    fun setCurrentEntryValue(value: String) {
        for (i in entryValues.indices) {
            if (value == entryValues[i]) {
                entryValueIndex = i
                break
            }
        }
        fillView()
    }

    fun setOnValueChangeListener(listener: OnValueChangeListener?) {
        this.listener = listener
    }

//    fun updateLanguage(context: Context) {
//        this.context = context
//        fillView()
//    }

    interface OnValueChangeListener {
        fun valueChanged(entryValue: String)
    }

    enum class Type(val id: Int) {
        TEXT(0), IMAGE(1);

        companion object {
            fun fromId(id: Int): Type {
                for (type in values()) {
                    if (type.id == id) {
                        return type
                    }
                }
                throw IllegalArgumentException("No color with passed id")
            }
        }
    }

    enum class Buttons(val id: Int) {
        ARROWS(0);

        companion object {
            fun fromId(id: Int): Buttons {
                for (type in values()) {
                    if (type.id == id) {
                        return type
                    }
                }
                throw IllegalArgumentException("No color with passed id")
            }
        }
    }

    companion object {
        const val TAG = "CustomPreference"
    }
}