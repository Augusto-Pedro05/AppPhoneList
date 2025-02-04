package com.example.phonelist.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.phonelist.adapter.listener.ContactOnClickListener
import com.example.phonelist.adapter.viewholder.ContactViewHolder
import com.example.phonelist.model.ContactModel

class ContactListAdapter(private val contactList: List<ContactModel>,
    private val contactOnClickListener: ContactOnClickListener) : RecyclerView.Adapter<ContactViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}