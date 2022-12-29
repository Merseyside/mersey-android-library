package com.merseyside.merseyLib.features.adapters.news.view

import android.content.Context
import android.os.Bundle
import android.view.View
import com.merseyside.adapters.interfaces.ext.addAsync
import com.merseyside.adapters.interfaces.ext.updateAsync
import com.merseyside.archy.presentation.fragment.BaseBindingFragment
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.databinding.FragmentNewsBinding
import com.merseyside.merseyLib.features.adapters.news.adapter.NewsAdapter
import com.merseyside.merseyLib.features.adapters.news.model.News
import com.merseyside.merseyLib.time.Time
import com.merseyside.utils.view.ext.onClick
import java.util.*

class NewsFragment: BaseBindingFragment<FragmentNewsBinding>() {

    private val adapter = NewsAdapter()

    override fun getLayoutId() = R.layout.fragment_news
    override fun performInjection(bundle: Bundle?, vararg params: Any) {}
    override fun getTitle(context: Context) = getString(R.string.news_title)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireBinding().generate.onClick {
            adapter.updateAsync(generateNews(Random().nextInt(10), Random().nextInt(5)))
            //adapter.addAfter(someNews, generateNews(3))
        }

        requireBinding().recycler.adapter = adapter
        adapter.addAsync(generateNews())
    }

    private fun generateNews(count: Int = 5, startFrom: Int = 1): List<News> {
        return (startFrom..count).map { id ->
            News(
                id = id,
                title = "News #$id",
                description = "Description for $id",
                time = Time.systemTime
            )
        }.reversed()
    }

    companion object {
        val someNews = News(
            id = 3,
            title = "News #5",
            description = "Description for 5",
            time = Time.systemTime
        )
    }
}