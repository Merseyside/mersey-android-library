package com.merseyside.animators.template

import android.widget.TextView
import com.merseyside.animators.BaseAnimator
import com.merseyside.animators.animator.AlphaAnimator
import com.merseyside.utils.time.TimeUnit
import com.merseyside.utils.time.div

class SetTextFadeOutInAnimator(
    val view: TextView,
    private val text: String,
    private val duration: TimeUnit,
    private val onInvisibleState: (view: TextView) -> Unit
) : AnimatorTemplate() {

    override fun createAnimator(): BaseAnimator {
        return AlphaAnimator(
            AlphaAnimator.Builder(
                view = view,
                duration = duration / 2
            ).apply {
                values(1f, 0f)
            }).apply {
            setLegacyReverse(true) // DK why, but doesn't work with reverse from 'box' (doesn't reverse values and plays the same as start())
            setOnEndCallback { _, isReverse ->
                if (!isReverse) {
                    view.apply {
                        text = this@SetTextFadeOutInAnimator.text
                    }

                    onInvisibleState.invoke(view)
                    reverse()
                }
            }
        }
    }
}