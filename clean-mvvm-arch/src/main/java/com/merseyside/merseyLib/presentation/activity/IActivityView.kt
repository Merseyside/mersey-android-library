package com.merseyside.merseyLib.presentation.activity

import android.content.Context
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.merseyside.merseyLib.presentation.fragment.BaseFragment
import com.merseyside.merseyLib.presentation.view.IView
import com.merseyside.merseyLib.presentation.view.OnKeyboardStateListener
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar

interface IActivityView : IView {

    fun getContext(): Context

    fun hideKeyboard(context: Context? = getContext(), view: View)

    fun registerKeyboardListener(listener: OnKeyboardStateListener): Unregistrar

    fun onBackPressed()

    fun getLanguage(): String?

    fun setFragmentToolbar(toolbar: Toolbar?)

    fun setFragmentResult(fragmentResult: BaseFragment.FragmentResult)
}