package com.example.homework3

import androidx.lifecycle.MutableLiveData

object ContactService {

    private val contactList = contacts()
    private var autoIncrementId = contactList.size

    val liveData = MutableLiveData<List<ContactModel>>()

    fun addContactItem(contactModel: ContactModel) {
        contactModel.id = ++autoIncrementId
        contactList.add(contactModel)
        getContactList()
    }

    fun deleteShopItem(contactModel: ContactModel) {
        contactList.remove(contactModel)
        getContactList()
    }

    fun deleteSelectedItems(listItem: List<ContactModel>) {
        listItem.forEach {
            val element = it.copy(isEnabled = true)
            contactList.remove(element)
        }
        getContactList()
    }

    fun editContactItem(contactModel: ContactModel) {
        val oldElement = getContactItem(contactModel.id)
        contactList.remove(oldElement)
        contactList.add(contactModel)
        getContactList()
    }

    fun editEnabledContactItem(contactModel: ContactModel) {
        val oldElement = getContactItem(contactModel.id)
        val newItem = contactModel.copy(isEnabled = !contactModel.isEnabled)
        contactList.remove(oldElement)
        contactList.add(newItem)
        getContactList()
    }

    private fun getContactItem(contactModelId: Int): ContactModel {
        return contactList.find {
            it.id == contactModelId
        } ?: throw RuntimeException("Element with id $contactModelId not found")
    }

    fun getContactList() {
        contactList.sortBy { it.id }
        liveData.value = contactList.toList()
    }
}