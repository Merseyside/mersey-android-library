package com.merseyside.adapters.listManager.impl

import com.merseyside.adapters.config.update.UpdateLogic
import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.listManager.AdapterListManager
import com.merseyside.adapters.modelList.ModelList
import com.merseyside.adapters.model.VM

open class ListManager<Parent, Model>(
    override val modelList: ModelList<Parent, Model>,
    override val listActions: AdapterListActions<Parent, Model>
) : AdapterListManager<Parent, Model>
        where Model : VM<Parent> {

    override val hashMap: MutableMap<Any, Model> = mutableMapOf()

    override lateinit var updateLogic: UpdateLogic<Parent, Model>

    override suspend fun removeModels(models: List<Model>): Boolean {
        modelList.removeAll(models)
        return true
    }

    override suspend fun updateModel(model: Model, item: Parent): Boolean {
        val payloads = model.payload(item)
        modelList.onModelUpdated(model, payloads)
        return true
    }

//    private suspend fun provideModel(item: Parent): Model {
//        return listActions.provideModelByItem(item)
//    }
//
//    fun getPositionOfItem(item: Parent, models: List<Model> = modelList): Int {
//        models.forEachIndexed { index, model ->
//            if (model.areItemsTheSame(item)) return index
//        }
//
//        throw IllegalArgumentException("No data found")
//    }
//
//    override suspend fun getModelByItem(item: Parent): Model? {
//        return getModelByItem(item, modelList)
//    }
//
//    suspend fun getModelByItem(item: Parent, models: List<Model>): Model? = runWithDefault {
//        if (item is Identifiable<*>) {
//            getModelByIdentifiable(item)
//        } else {
//            models.find { model ->
//                model.areItemsTheSame(item)
//            }
//        }
//    }
//
//    private fun getModelByIdentifiable(identifiable: Identifiable<*>): Model? {
//        return hashMap[identifiable.getId()]
//    }
//
//    @CallSuper
//    open suspend fun createModel(item: Parent): Model {
//        return provideModel(item).also { model ->
//            if (item is Identifiable<*>) {
//                val id = item.getId()
//                id?.let {
//                    hashMap[id] = model
//                }
//            }
//        }
//    }
//
//    suspend fun createModels(items: List<Parent>): List<Model> {
//        return items.map { item -> createModel(item) }
//    }
//
//    override suspend fun add(item: Parent): Model {
//        val model = createModel(item)
//        addModel(model)
//        return model
//    }
//
//    override suspend fun add(items: List<Parent>): List<Model> {
//        val models = createModels(items)
//        addModels(models)
//        return models
//    }
//
//    override suspend fun add(position: Int, item: Parent): Model {
//        requireValidPosition(position)
//        val model = createModel(item)
//        addModel(position, model)
//        return model
//    }
//
//    override suspend fun add(position: Int, items: List<Parent>): List<Model> {
//        requireValidPosition(position)
//        val models = createModels(items)
//        addModels(position, models)
//        return models
//    }
//
//    override suspend fun remove(item: Parent): Model? {
//        return try {
//            val model = getModelByItem(item)
//            model?.let { removeModel(model) }
//            model
//        } catch (e: IllegalArgumentException) {
//            null
//        }
//    }
//
//    override suspend fun update(updateRequest: UpdateRequest<Parent>): Boolean {
//        return update(updateRequest, modelList)
//    }
//
//    override suspend fun remove(items: List<Parent>): List<Model> {
//        return items.mapNotNull { item -> remove(item) }
//    }
//
//    protected suspend fun update(items: List<Parent>): Boolean {
//        return update(UpdateRequest(items))
//    }
//
//    open suspend fun update(dest: List<Model>, source: List<Model> = modelList) {
//        updateLogic.update(dest, source)
//    }
//
//    internal open suspend fun update(
//        updateRequest: UpdateRequest<Parent>,
//        sourceList: List<Model>
//    ): Boolean {
//        return updateLogic.update(updateRequest, sourceList)
//    }
//
//
//    /* Models */
//    override suspend fun addModel(model: Model): Boolean {
//        modelList.add(model)
//        return true
//    }
//
//    override suspend fun addModel(position: Int, model: Model): Boolean {
//        modelList.add(position, model)
//        return true
//    }
//
//    open suspend fun addModels(models: List<Model>): Boolean {
//        modelList.addAll(models)
//        return true
//    }
//
//    open suspend fun addModels(position: Int, models: List<Model>): Boolean {
//        modelList.addAll(position, models)
//        return true
//    }
//
//    open suspend fun removeModel(model: Model): Boolean {
//        return modelList.remove(model)
//    }
//
//    override suspend fun removeModels(models: List<Model>): Boolean {
//        modelList.removeAll(models)
//        return true
//    }
//
//    override suspend fun updateModel(model: Model, item: Parent): Boolean {
//        val payloads = model.payload(item)
//        modelList.onModelUpdated(model, payloads)
//        return true
//    }
//
//    override suspend fun updateModels(pairs: List<Pair<Model, Parent>>) {
//        pairs.forEach { (model, item) -> updateModel(model, item) }
//    }
//
//    override suspend fun move(oldModel: Model, fromPosition: Int, toPosition: Int) {
//        (modelList as SimpleModelList).move(fromPosition, toPosition)
//    }
//
//    override fun getModelByPosition(position: Int): Model {
//        return modelList[position]
//    }
//
//    @InternalAdaptersApi
//    override suspend fun clear() {
//        modelList.clear()
//    }
//
//    fun requireValidPosition(position: Int, models: List<Model> = modelList) {
//        if (models.size < position) throw IllegalArgumentException(
//            "Models size is ${models.size}" +
//                    " but position is $position."
//        )
//    }
}