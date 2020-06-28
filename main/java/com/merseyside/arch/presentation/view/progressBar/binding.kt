package com.merseyside.archy.presentation.view.progressBar

import androidx.databinding.BindingAdapter
import com.merseyside.archy.presentation.view.progressBar.TextProgressBar

@BindingAdapter("bind:visibility")
fun setProgressBarVisibility(progress: TextProgressBar, visibility: Int) {
    progress.visibility = visibility
}

@BindingAdapter("bind:text")
fun setText(progress: TextProgressBar, text: String?) {
    progress.setText(text)
}