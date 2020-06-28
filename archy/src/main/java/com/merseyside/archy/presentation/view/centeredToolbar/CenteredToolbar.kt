package com.merseyside.archy.presentation.view.centeredToolbar

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.TextViewCompat
import com.merseyside.archy.R

class CenteredToolbar(context: Context, attributeSet: AttributeSet) :
    Toolbar(context, attributeSet) {
    private var centeredTitleTextView: TextView? = null

    override fun setTitle(title: CharSequence) {
        getCenteredTitleTextView().text = title
    }

    override fun getTitle(): CharSequence {
        return getCenteredTitleTextView().text.toString()
    }

    private fun getCenteredTitleTextView(): TextView {
        if (centeredTitleTextView == null) {
            centeredTitleTextView = TextView(context)
            centeredTitleTextView?.let {
                with(it) {
                    setSingleLine()
                    ellipsize = TextUtils.TruncateAt.END
                    gravity = Gravity.CENTER

                    val textStyle = R.style.TextAppearance_AppCompat_Widget_ActionBar_Title
                    TextViewCompat.setTextAppearance(it, textStyle)
                    val params = LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                    )
                    params.gravity = Gravity.CENTER
                    layoutParams = params
                    addView(centeredTitleTextView)
                }
            }
        }
        return centeredTitleTextView as TextView
    }
}