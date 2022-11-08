//@file:OptIn(InternalAdaptersApi::class)
//
//package com.merseyside.adapters.compose.adapter
//
//import com.merseyside.adapters.feature.selecting.callback.OnItemSelectedListener
//import com.merseyside.adapters.feature.selecting.callback.OnSelectEnabledListener
//import com.merseyside.adapters.config.AdapterConfig
//import com.merseyside.adapters.compose.delegate.ViewDelegatesManager
//import com.merseyside.adapters.compose.view.base.selectable.CSV
//import com.merseyside.adapters.feature.selecting.SelectableItem
//import com.merseyside.adapters.feature.selecting.SelectableMode
//import com.merseyside.adapters.model.AdapterParentViewModel
//import com.merseyside.adapters.model.SelectableAdapterParentViewModel
//import com.merseyside.adapters.utils.InternalAdaptersApi
//import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty
//
//class SelectableViewCompositeAdapter<Parent, Model>(
//    adapterConfig: AdapterConfig<Parent, Model> = AdapterConfig(),
//    override val delegatesManager: ViewDelegatesManager<Parent, Model> = ViewDelegatesManager(),
//    selectableMode: SelectableMode = SelectableMode.SINGLE,
//    override var isAllowToCancelSelection: Boolean = selectableMode == SelectableMode.MULTIPLE,
//    isSelectEnabled: Boolean = true
//) : ViewCompositeAdapter<Parent, Model>(adapterConfig, delegatesManager),
//    ISelectableAdapter<Parent, Model>
//        where Parent : CSV,
//              Model : AdapterParentViewModel<out Parent, Parent>,
//              Model : SelectableItem {
//
//    override var selectedList: MutableList<Model> = ArrayList()
//    override val selectedListeners: MutableList<OnItemSelectedListener<Parent>> = ArrayList()
//    override var onSelectEnableListener: OnSelectEnabledListener? = null
//
//    override var isGroupAdapter: Boolean = false
//
//    override var selectFirstOnAdd: Boolean = false
//        get() {
//            return try {
//                field
//            } finally {
//                field = false
//            }
//        }
//
//    override var selectableMode: SelectableMode = selectableMode
//        set(value) {
//            if (field != value) {
//                field = value
//
//                if (value == SelectableMode.SINGLE) {
//                    if (selectedList.size > 1) {
//                        (1 until selectedList.size).forEach { index ->
//                            selectedList[index].isSelected = false
//                        }
//
//                        selectedList = mutableListOf(selectedList.first())
//                    }
//                }
//            }
//        }
//
//    override var isSelectEnabled: Boolean = isSelectEnabled
//        set(value) {
//            if (value != field) {
//                field = value
//
//                onSelectEnableListener?.onEnabled(value)
//
//                models.isNotNullAndEmpty {
//                    forEach { model ->
//                        model.isSelectable = value
//                    }
//                }
//            }
//        }
//
//    override val internalOnSelect: (Parent) -> Unit = { item ->
//        doAsync {
//            val model = getModelByItem(item)
//            model?.let {
//                if (model.isSelectable) {
//                    setModelSelected(model, true)
//                }
//            }
//        }
//    }
//
//    override fun setModelSelected(model: Model?, isSelectedByUser: Boolean): Boolean {
//        return if (super.setModelSelected(model, isSelectedByUser)) {
//            recyclerView?.invalidateItemDecorations()
//            true
//        } else false
//    }
//
//    override fun removeListeners() {
//        super.removeListeners()
//        selectedListeners.clear()
//    }
//}