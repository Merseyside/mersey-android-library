package com.merseyside.archy.utils.ext

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.merseyside.utils.ext.put

fun Fragment.navigate(navDirections: NavDirections) {
    findNavController().navigate(navDirections)
}

fun Fragment.navigate(@IdRes fragmentId: Int, vararg params: Pair<String, Any>) {
    val bundle = Bundle().apply {
        params.forEach { (key, value) -> put(key, value) }
    }
    findNavController().navigate(fragmentId, bundle)
}

fun Fragment.navigateUp() {
    findNavController().navigateUp()
}