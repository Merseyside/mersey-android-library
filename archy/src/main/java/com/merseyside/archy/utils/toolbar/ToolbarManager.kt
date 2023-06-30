package com.merseyside.archy.utils.toolbar

import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.merseyside.archy.presentation.activity.BaseActivity

/**
 * Manages toolbar that already passed into activity's ActionBar
 */
interface ToolbarManager {

    val baseActivity: BaseActivity

    /**
     * If @return true then inherited instance can update current toolbar
     * false otherwise
     */
    fun isToolbarManagerEnabled(): Boolean = false

    /**
     * Just updates current toolbar.
     */
    fun setupToolbar() {
        if (isNavigateUpEnabled()) {
            getActionBar()?.setDisplayHomeAsUpEnabled(true)
            baseActivity.getToolbar()?.setNavigationOnClickListener { onNavigateUpClicked() }
        } else {
            getActionBar()?.setDisplayHomeAsUpEnabled(false)
        }
    }

    fun isNavigateUpEnabled(): Boolean

    /**
     * Calls when toolbar is not null and visible
     */
    fun setupNavigateUp(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener { onNavigateUpClicked() }
    }

    fun onNavigateUpClicked()

    fun getCurrentToolbar(): Toolbar? {
        return baseActivity.getToolbar()
    }

    private fun getActionBar(): ActionBar? {
        return baseActivity.supportActionBar
    }

}