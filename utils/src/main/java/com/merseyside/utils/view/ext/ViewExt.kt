package com.merseyside.utils.view.ext

import android.content.Context
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.graphics.Point
import android.graphics.Rect
import android.text.TextWatcher
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import com.merseyside.merseyLib.kotlin.utils.safeLet
import com.merseyside.merseyLib.time.units.TimeUnit
import com.merseyside.utils.delayedThread
import com.merseyside.utils.ext.*
import com.merseyside.utils.view.ViewBaseline

fun View.getResourceFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int? {
    return this.getActivity().getResourceFromAttr(attrColor, typedValue, resolveRefs)
}

fun View.requireResourceFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    return getResourceFromAttr(attrColor, typedValue, resolveRefs) ?: throw NullPointerException()
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

fun View.onClick(onClick: (View) -> Unit): View.OnClickListener {
    val listener = View.OnClickListener { view -> onClick(view) }
    setOnClickListener(listener)

    return listener
}

fun View.onClickDebounce(debounce: TimeUnit, onClick: (View) -> Unit): View.OnClickListener {

    return onClick { view ->
        setOnClickListener(null)
        onClick(view)
        delayedThread(debounce) {
            onClickDebounce(debounce, onClick)
        }
    }
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
    this.x = when (ViewBaseline.getHorizontalBaseline(baseline)) {
        ViewBaseline.HORIZONTAL_CENTER -> x - width / 2F
        ViewBaseline.HORIZONTAL_END -> x - width.toFloat()
        else -> point.x.toFloat()
    }

    this.y = when (ViewBaseline.getVerticalBaseline(baseline)) {
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
    val rect = Rect()
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
) = layoutParams.width != size.x || layoutParams.height != size.y

fun View.setMarginsRes(
    @DimenRes left: Int? = null,
    @DimenRes top: Int? = null,
    @DimenRes right: Int? = null,
    @DimenRes bottom: Int? = null
) {
    updateLayoutParams {
        setMargins(left, top, right, bottom)
    }
}

fun View.getCurrentDrawableState(matchStates: IntArray): Int? {
    return matchStates.find { state ->
        drawableState.find { it == state } != null
    }
}

@ColorInt
fun View.getColorByCurrentState(colorStateList: ColorStateList, matchStates: IntArray): Int {
    val currentState = getCurrentDrawableState(matchStates)

    return safeLet(currentState) {
        colorStateList.getColorForState(it)
    } ?: colorStateList.defaultColor
}