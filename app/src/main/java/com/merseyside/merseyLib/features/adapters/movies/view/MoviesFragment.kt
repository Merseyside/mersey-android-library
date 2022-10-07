package com.merseyside.merseyLib.features.adapters.movies.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.merseyside.adapters.feature.composable.adapter.SimpleViewCompositeAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentMoviesBinding
import com.merseyside.merseyLib.features.adapters.movies.adapter.MovieScreenAdapterComposer
import com.merseyside.merseyLib.features.adapters.movies.di.DaggerMoviesComponent
import com.merseyside.merseyLib.features.adapters.movies.di.MoviesModule
import com.merseyside.merseyLib.features.adapters.movies.model.MoviesViewModel

class MoviesFragment : BaseSampleFragment<FragmentMoviesBinding, MoviesViewModel>() {

    private lateinit var movieAdapter: SimpleViewCompositeAdapter
    private lateinit var screenBuilder: MovieScreenAdapterComposer

    override fun hasTitleBackButton() = true
    override fun getBindingVariable() = BR.viewModel
    override fun getLayoutId() = R.layout.fragment_movies

    override fun performInjection(bundle: Bundle?, vararg params: Any) {
        DaggerMoviesComponent.builder()
            .appComponent(appComponent)
            .moviesModule(MoviesModule(this))
            .build().inject(this)
    }

    override fun getTitle(context: Context) = "CompositeScreen"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = SimpleViewCompositeAdapter(lifecycleScope).also { adapter ->
            requireBinding().composite.adapter = adapter
        }

        screenBuilder = MovieScreenAdapterComposer(requireContext(), movieAdapter, viewLifecycleOwner)
    }
}