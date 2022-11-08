package com.merseyside.merseyLib.features.adapters.movies.adapter.comparator

import com.merseyside.adapters.compose.feature.sorting.ViewComparator
import com.merseyside.adapters.compose.view.text.ComposingTextViewModel
import com.merseyside.adapters.compose.view.text.Text

class TextComparator : ViewComparator<ComposingTextViewModel<Text>>() {

    override fun compareViews(
        model1: ComposingTextViewModel<Text>,
        model2: ComposingTextViewModel<Text>
    ): Int {
        return model1.getText().compareTo(model2.getText())
    }
}