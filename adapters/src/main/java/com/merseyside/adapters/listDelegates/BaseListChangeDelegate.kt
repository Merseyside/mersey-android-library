package com.merseyside.adapters.listDelegates

import com.merseyside.adapters.listDelegates.interfaces.AdapterListChangeDelegate
import com.merseyside.adapters.listDelegates.utils.UpdateTransaction
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.adapters.utils.runWithDefault
import com.merseyside.merseyLib.kotlin.extensions.subtractBy
import com.merseyside.merseyLib.kotlin.logger.log

abstract class BaseListChangeDelegate<Parent, Model>
    : AdapterListChangeDelegate<Parent, Model>
    where Model : AdapterParentViewModel<out Parent, Parent> {

    abstract fun getModels(): List<Model>

    fun getPositionOfItem(item: Parent, models: List<Model> = getModels()): Int {
        models.forEachIndexed { index, model ->
            if (model.areItemsTheSame(item)) return index
        }

        throw IllegalArgumentException("No data found")
    }

    suspend fun getModelByItem(item: Parent, models: List<Model> = getModels()): Model? {
        val model = getModelByItem(item)
        return model ?: models.find { it.areItemsTheSame(item) }
    }

    fun getItemCount(): Int {
        return getModels().size
    }

    abstract suspend fun createModel(item: Parent): Model

    suspend fun createModels(items: List<Parent>): List<Model> {
        return items.map { item -> createModel(item) }
    }

    abstract suspend fun addModel(model: Model): Boolean

    abstract suspend fun addModels(models: List<Model>): Boolean

    override suspend fun remove(items: List<Parent>): List<Model> {
        return items.mapNotNull { item -> remove(item) }
    }

    abstract suspend fun removeModel(model: Model): Boolean

    abstract suspend fun removeModels(models: List<Model>): Boolean

    abstract suspend fun updateModel(model: Model, item: Parent): Boolean

    protected suspend fun update(items: List<Parent>): Boolean {
        return update(UpdateRequest(items))
    }

    abstract suspend fun applyUpdateTransaction(updateTransaction: UpdateTransaction<Parent, Model>): Boolean

    abstract suspend fun setModels(models: List<Model>)

    protected suspend fun updateModels(updateList: List<Pair<Model, Parent>>) {
        updateList.forEach { (model, item) -> updateModel(model, item) }
    }

    protected suspend fun getUpdateTransaction(
        updateRequest: UpdateRequest<Parent>,
        models: List<Model>
    ): UpdateTransaction<Parent, Model> = runWithDefault {

        val updateTransaction = UpdateTransaction<Parent, Model>()
        with(updateTransaction) {
            if (updateRequest.isDeleteOld) {
                modelsToRemove = findOutdatedModels(updateRequest.list, models)
            }

            val addList = ArrayList<Parent>()
            val updateList = ArrayList<Pair<Model, Parent>>()
            updateRequest.list.forEach { newItem ->
                val model = getModelByItem(newItem)
                if (model == null) {
                    if (updateRequest.isAddNew) addList.add(newItem)
                } else {
                    if (!model.areContentsTheSame(newItem)) {
                        updateList.add(model to newItem)
                    }
                }
            }

            modelsToUpdate = updateList
            itemsToAdd = addList
        }

        updateTransaction
    }

    protected suspend fun findOutdatedModels(
        newItems: List<Parent>,
        models: List<Model>
    ): List<Model> = runWithDefault {
        models.subtractBy(newItems) { oldModel, newItem ->
            oldModel.isDeletable && oldModel.areItemsTheSame(newItem)
        }.toList()
    }

    protected suspend fun findNewModels(newModels: List<Model>, models: List<Model>): List<Model> =
        runWithDefault {
            newModels.subtractBy(models) { newModel, model ->
                model.areItemsTheSame(newModel.item)
            }.toList()
        }

    protected suspend fun findNewItems(newItems: List<Parent>, models: List<Model>): List<Parent> =
        runWithDefault {
            newItems.subtractBy(models) { newItem, model ->
                model.areItemsTheSame(newItem)
            }.toList()
        }
}