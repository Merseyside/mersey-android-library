package com.merseyside.archy.presentation.view.validationInputView

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.view.isInvisible
import androidx.core.view.updateLayoutParams
import com.merseyside.archy.R
import com.merseyside.archy.databinding.ViewValidationInputBinding
import com.merseyside.archy.presentation.view.validationInputView.ValidationState.*
import com.merseyside.merseyLib.kotlin.Logger
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.colorStateList.colorToSimpleStateList
import com.merseyside.utils.delegate.*
import com.merseyside.utils.textWatcher.ValidationTextWatcher
import com.merseyside.utils.view.ext.requireResourceFromAttr

open class ValidationInputView(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int
) : LinearLayout(context, attributeSet, defStyleAttr) {

    constructor(context: Context, attributeSet: AttributeSet)
            : this(context, attributeSet, R.attr.validationInputViewStyle)

    private val binding: ViewValidationInputBinding by viewBinding(R.layout.view_validation_input)
    private val attrs = AttributeHelper(
        context,
        attributeSet,
        R.styleable.ValidationInputView,
        "ValidationInputView",
        defStyleAttr,
        styleableNamePrefix = "valid"
    )

    protected val inputLayout = binding.inputLayout
    protected val editText = binding.editText
    protected val message = binding.message
    protected val icon = binding.icon

    private var onTextChangedListener: (String) -> Unit = {}
    var formatter: ((String) -> String)? = null
    var validator: ((String) -> ValidationState)? = null

    private val inputWidth by attrs.dimensionPixelSizeOrNull()
    private val inputHeight by attrs.dimensionPixelSizeOrNull()

    private val forceState by attrs.bool()
    private val hintText by attrs.string(defaultValue = "")
    private val iconMode by attrs.int(defaultValue = 0)
    private val inputType by attrs.int(defaultValue = 1)
    private val maxLength by attrs.int(defaultValue = 0)

    private val textDefault by attrs.string(defaultValue = "")
    private var textSuccess by attrs.string(defaultValue = "")
    private var textError by attrs.string(defaultValue = "")


    protected open val strokeColor by attrs.color(
        defaultValue = requireResourceFromAttr(R.attr.colorOnSurface)
    )

    protected open val successStrokeColor by attrs.color(
        defaultValue = requireResourceFromAttr(R.attr.colorAccent)
    )

    protected open val errorStrokeColor by attrs.color(
        defaultValue = requireResourceFromAttr(R.attr.colorError)
    )


    protected open val hintColor by attrs.color(
        defaultValue = requireResourceFromAttr(R.attr.colorOnSurface)
    )

    protected open val successHintColor by attrs.color(
        defaultValue = requireResourceFromAttr(R.attr.colorAccent)
    )

    protected open val errorHintColor by attrs.color(
        defaultValue = requireResourceFromAttr(R.attr.colorError)
    )

    protected open val messageColor by attrs.color(
        defaultValue = requireResourceFromAttr(R.attr.textColor)
    )

    protected open val successMessageColor by attrs.color(
        defaultValue = requireResourceFromAttr(R.attr.colorAccent)
    )

    protected open val errorMessageColor by attrs.color(
        defaultValue = requireResourceFromAttr(R.attr.colorError)
    )

    protected open val defaultIcon by attrs.drawableOrNull(resName = "icon")
    protected open val successIcon by attrs.drawableOrNull()
    protected open val errorIcon by attrs.drawableOrNull()


    var text: String
        get() = editText.text.toString()
        set(value) {
            if (text != value) {
                editText.setText(value)
            }
        }

    var getDefaultMsg: (text: String) -> String = { textDefault }
    var getErrorMsg: (text: String) -> String = { textError }
    var getSuccessMsg: (text: String) -> String = { textSuccess }


    private var validationState = FILLING

    init {
        orientation = VERTICAL
        inputLayout.id = View.generateViewId()
        editText.id = View.generateViewId()

        setLayout()

        setWithAttrs()
        setTextWatcher()
        setFocusListener()
    }

    private fun setLayout() {
        editText.updateLayoutParams {
            inputWidth?.let { width = it }
            inputHeight?.let { height = it }
        }
    }

    private fun setWithAttrs() {
        with(inputLayout) {
            hint = hintText
            endIconMode = iconMode

            if (maxLength > 0) {
                isCounterEnabled = true
                counterMaxLength = maxLength
            }
        }

        with(editText) {
            inputType = this@ValidationInputView.inputType
        }
    }

    private fun setTextWatcher() {
        editText.addTextChangedListener(object : ValidationTextWatcher(editText) {
            override fun onTextChanged(validText: String) {
                validate(text)
                onTextChangedListener(text)
            }

            override fun getValidValue(newText: String, oldText: String): String {
                return if (newText != oldText) {
                    formatter?.invoke(newText) ?: newText
                } else oldText
            }
        })
    }

    private fun setFocusListener() {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (validationState != OK) {
                    if (!applyState(validationState)) updateViewsWithState()
                }
            } else {
                validate(text)
            }
        }
    }

    private fun applyState(state: ValidationState): Boolean {
        val newState = if (forceState) state
        else if (state == ERROR) {
            if (isTypingState()) FILLING
            else state
        } else state


        return if (validationState != newState) {
            validationState = newState
            updateViewsWithState()
            true
        } else false
    }

    private fun updateViewsWithState() {
        when (validationState) {
            FILLING -> setFillingState()
            OK -> setSuccessState()
            ERROR -> setErrorState()
        }
    }

    private fun setFillingState() {
        setState(
            getDefaultMsg(text),
            messageColor,
            hintColor,
            strokeColor,
            defaultIcon
        )
    }

    private fun setErrorState() {
        setState(
            getErrorMsg(text),
            errorMessageColor,
            errorHintColor,
            errorStrokeColor,
            errorIcon
        )
    }

    private fun setSuccessState() {
        setState(
            getSuccessMsg(text),
            successMessageColor,
            successHintColor,
            successStrokeColor,
            successIcon
        )
    }

    private fun setState(
        msg: String,
        @ColorInt messageColor: Int,
        @ColorInt hintTextColor: Int,
        @ColorInt strokeColor: Int,
        iconDrawable: Drawable?
    ) {
        binding.messageContainer.isInvisible = msg.isEmpty()
        message.text = msg
        message.setTextColor(messageColor)

        setTextInputColor(hintTextColor, strokeColor)
        icon.setImageDrawable(iconDrawable)
    }

    private fun setTextInputColor(
        @ColorInt hintTextColor: Int,
        @ColorInt strokeColor: Int
    ) {
        val hintColorList = colorToSimpleStateList(hintTextColor)
        val strokeColorList = colorToSimpleStateList(strokeColor)

        with(inputLayout) {
            defaultHintTextColor = hintColorList
            this.hintTextColor = hintColorList
            setBoxStrokeColorStateList(strokeColorList)
        }
    }

    private fun validate(text: String) {
        applyState(
            validator?.invoke(text) ?: run {
                Logger.logInfo("Validator not set")
                FILLING
            }
        )
    }

    fun setOnTextChangedListener(listener: (String) -> Unit) {
        onTextChangedListener = listener
    }

    fun isTypingState(): Boolean {
        return editText.isFocused
    }
}

enum class ValidationState { FILLING, OK, ERROR }