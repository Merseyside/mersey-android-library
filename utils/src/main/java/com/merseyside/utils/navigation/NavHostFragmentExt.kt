package com.merseyside.utils.navigation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import androidx.navigation.fragment.NavHostFragment

fun NavHostFragment.registerLifecycleCallback(
    callback: FragmentLifecycleCallbacks,
    recursive: Boolean = true
): FragmentLifecycleCallbacks {
    return callback.also {
        childFragmentManager.registerFragmentLifecycleCallbacks(
            callback,
            recursive
        )
    }
}

fun NavHostFragment.onFragmentAttached(
    recursive: Boolean = true,
    onFragmentAttached: (fragment: Fragment) -> Unit
): FragmentLifecycleCallbacks {
    val callback = object : FragmentLifecycleCallbacks() {
        override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
            super.onFragmentAttached(fm, f, context)
            onFragmentAttached(f)
        }

    }
    return registerLifecycleCallback(callback, recursive)
}

fun NavHostFragment.onFragmentViewCreated(
    recursive: Boolean = true,
    onViewCreated: (fragment: Fragment, savedInstanceState: Bundle?) -> Unit
): FragmentLifecycleCallbacks {
    val callback = object : FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            onViewCreated(f, savedInstanceState)
        }
    }
    return registerLifecycleCallback(callback, recursive)
}

fun NavHostFragment.onFragmentViewDestroyed(
    recursive: Boolean = true,
    onViewDestroyed: (fragment: Fragment) -> Unit
): FragmentLifecycleCallbacks {
    val callback = object : FragmentLifecycleCallbacks() {
        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            super.onFragmentViewDestroyed(fm, f)
            onViewDestroyed(f)
        }
    }
    return registerLifecycleCallback(callback, recursive)
}

fun NavHostFragment.onFragmentDetached(
    recursive: Boolean = true,
    onFragmentDetached: (fragment: Fragment) -> Unit
): FragmentLifecycleCallbacks {
    val callback = object : FragmentLifecycleCallbacks() {

        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
            super.onFragmentDetached(fm, f)
            onFragmentDetached(f)
        }
    }
    return registerLifecycleCallback(callback, recursive)
}