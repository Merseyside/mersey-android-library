package com.merseyside.merseyLib.features.adapters.movies.adapter

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.merseyside.adapters.decorator.SimpleItemOffsetDecorator
import com.merseyside.adapters.extensions.onClick
import com.merseyside.adapters.extensions.onItemSelected
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.SimpleAdapterComposer
import com.merseyside.adapters.compose.adapter.SimpleViewCompositeAdapter
import com.merseyside.adapters.compose.delegate.ViewDelegateAdapter
import com.merseyside.adapters.compose.dsl.context.compose
import com.merseyside.adapters.compose.model.ViewAdapterViewModel
import com.merseyside.adapters.compose.view.list.selectable.ComposingSelectableList
//import com.merseyside.adapters.compose.view.list.selectable.ComposingSelectableListDelegate
import com.merseyside.adapters.compose.view.list.simple.ComposingListDelegate
import com.merseyside.adapters.compose.view.text.ComposingText as Text
import com.merseyside.adapters.compose.view.text.ComposingTextDelegate
import com.merseyside.adapters.compose.style.ComposingStyle
import com.merseyside.merseyLib.R
import kotlin.collections.List as ArrayList
import com.merseyside.merseyLib.kotlin.extensions.launchDelayed
import com.merseyside.merseyLib.kotlin.logger.ILogger
import com.merseyside.merseyLib.time.units.Seconds
import com.merseyside.merseyLib.features.adapters.movies.adapter.views.MarginComposingList as List

class MovieScreenAdapterComposer(
    private val context: Context,
    override val adapter: SimpleViewCompositeAdapter,
    viewLifecycleOwner: LifecycleOwner,
) : SimpleAdapterComposer(viewLifecycleOwner), ILogger {

    private var dataLiveData: LiveData<String>? = null

    override val delegates: ArrayList<ViewDelegateAdapter<out SCV, *, out ViewAdapterViewModel>> =
        listOf(
            //ComposingSelectableListDelegate(),
            ComposingTextDelegate(),
            ComposingListDelegate()
        )

    override suspend fun composeScreen() = compose {
        Text("shrinked",
            style = {
                width = 150
                height = 400
                textColor = R.color.red
            })
        {
            text = "shrinked width text"
        }

        if (dataLiveData?.value != null) {
            Text("data") {
                text = "Text from data source ${dataLiveData?.value}"
            }
        }

//        ComposingSelectableList("selectable_list",
//            initList = {
//                onItemSelected { item, isSelected, _ -> isSelected.log("selected")}
//            }
//        ) {
//            Text("kek") {
//                onClick { "clicked".log() }
//                text = "lol"
//            }
//        }

        List("list") {
            List("inner_list1") {
                List("inner_list2") {
                    List("inner_list3") {
                        List("inner_list4",
                            style = { margins = ComposingStyle.Margins(R.dimen.very_small_spacing) },
                            initList = {
                                decorator = SimpleItemOffsetDecorator(context, R.dimen.very_small_spacing)
                                onClick { item ->
                                    "on item click $item".log()
                                }
                            }) {

                            Text("text4_1",
                                style = { textColor = R.color.green }) {
                                text = "text item 4_1"
                            }

                            Text("text4_2",
                                style = { textColor = R.color.blue_primary }) {
                                text = "text item 4_2 ${dataLiveData?.value ?: ""}"
                            }
                        }

                        Text("text3_1",
                            style = { textColor = R.color.green }) {
                            text = "text item 3_1"
                        }

                        Text("text3_2",
                            style = { textColor = R.color.blue_primary }) {
                            text = "text item 3_2 ${dataLiveData?.value ?: ""}"
                        }
                    }

                    Text("text2_1",
                        style = { textColor = R.color.green }) {
                        text = "text item 2_1"
                    }

                    Text("text2_2",
                        style = { textColor = R.color.blue_primary }) {
                        text = "text item 2_2 ${dataLiveData?.value ?: ""}"
                    }
                }

                Text("text1_1",
                    style = { textColor = R.color.green }) {
                    text = "text item 1_1"
                }

                Text("text1_2",
                    style = { textColor = R.color.blue_primary }) {
                    onClick { "click".log() }
                    text =
                        if (dataLiveData?.value == null) "text item 1_2" else "updated text item 1_2"
                }
            }


            Text("text1",
                style = { textColor = R.color.green }) {
                text = "text item 1"
            }
        }

        Text("large",
            style = { textSize = R.dimen.large_text_size }) {
            text = "large text size"
        }

        Text("default",
            style = { backgroundColor = R.color.green }) {
            text = "dafault text with background"
        }
    }


    fun addTextDataSource(ld: LiveData<String>) {
        dataLiveData = ld
        addDataSource(ld)
    }

    init {
        invalidateAsync()

        adapter.adapterConfig.coroutineScope.launchDelayed(Seconds(2).millis) {
            val mld = MutableLiveData("some data")
            addTextDataSource(mld)
        }
    }

    override val tag = "MovieScreen"
}