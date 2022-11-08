package com.merseyside.merseyLib.features.adapters.movies.adapter

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.merseyside.adapters.compose.SimpleAdapterComposer
import com.merseyside.adapters.compose.adapter.SimpleViewCompositeAdapter
import com.merseyside.adapters.compose.delegate.ViewDelegateAdapter
import com.merseyside.adapters.compose.dsl.context.compose
import com.merseyside.adapters.compose.model.ViewAdapterViewModel
import com.merseyside.adapters.compose.style.ComposingStyle
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.checkBox.CheckBox
import com.merseyside.adapters.compose.view.checkBox.ComposingCheckBoxDelegate
import com.merseyside.adapters.compose.view.list.selectable.ComposingSelectableList
import com.merseyside.adapters.compose.view.list.selectable.ComposingSelectableListDelegate
import com.merseyside.adapters.compose.view.list.simple.ComposingListDelegate
import com.merseyside.adapters.compose.view.list.simple.adapterConfig
import com.merseyside.adapters.compose.view.text.ComposingTextDelegate
import com.merseyside.adapters.compose.view.text.Text
import com.merseyside.adapters.extensions.onClick
import com.merseyside.adapters.extensions.onItemSelected
import com.merseyside.adapters.feature.selecting.SelectableMode
import com.merseyside.adapters.feature.sorting.Sorting
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.movies.adapter.comparator.TextComparator
import com.merseyside.merseyLib.kotlin.extensions.launchDelayed
import com.merseyside.merseyLib.kotlin.logger.ILogger
import com.merseyside.merseyLib.time.units.Seconds
import com.merseyside.merseyLib.features.adapters.movies.adapter.views.MarginComposingList as List
import kotlin.collections.List as ArrayList

class MovieScreenAdapterComposer(
    private val context: Context,
    override val adapter: SimpleViewCompositeAdapter,
    viewLifecycleOwner: LifecycleOwner,
) : SimpleAdapterComposer(viewLifecycleOwner), ILogger {

    private var dataLiveData: LiveData<String>? = null

    override val delegates: ArrayList<ViewDelegateAdapter<out SCV, out ComposingStyle, out ViewAdapterViewModel>> =
        listOf(
            ComposingCheckBoxDelegate(),
            ComposingTextDelegate(),
            ComposingSelectableListDelegate(),
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

        ComposingSelectableList("selectable_list",
            configure = {
                variableId = BR.selectCallback
                selectableMode = SelectableMode.SINGLE

                onItemSelected { item, isSelected, _ -> isSelected.log("selected")}
            }
        ) {
            CheckBox("kek",
                style = {
                    textColor = R.color.red
                }) {
                onClick { "clicked".log() }
                text = "lol"
                checked = true
            }

            Text("imposter",
                style = { textColor = R.color.green }) {
                text = "I'm an imposter in selectable list"
            }

            CheckBox("kek1",
                style = {
                    textColor = R.color.red
                }) {
                onClick { "clicked".log() }
                text = "lol"
                //checked = true
            }
        }

        List("list") {
            List("inner_list1") {
                List("inner_list2") {
                    List("inner_list3") {
                        List("inner_list4",
                            style = { margins = ComposingStyle.Margins(R.dimen.very_small_spacing) },
                            configure = {
                                adapterConfig {
                                    Sorting {
                                        comparator = TextComparator()
                                    }
                                }
                                //decorator = SimpleItemOffsetDecorator(context, R.dimen.very_small_spacing)
                                onClick { item ->
                                    "on item click $item".log()
                                }
                            }) {

                            Text("text4_3",
                                style = { textColor = R.color.green }) {
                                text = "text item 4_3"
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
//
                    Text("text2_1",
                        style = { textColor = R.color.green }) {
                        text = "text item 2_1"
                    }

                    Text("text2_2",
                        style = { textColor = R.color.blue_primary }) {
                        text = "text item 2_2 ${dataLiveData?.value ?: ""}"
                    }
                }
//
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