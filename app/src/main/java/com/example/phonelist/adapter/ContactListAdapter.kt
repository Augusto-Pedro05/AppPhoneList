package com.example.phonelist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.phonelist.R
import com.example.phonelist.adapter.listener.ContactOnClickListener
import com.example.phonelist.adapter.viewholder.ContactViewHolder
import com.example.phonelist.model.ContactModel

class ContactListAdapter(private val contactList: List<ContactModel>,
    private val contactOnClickListener: ContactOnClickListener) : RecyclerView.Adapter<ContactViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.textName.text = contact.name
        if(contact.imageId > 0){
            holder.image.setImageResource(contact.imageId)
        }else{
            holder.image.setImageResource(R.drawable.contacts)
        }
        holder.itemView.setOnClickListener {
            contactOnClickListener.clickListener(contact)
        }
    }

}