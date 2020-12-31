package com.merseyside.archy.presentation.ext

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.merseyside.archy.presentation.model.ParcelableViewModel
import com.merseyside.utils.ext.isNotNullAndEmpty

@Suppress("UNCHECKED_CAST")
fun <VM : ViewModel> Fragment.viewModel(
    key: String? = null,
    bundle: Bundle? = null,
    factory: () -> VM
): VM {
    val factoryViewModel = factory()
    val viewModelProviderFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (bundle.isNotNullAndEmpty() &&
                factoryViewModel is ParcelableViewModel) {
                factoryViewModel.readFrom(bundle)
            }

            return factoryViewModel as T
        }
    }

    return if (key != null) {
        ViewModelProvider(this, viewModelProviderFactory)[key, factoryViewModel::class.java]
    } else {
        ViewModelProvider(this, viewModelProviderFactory)[factoryViewModel::class.java]
    }
}