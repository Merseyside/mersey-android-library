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
import com.merseyside.merseyLib.kotlin.logger.ILogger
import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.merseyLib.kotlin.utils.safeLet
import com.merseyside.merseyLib.time.coroutines.delay
import com.merseyside.merseyLib.time.units.Millis
import com.merseyside.merseyLib.time.units.TimeUnit
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.colorStateList.colorToSimpleStateList
import com.merseyside.utils.delegate.*
import com.merseyside.utils.textWatcher.ValidationTextWatcher
import com.merseyside.utils.view.ext.requireResourceFromAttr
import com.merseyside.utils.view.viewScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.merseyside.utils.attributes.bool
import com.merseyside.utils.attributes.color
import com.merseyside.utils.attributes.dimensionPixelSizeOrNull
import com.merseyside.utils.attributes.drawableOrNull
import com.merseyside.utils.attributes.int
import com.merseyside.utils.attributes.string
import com.google.android.material.R as MaterialStyle

open class ValidationInputView(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int
) : LinearLayout(context, attributeSet, defStyleAttr), ILogger {

    constructor(context: Context, attributeSet: AttributeSet)
            : this(context, attributeSet, R.attr.validationInputViewStyle)

    private val binding: ViewValidationInputBinding by viewBinding(R.layout.view_validation_input)
    private val attrs = AttributeHelper(
        context,
        attributeSet,
        R.styleable.ValidationInputView,
        defStyleAttr
    )

    protected val inputLayout = binding.inputLayout
    protected val editText = binding.editText
    protected val message = binding.message
    protected val icon = binding.icon

    private var onTextChangedListener: (String) -> Unit = {}
    var formatter: ((String) -> String)? = null
    var validator: (suspend (String) -> ValidationState)? = null

    private val debounce by attrs.int(
        resId = R.styleable.ValidationInputView_validDebounce,
        defaultValue = defaultDebounce
    )

    private val timeDebounce: TimeUnit
        get() = Millis(debounce)

    private val inputWidth by attrs.dimensionPixelSizeOrNull(resId = R.styleable.ValidationInputView_validInputWidth)
    private val inputHeight by attrs.dimensionPixelSizeOrNull(resId = R.styleable.ValidationInputView_validInputHeight)

    private val forceState by attrs.bool(resId = R.styleable.ValidationInputView_validForceState)
    private val hintText by attrs.string(resId = R.styleable.ValidationInputView_validHintText, defaultValue = "")
    private val iconMode by attrs.int(resId = R.styleable.ValidationInputView_validIconMode, defaultValue = 0)
    private val inputType by attrs.int(resId = R.styleable.ValidationInputView_validInputType, defaultValue = 1)
    private val maxLength by attrs.int(resId = R.styleable.ValidationInputView_validMaxLength, defaultValue = 0)

    private val textDefault by attrs.string(resId = R.styleable.ValidationInputView_validTextDefault, defaultValue = "")
    private var textSuccess by attrs.string(resId = R.styleable.ValidationInputView_validTextSuccess, defaultValue = "")
    private var textError by attrs.string(resId = R.styleable.ValidationInputView_validTextError, defaultValue = "")

    private val needFillingStateOnInit by attrs.bool(resId = R.styleable.ValidationInputView_validNeedFillingStateOnInit, false)

    protected open val strokeColor by attrs.color(
        resId = R.styleable.ValidationInputView_validStrokeColor,
        defaultValue = requireResourceFromAttr(MaterialStyle.attr.colorOnSurface)
    )

    protected open val successStrokeColor by attrs.color(
        resId = R.styleable.ValidationInputView_validSuccessStrokeColor,
        defaultValue = requireResourceFromAttr(MaterialStyle.attr.colorAccent)
    )

    protected open val errorStrokeColor by attrs.color(
        resId = R.styleable.ValidationInputView_validErrorStrokeColor,
        defaultValue = requireResourceFromAttr(MaterialStyle.attr.colorError)
    )

    protected open val hintColor by attrs.color(
        resId = R.styleable.ValidationInputView_validHintColor,
        defaultValue = requireResourceFromAttr(MaterialStyle.attr.colorOnSurface)
    )

    protected open val successHintColor by attrs.color(
        resId = R.styleable.ValidationInputView_validSuccessHintColor,
        defaultValue = requireResourceFromAttr(MaterialStyle.attr.colorAccent)
    )

    protected open val errorHintColor by attrs.color(
        resId = R.styleable.ValidationInputView_validErrorHintColor,
        defaultValue = requireResourceFromAttr(MaterialStyle.attr.colorError)
    )

    protected open val messageColor by attrs.color(
        resId = R.styleable.ValidationInputView_validMessageColor,
        defaultValue = requireResourceFromAttr(android.R.attr.textColor)
    )

    protected open val successMessageColor by attrs.color(
        resId = R.styleable.ValidationInputView_validSuccessMessageColor,
        defaultValue = requireResourceFromAttr(MaterialStyle.attr.colorAccent)
    )

    protected open val errorMessageColor by attrs.color(
        resId = R.styleable.ValidationInputView_validErrorMessageColor,
        defaultValue = requireResourceFromAttr(MaterialStyle.attr.colorError)
    )

    protected open val defaultIcon by attrs.drawableOrNull(
        resId = R.styleable.ValidationInputView_validIcon
    )
    protected open val successIcon by attrs.drawableOrNull(
        resId = R.styleable.ValidationInputView_validSuccessIcon
    )
    protected open val errorIcon by attrs.drawableOrNull(
        resId = R.styleable.ValidationInputView_validErrorIcon
    )

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

    private var everFocused: Boolean = false

    fun setErrorText(text: String) {
        if (textError != text) {
            textError = text

            if (validationState == ERROR)
                updateViewsWithState()
        }
    }

    fun setSuccessText(text: String) {
        if (textSuccess != text) {
            textSuccess = text
            if (validationState == OK)
                updateViewsWithState()
        }
    }

    private var validationState = FILLING

    init {
        orientation = VERTICAL
        inputLayout.id = View.generateViewId()
        editText.id = View.generateViewId()

        setLayout()

        setWithAttrs()
        setTextWatcher()
        setFocusListener()

        if (needFillingStateOnInit) updateViewsWithState()
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
                everFocused = true
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
            if (isTypingState() || !everFocused) FILLING
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

    private var validationJob: Job? = null

    private fun validate(text: String) {
        validationJob?.cancel()
        safeLet(validator) {
            validationJob = viewScope.launch {
                delay(timeDebounce)
                validateData(it(text))
            }
        } ?: run {
            Logger.logInfo("Validator not set")
            applyState(FILLING)
        }
    }

    private fun validateData(state: ValidationState) {
        try {
            applyState(state)
        } catch (e: CancellationException) {
            Logger.log(e.message)
        }
    }

    fun setOnTextChangedListener(listener: (String) -> Unit) {
        onTextChangedListener = listener
    }

    private fun isTypingState(): Boolean {
        return editText.isFocused
    }

    companion object {
        private const val defaultDebounce = 300
    }

    override val tag: String = "ValidationInputView"
}

enum class ValidationState { FILLING, OK, ERROR }