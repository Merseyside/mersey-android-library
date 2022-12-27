package com.merseyside.merseyLib.features.adapters.movies.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import com.merseyside.adapters.compose.adapter.SimpleViewCompositeAdapter
import com.merseyside.adapters.config.config
import com.merseyside.adapters.config.init.initAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentMoviesBinding
import com.merseyside.merseyLib.features.adapters.movies.adapter.MovieScreenAdapterComposer
import com.merseyside.merseyLib.features.adapters.movies.di.DaggerMoviesComponent
import com.merseyside.merseyLib.features.adapters.movies.di.MoviesModule
import com.merseyside.merseyLib.features.adapters.movies.model.MoviesViewModel
import java.nio.ByteBuffer

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

        movieAdapter = initAdapter(::SimpleViewCompositeAdapter) {
            coroutineScope = lifecycleScope
        }.also { adapter -> requireBinding().composite.adapter = adapter }

        screenBuilder = MovieScreenAdapterComposer(this, movieAdapter)

        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()

        val str = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"2048\" height=\"1024\">  <g><defs>   <linearGradient id=\"gradientae3b9474-b95d-4b73-8262-bde6130ab5eb\" x1=\"0%\" y1=\"0%\" x2=\"0%\" y2=\"100%\">" +
                "     <stop offset=\"0%\" stop-color=\"black\" stop-opacity=\"0\" />     <stop offset=\"70%\" stop-color=\"white\" stop-opacity=\"1\" />" +
                "     <stop offset=\"100%\" stop-color=\"white\" stop-opacity=\"1\" />   </linearGradient>   <mask id=\"gradient-maskae3b9474-b95d-4b73-8262-bde6130ab5eb\">" +
                "     <rect x=\"794.2145775747783\" y=\"296.6421041159125\" width=\"61.44\" height=\"61.44\" fill=\"url(#gradientae3b9474-b95d-4b73-8262-bde6130ab5eb)\"  />" +
                "   </mask>  </defs>    <circle      cx=\"824.9345775747784\"      cy=\"327.36210411591253\"      stroke=\"red\"      stroke-width=\"10.239999999999998\"      r=\"25.6\"" +
                "      fill=\"transparent\"      mask=\"url(#gradient-maskae3b9474-b95d-4b73-8262-bde6130ab5eb)\"    /></g><g>" +
                "                    <mask id=\"arrowTip-mask5b8f704b-851e-446f-ac3f-715f65c77656\" maskUnits=\"userSpaceOnUse\" x=\"0\" y=\"0\" width=\"2048\" height=\"1024\"> " +
                "                       <rect x=\"0\" y=\"0\" width=\"2048\" height=\"1024\" fill=\"rgb(255,255,255)\"/>" +
                "                        <rect x=\"665.7925453901003\" y=\"781.3897022914837\" width=\"22.518936140931636\" height=\"22.518936140931636\" fill=\"rgb(0,0,0)\" transform=\"rotate(197.18635668513696, 677.0520134605661, 781.3897022914837)\"/> " +
                "                   </mask>  <path d=\"M 817.3637159328711 327.73344515584677 Q 747.2078646967187 554.5615737236651, 677.0520134605661 781.3897022914837\" stroke-dasharray=\"10.24 0\" stroke=\"red\" stroke-width=\"10.24\" fill=\"transparent\" " +
                "mask=\"url(#arrowTip-mask5b8f704b-851e-446f-ac3f-715f65c77656)\"/>  <polygon points=\"677.0520134605661,781.3897022914837 665.7925453901003, 803.9086384324153 688.3114815310319,803.9086384324153\" " +
                "transform=\"rotate(197.18635668513696, 677.0520134605661, 781.3897022914837)\" fill=\"red\"/></g></svg>"

        requireBinding().vector.load(ByteBuffer.wrap(str.toByteArray()), imageLoader)
    }
}