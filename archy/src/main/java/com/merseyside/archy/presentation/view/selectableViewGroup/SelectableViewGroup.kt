package com.merseyside.archy.presentation.view.selectableViewGroup

import android.view.View

interface SelectableViewGroup {

    var selectableViews: List<View>

    val setSelected: (view: View) -> Unit
    val setNotSelected: (view: View) -> Unit

    fun setViews(list: List<View>) {
        selectableViews = list
        val onClickListener: (view: View) -> Unit = { view ->
            onViewSelected(view)
            setStates()
        }

        list.forEach { it.setOnClickListener(onClickListener) }
        selectView(list.first())
    }

    fun selectView(view: View) {
        if (selectableViews.contains(view)) {
            onViewSelected(view)
            setStates()
        }
    }

    private fun onViewSelected(view: View) {
        with(view) {
            if (!isSelected) {
                selectableViews.forEach { view ->
                    view.isSelected = this == view
                }
            }
        }
    }

    private fun setStates() {
        selectableViews.forEach { view ->
            if (view.isSelected) setSelected(view)
            else setNotSelected(view)
        }
    }
}