package com.merseyside.adapters.listManager.impl

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.interfaces.nested.AdapterNestedListActions
import com.merseyside.adapters.listManager.AdapterNestedListManager
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.adapters.modelList.ModelList

class NestedListManager<Parent, Model, InnerData, InnerAdapter>(
    modelList: ModelList<Parent, Model>,
    override val listActions: AdapterNestedListActions<Parent, Model, InnerData, InnerAdapter>,
) : ListManager<Parent, Model>(modelList, listActions),
    AdapterNestedListManager<Parent, Model, InnerData, InnerAdapter>
        where Model : NestedAdapterParentViewModel<out Parent, Parent, InnerData>,
              InnerAdapter : BaseAdapter<InnerData, out AdapterParentViewModel<out InnerData, InnerData>> {

    //    private fun provideNestedAdapter(model: Model): InnerAdapter {
//        return nestedListActions.getNestedAdapterByModel(model)
//    }
//
//    override suspend fun remove(item: Parent): Model? {
//        return super.remove(item)?.also { model ->
//            removeNestedAdapterByModel(model)
//        }
//    }
//
//    override suspend fun createModel(item: Parent): Model {
//        return super.createModel(item).also { model ->
//            val adapter = provideNestedAdapter(model)
//            val innerDataList = model.getNestedData()
//
//            innerDataList?.let { data -> adapter.add(data) }
//        }
//    }
//
//    override suspend fun removeModels(models: List<Model>): Boolean {
//        models.forEach { model ->
//            removeNestedAdapterByModel(model)
//        }
//        return super.removeModels(models)
//    }
//
//    override suspend fun updateModel(model: Model, item: Parent): Boolean {
//        val isUpdated = super.updateModel(model, item)
//        if (isUpdated) {
//            val adapter = provideNestedAdapter(model)
//            val innerDataList = model.getNestedData()
//            innerDataList?.let { data -> adapter.update(UpdateRequest(data)) }
//        }
//
//        return isUpdated
//    }
//
//    private fun removeNestedAdapterByModel(model: Model): Boolean {
//        nestedListActions.removeNestedAdapterByModel(model)
//        return true
//    }

//    override suspend fun removeModels(models: List<Model>): Boolean {
//        models.forEach { model ->
//            removeNestedAdapterByModel(model)
//        }
//        return super.removeModels(models)
//    }
//
//    override suspend fun updateModel(model: Model, item: Parent): Boolean {
//        val isUpdated = super.updateModel(model, item)
//        if (isUpdated) {
//            val adapter = provideInnerAdapter(model)
//            val innerDataList = model.getNestedData()
//            innerDataList?.let { data -> adapter.update(UpdateRequest(data)) }
//        }
//
//        return isUpdated
//    }

}