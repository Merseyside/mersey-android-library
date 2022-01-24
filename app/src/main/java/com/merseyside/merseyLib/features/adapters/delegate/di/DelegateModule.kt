package com.merseyside.merseyLib.features.adapters.delegate.di

import android.app.Application
import androidx.fragment.app.Fragment
import com.merseyside.archy.presentation.di.qualifiers.ApplicationContext
import com.merseyside.archy.presentation.ext.viewModel
import com.merseyside.merseyLib.features.adapters.delegate.model.DelegateViewModel
import dagger.Module
import dagger.Provides

@Module
class DelegateModule(private val fragment: Fragment) {
    @Provides
    fun provideContactsViewModel(
        @ApplicationContext application: Application
    ) = fragment.viewModel { DelegateViewModel(application) }

//    @Provides
//    fun provideContactProducer(): ContactProducer {
//        return ContactProducer()
//    }
}