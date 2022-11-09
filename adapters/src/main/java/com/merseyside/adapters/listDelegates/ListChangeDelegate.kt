@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.listDelegates

import androidx.annotation.CallSuper
import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.listDelegates.utils.UpdateTransaction
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.adapters.utils.runWithDefault
import com.merseyside.merseyLib.kotlin.contract.Identifiable
import com.merseyside.merseyLib.kotlin.logger.ILogger

abstract class ListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>
    : BaseListChangeDelegate<Parent, Model>(), ILogger {

    abstract val listActions: AdapterListActions<Parent, Model>

    protected val hashMap: MutableMap<Any, Model> = mutableMapOf()

    final override fun getModels(): List<Model> = listActions.models

    override suspend fun add(item: Parent): Model {
        val model = createModel(item)
        addModel(model)
        return model
    }

    override suspend fun add(items: List<Parent>): List<Model> {
        val models = createModels(items)
        addModels(models)
        return models
    }

    override suspend fun remove(item: Parent): Model? {
        return try {
            val model = getModelByItem(item)
            model?.let { removeModel(model) }
            model
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override suspend fun clear() {
        listActions.removeAll()
    }

    override suspend fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        val updateTransaction = getUpdateTransaction(updateRequest, getModels())
        return applyUpdateTransaction(updateTransaction)
    }

    override suspend fun addOrUpdate(items: List<Parent>) {
        if (getModels().isEmpty()) add(items)
        else update(items)
    }

    override suspend fun applyUpdateTransaction(
        updateTransaction: UpdateTransaction<Parent, Model>
    ): Boolean {
        with(updateTransaction) {
            if (modelsToRemove.isNotEmpty()) {
                removeModels(modelsToRemove)
            }

            if (modelsToUpdate.isNotEmpty()) {
                updateModels(modelsToUpdate)
            }

            if (itemsToAdd.isNotEmpty()) {
                add(itemsToAdd)
            }
        }

        return !updateTransaction.isEmpty()
    }

    override suspend fun setModels(models: List<Model>) {
        val modelsToRemove = findOutdatedModels(models.map { it.item }, getModels())
        removeModels(modelsToRemove)

        val modelsToAdd = findNewModels(models, getModels())
        addModels(modelsToAdd)
    }


    override suspend fun addModel(model: Model): Boolean {
        listActions.addModel(model)
        return true
    }

    override suspend fun addModels(models: List<Model>): Boolean {
        listActions.addModels(models)
        return true
    }

    override suspend fun removeModel(model: Model): Boolean {
        return listActions.removeModel(model)
    }

    override suspend fun removeModels(models: List<Model>): Boolean {
        return listActions.removeModels(models)
    }

    override suspend fun updateModel(model: Model, item: Parent): Boolean {
        return listActions.updateModel(model, item)
    }

    @CallSuper
    override suspend fun createModel(item: Parent): Model {
        return listActions.provideModelByItem(item).also { model ->
            if (item is Identifiable<*>) {
                val id = item.getId()
                id?.let {
                    hashMap[id] = model
                }
            }
        }
    }

    override suspend fun getModelByItem(item: Parent): Model? = runWithDefault {
        if (item is Identifiable<*>) {
            getModelByIdentifiable(item)
        } else {
            getModels().find { model ->
                model.areItemsTheSame(item)
            }
        }
    }

    private fun getModelByIdentifiable(identifiable: Identifiable<*>): Model? {
        return hashMap[identifiable.getId()]
    }

    override val tag: String = "ListChangeDelegate"
}