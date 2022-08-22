package com.merseyside.merseyLib.features.adapters.contacts.producer

import com.merseyside.merseyLib.features.adapters.contacts.entity.ContactGroup
import com.merseyside.merseyLib.kotlin.generateRandomString
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ContactProducer {

    private val contactsFlow = MutableSharedFlow<List<ContactGroup>>()
    fun getContactsSharedFlow(): SharedFlow<List<ContactGroup>> = contactsFlow

    suspend fun generateRandomContacts(count: Int = 100) {
        val contacts = (0 until count).map {
            generateRandomString(6)
        }

        val contactGroups = contacts.groupBy { it.first() }
            .map { (key, values) ->
                ContactGroup(key, values)
            }

       contactsFlow.emit(contactGroups)
    }
}