package com.merseyside.utils.ext

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Point
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.merseyside.utils.view.ViewBaseline
import com.merseyside.utils.view.ext.contains
import com.merseyside.utils.view.ext.getRawCoordPoint

fun View.getResourceFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int? {
    return this.getActivity().getResourceFromAttr(attrColor, typedValue, resolveRefs)
}

@ColorInt
fun View.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    return this.getActivity().getColorFromAttr(attrColor, typedValue, resolveRefs)
}

fun View.getStringFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): String {
    return this.getActivity().getStringFromAttr(attrColor, typedValue, resolveRefs)
}

fun EditText.setTextWithCursor(text: String?) {
    if (this.text.toString() != text) {
        text?.let {
            setText(it)
            setSelection(it.length)
        }
    }
}

fun EditText.setTextWithCursor(text: CharSequence?) {
    setTextWithCursor(text.toString())
}

fun View.getActivity(): AppCompatActivity {
    var context: Context = context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }

    throw IllegalStateException("View hasn't been bind to activity!")
}

/**
 * Base unregistrar for all view callbacks
 */
interface CallbackUnregistrar {
    fun removeCallback()
}

internal class TextChangeListenerUnregistrar(
    private val textView: TextView,
    private val textWatcher: TextWatcher
) : CallbackUnregistrar {

    override fun removeCallback() {
        textView.removeTextChangedListener(textWatcher)
    }
}

fun TextView.addTextChangeListener(
    callback: (
        view: TextView,
        newValue: String?,
        oldValue: String?,
        length: Int,
        start: Int,
        before: Int,
        count: Int
    ) -> Boolean // return true if new value is valid and should be saved
): CallbackUnregistrar {
    val textWatcher = object : TextWatcher {
        private var oldValue: String? = null

        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val newValue = s?.toString()

            if (oldValue != newValue) {
                if (callback(
                        this@addTextChangeListener,
                        newValue,
                        oldValue,
                        newValue?.length ?: 0,
                        start,
                        before,
                        count
                    )
                ) {
                    oldValue = newValue
                }
            }
        }
    }
    this.addTextChangedListener(textWatcher)
    return TextChangeListenerUnregistrar(this, textWatcher)
}

fun TextView.setTextColorAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
) {
    setTextColor(getColorFromAttr(attrColor, typedValue, resolveRefs))
}

fun TextView.setTextSizePx(value: Number) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, value.toFloat())
}

fun View.onClick(onClick: () -> Unit): View.OnClickListener {
    val listener = View.OnClickListener { onClick.invoke() }
    this.setOnClickListener(listener)

    return listener
}

fun View.isFullyVisible(): Boolean {
    val visibleSize = getVisibleSize()
    val drawingSize = getAdjustedSize()

    return visibleSize.x == drawingSize.x && visibleSize.y == drawingSize.y
}

fun View.getVisibleSize(): Point {
    val viewRect = Rect().apply {
        if (!this@getVisibleSize.getGlobalVisibleRect(this)) {
            return Point()
        }
    }

    val visibleWidth = viewRect.width()
    val visibleHeight = viewRect.height()

    return Point(visibleWidth, visibleHeight)
}

fun View.getAdjustedSize(): Point {
    val drawingRect = Rect().apply {
        this@getAdjustedSize.getDrawingRect(this)
    }

    val drawingWidth = drawingRect.width()
    val drawingHeight = drawingRect.height()

    return Point(drawingWidth, drawingHeight)
}

fun View.padding(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) {
    setPadding(left, top, right, bottom)
}

fun View.setHorizontalPadding(size: Int) {
    padding(left = size, right = size)
}

fun View.setVerticalPadding(size: Int) {
    padding(top = size, bottom = size)
}

fun View.setSize(topLeft: Point, bottomRight: Point) {
    layoutParams = layoutParams.apply {
        width = bottomRight.x - topLeft.x
        height = bottomRight.y - topLeft.y
    }
}

fun View.setCoords(
    point: Point,
    baseline: Int = ViewBaseline.UNSPECIFIED
) {
    this.x = when(ViewBaseline.getHorizontalBaseline(baseline)) {
        ViewBaseline.HORIZONTAL_CENTER -> x - width / 2F
        ViewBaseline.HORIZONTAL_END -> x - width.toFloat()
        else -> point.x.toFloat()
    }

    this.y = when(ViewBaseline.getVerticalBaseline(baseline)) {
        ViewBaseline.VERTICAL_CENTER -> y - height / 2F
        ViewBaseline.VERTICAL_BOTTOM -> y - height.toFloat()
        else -> point.y.toFloat()
    }
}

fun View.setCoordPoint(
    point: Point,
    baseline: Int = ViewBaseline.UNSPECIFIED
) {
    setCoords(point, baseline)
}

fun View.getCoordPoint(): Point =
    Point(x.toInt(), y.toInt())

fun View.getRightBottomPoint(): Point =
    Point(x.toInt() + width, y.toInt() + height)

fun View.isInViewBounds(event: MotionEvent): Boolean {
    val rect = android.graphics.Rect()
    getDrawingRect(rect)

    val location = getLocationOnScreen()
    rect.offset(location.x, location.y)

    return rect.contains(event.getRawCoordPoint())
}

fun View.getLocationOnScreen(): Point {
    val location = IntArray(2)
    getLocationOnScreen(location)

    return Point(location[0], location[1])
}

fun View.isSizeChanged(
    size: Point
): Boolean {
    return layoutParams.width != size.x ||
            layoutParams.height != size.y
}