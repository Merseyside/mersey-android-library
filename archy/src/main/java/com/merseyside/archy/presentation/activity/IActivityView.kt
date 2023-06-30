package com.merseyside.archy.presentation.activity

import android.content.Context
import androidx.appcompat.widget.Toolbar
import com.merseyside.archy.presentation.fragment.BaseFragment
import com.merseyside.archy.presentation.view.IView
import com.merseyside.archy.presentation.view.OnKeyboardStateListener
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar

interface IActivityView : IView {

    fun getContext(): Context

    fun registerKeyboardListener(listener: OnKeyboardStateListener): Unregistrar

    fun getLanguage(): String?

    fun setFragmentToolbar(fragmentToolbar: Toolbar?)

    fun setBarVisibility(isVisible: Boolean)

    fun setFragmentResult(fragmentResult: BaseFragment.FragmentResult)
}