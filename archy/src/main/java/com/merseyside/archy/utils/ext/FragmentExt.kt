package com.merseyside.archy.utils.ext

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(navDirections: NavDirections) {
    findNavController().navigate(navDirections)
}

fun Fragment.navigate(@IdRes fragmentId: Int) {
    findNavController().navigate(fragmentId)
}

fun Fragment.navigateUp() {
    findNavController().navigateUp()
}