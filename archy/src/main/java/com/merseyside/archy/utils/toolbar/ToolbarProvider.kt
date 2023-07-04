package com.merseyside.archy.utils.toolbar

import androidx.appcompat.widget.Toolbar

interface ToolbarProvider : NavigationToolbarManager {
    fun getToolbar(): Toolbar

    /**
     * Sets new toolbar to action activity's actionBar
     */
    override fun setupToolbar() {
        baseActivity.setFragmentToolbar(getToolbar())
        super.setupToolbar()
    }

}