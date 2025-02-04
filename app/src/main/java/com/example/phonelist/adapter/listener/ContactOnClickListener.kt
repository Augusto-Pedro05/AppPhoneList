package com.example.phonelist.adapter.listener

import com.example.phonelist.model.ContactModel

class ContactOnClickListener (val clickListener: (contact: ContactModel) -> Unit) {
    fun onClick(contact: ContactModel) = clickListener
}