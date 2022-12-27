package com.merseyside.merseyLib.features.adapters.movies.adapter

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.merseyside.adapters.compose.FragmentAdapterComposer
import com.merseyside.adapters.compose.adapter.SimpleViewCompositeAdapter
import com.merseyside.adapters.compose.delegate.ViewDelegateAdapter
import com.merseyside.adapters.compose.dsl.context.ComposeContext
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
import com.merseyside.adapters.compose.viewProvider.asComposeState
import com.merseyside.adapters.compose.viewProvider.composeState
import com.merseyside.adapters.extensions.onClick
import com.merseyside.adapters.extensions.onItemSelected
import com.merseyside.adapters.feature.selecting.SelectableMode
import com.merseyside.adapters.feature.sorting.Sorting
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.movies.adapter.comparator.TextComparator
import com.merseyside.merseyLib.kotlin.logger.ILogger
import com.merseyside.merseyLib.time.coroutines.delay
import com.merseyside.merseyLib.time.units.Millis
import com.merseyside.utils.randomColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import com.merseyside.merseyLib.features.adapters.movies.adapter.views.MarginComposingList as List
import kotlin.collections.List as ArrayList

class MovieScreenAdapterComposer(
    fragment: Fragment,
    override val adapter: SimpleViewCompositeAdapter,
) : FragmentAdapterComposer(fragment), ILogger {

    override val delegates: ArrayList<ViewDelegateAdapter<out SCV, out ComposingStyle, out ViewAdapterViewModel>> =
        listOf(
            ComposingCheckBoxDelegate(),
            ComposingTextDelegate(),
            ComposingSelectableListDelegate(),
            ComposingListDelegate()
        )

    init {
        fragment.lifecycleScope.launchWhenStarted {
            while(isActive) {
                delay(Millis(400))
                stateFlow.value = randomColor()
            }
        }
    }

    private val stateFlow = MutableStateFlow(R.color.red)

    override suspend fun composeScreen(): ComposeContext.() -> Unit = {

        val color2: Int by stateFlow.asComposeState(this)

        Text("disco",
            style = {
                setTextSize(R.dimen.large_text_size)
                textColor = color2
            })
        {
            text = "Disco time"
        }

        ComposingSelectableList("selectable_list",
            configure = {
                selectableMode = SelectableMode.SINGLE
                onItemSelected { item, isSelected, _ -> isSelected.log("selected") }
            }
        ) {
            CheckBox("kek",
                style = {
                    setTextColor(R.color.red)
                }) {
                onClick { "clicked".log() }
                text = "lol"
                checked = true
            }

            Text("imposter",
                style = { setTextColor(R.color.green) }) {
                text = "I'm an imposter in selectable list"
            }

            CheckBox("kek1",
                style = {
                    setTextColor(R.color.red)
                }) {
                onClick { "clicked".log() }
                text = "lol"
                //checked = true
            }
        }
//
        List("list") {
            List("inner_list1") {
                List("inner_list2") {
                    List("inner_list3") {
                        List("inner_list4",
                            style = {
                                margins = ComposingStyle.Margins(R.dimen.very_small_spacing)
                            },
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
                                style = { setTextColor(R.color.green) }) {
                                text = "some text"
                            }

                            Text("text4_2",
                                style = { setTextColor(R.color.blue_primary) }) {
                                text = "text item 4_2"
                            }
                        }

                        Text("text3_1",
                            style = { setTextColor(R.color.green) }) {
                            text = "text item 3_1"
                        }

                        Text("text3_2",
                            style = { setTextColor(R.color.blue_primary) }) {
                            text = "text item 3_2"
                        }
                    }
//
                    Text("text2_1",
                        style = { setTextColor(R.color.green) }) {
                        text = "text item 2_1"
                    }

                    Text("text2_2",
                        style = { setTextColor(R.color.blue_primary) }) {
                        text = "text item 2_2"
                    }
                }
//
                Text("text1_1",
                    style = { setTextColor(R.color.green) }) {
                    text = "text item 1_1"
                }

                Text("text1_2",
                    style = { setTextColor(R.color.blue_primary) }) {
                    onClick { "click".log() }
                    text = "text item 1_2"
                }
            }

            val color1: Int by stateFlow.asComposeState(this)
            var someState: Int by composeState(this) { 0 }

            Text("text1",
                style = {
                    textColor = color1
                    setTextSize(R.dimen.large_text_size)
                }) {
                text = "Click me!"
                onClick { someState++ }
            }

            Text("text1_clicks",
                style = {
                    setTextColor(R.color.white)
                    setTextSize(R.dimen.text_size)
                }) {
                text = "Clicks count: $someState"
            }
        }
//
        Text("large",
            style = { setTextSize(R.dimen.large_text_size) }) {
            text = "large text size"
        }

        Text("default",
            style = { setBackgroundColor(R.color.green) }) {
            text = "default text with background"
        }
    }

    init {
        invalidateAsync()
    }

    override val tag = "MovieScreen"
}
