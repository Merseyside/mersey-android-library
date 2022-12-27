package com.merseyside.adapters.compose

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.merseyside.adapters.feature.composable.HasComposableAdapter

abstract class FragmentAdapterComposer(val fragment: Fragment): HasComposableAdapter {

    override val context: Context
        get() = fragment.requireContext()

    override val viewLifecycleOwner: LifecycleOwner
        get() = fragment.viewLifecycleOwner
}