package com.merseyside.merseyLib.features.adapters.movies.adapter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.merseyside.adapters.extensions.onClick
import com.merseyside.adapters.feature.compositeScreen.SCV
import com.merseyside.adapters.feature.compositeScreen.ScreenComposer
import com.merseyside.adapters.feature.compositeScreen.delegate.ViewAdapterViewModel
import com.merseyside.adapters.feature.compositeScreen.delegate.ViewDelegateAdapter
import com.merseyside.adapters.feature.compositeScreen.dsl.context.compose
import com.merseyside.adapters.feature.compositeScreen.view.list.ComposingListDelegate
import com.merseyside.adapters.feature.compositeScreen.view.text.ComposingText as Text
import com.merseyside.adapters.feature.compositeScreen.view.text.ComposingTextDelegate
import com.merseyside.adapters.feature.style.ComposingStyle
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.kotlin.extensions.launchDelayed
import com.merseyside.merseyLib.time.units.Seconds
import com.merseyside.merseyLib.features.adapters.movies.adapter.views.MarginComposingList as List


class MovieScreenComposer(
    adapter: MovieCompositeAdapter,
    viewLifecycleOwner: LifecycleOwner
) : ScreenComposer(adapter, viewLifecycleOwner) {

    private var dataLiveData: LiveData<String>? = null

    override val delegates: kotlin.collections.List<ViewDelegateAdapter<out SCV, *, out ViewAdapterViewModel>> = listOf(
        ComposingTextDelegate(),
        ComposingListDelegate()
    )

    override suspend fun composeScreen() = compose {
        Text("shrinked") {
            style = {
                width = 150
                height = 400
                textColor = R.color.red
            }
            text = "shrinked width text"
        }

        if (dataLiveData?.value != null) {
            Text("data") {
                text = "Text from data source ${dataLiveData?.value}"
            }
        }

        List("list") {
            List("inner_list1") {
                List("inner_list2") {
                    List("inner_list3") {
                        List("inner_list4",
                            initList = {
                                style =
                                    { margins = ComposingStyle.Margins(R.dimen.very_small_spacing) }
                                onClick { item ->
                                    "on item click $item".log()
                                }
                            }
                        ) {

                            Text("text4_1") {
                                style = { textColor = R.color.green }
                                text = "text item 4_1"
                            }

                            Text("text4_2") {
                                style = { textColor = R.color.blue_primary }
                                text = "text item 4_2 ${dataLiveData?.value ?: ""}"
                            }
                        }

                        Text("text3_1") {
                            style = { textColor = R.color.green }
                            text = "text item 3_1"
                        }

                        Text("text3_2") {
                            style = { textColor = R.color.blue_primary }
                            text = "text item 3_2 ${dataLiveData?.value ?: ""}"
                        }
                    }

                    Text("text2_1") {
                        style = { textColor = R.color.green }
                        text = "text item 2_1"
                    }

                    Text("text2_2") {
                        style = { textColor = R.color.blue_primary }
                        text = "text item 2_2 ${dataLiveData?.value ?: ""}"
                    }
                }

                Text("text1_1") {
                    style = { textColor = R.color.green }
                    text = "text item 1_1"
                }

                Text("text1_2") {
                    onClick { "click".log() }
                    style = { textColor = R.color.blue_primary }
                    text =
                        if (dataLiveData?.value == null) "text item 1_2" else "updated text item 1_2"
                }
            }


            Text("text1") {
                style = { textColor = R.color.green }
                text = "text item 1"
            }
        }

        Text("large") {
            style = { textSize = R.dimen.large_text_size }
            text = "large text size"
        }

        Text("default") {
            text = "dafault text with background"
            style = { backgroundColor = R.color.green }
        }
    }


    fun addTextDataSource(ld: LiveData<String>) {
        dataLiveData = ld
        addDataSource(ld)
    }

    init {
        buildAsync()

        adapter.scope.launchDelayed(Seconds(2).millis) {
            val mld = MutableLiveData("some data")
            addTextDataSource(mld)
        }
    }
}