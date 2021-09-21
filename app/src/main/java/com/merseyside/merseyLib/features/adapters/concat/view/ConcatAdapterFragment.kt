package com.merseyside.merseyLib.features.adapters.concat.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.ConcatAdapter
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentConcatAdapterBinding
import com.merseyside.merseyLib.features.adapters.colors.view.DaggerConcatComponent
import com.merseyside.merseyLib.features.adapters.concat.adapter.AdsAdapter
import com.merseyside.merseyLib.features.adapters.concat.adapter.NewsAdapter
import com.merseyside.merseyLib.features.adapters.concat.di.ConcatModule
import com.merseyside.merseyLib.features.adapters.concat.model.ConcatViewModel

class ConcatAdapterFragment : BaseSampleFragment<FragmentConcatAdapterBinding, ConcatViewModel>() {

    private val concatAdapter = ConcatAdapter()
    private val newsAdapter = NewsAdapter()
    private val adsAdapter = AdsAdapter()

    override fun hasTitleBackButton() = true
    override fun getLayoutId() = R.layout.fragment_concat_adapter
    override fun performInjection(bundle: Bundle?, vararg params: Any) {
        DaggerConcatComponent.builder()
            .appComponent(appComponent)
            .concatModule(ConcatModule(this))
            .build().inject(this)

    }
    override fun getTitle(context: Context) = getString(R.string.concat_title)
    override fun getBindingVariable() = BR.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        concatAdapter.addAdapter(newsAdapter)
        concatAdapter.addAdapter(adsAdapter)

        requireBinding().recycler.adapter = concatAdapter

        viewModel.getNewsFlow().asLiveData().observe(viewLifecycleOwner) {
            newsAdapter.add(it)
        }

        viewModel.getAdsFlow().asLiveData().observe(viewLifecycleOwner) {
            adsAdapter.add(it)
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.startProducers()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopProducer()
    }
}