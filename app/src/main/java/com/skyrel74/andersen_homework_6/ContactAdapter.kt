package com.skyrel74.andersen_homework_6

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skyrel74.andersen_homework_6.databinding.ItemContactBinding
import java.util.*

class ContactAdapter(
    private val contactList: MutableList<Contact>,
    private val onClick: (Contact) -> Unit,
    private val onLongClick: (Contact) -> Unit
) : ListAdapter<Contact, ContactAdapter.ContactViewHolder>(
        object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact) =
                oldItem.imageUrl == newItem.imageUrl
                        && oldItem.name == newItem.name
                        && oldItem.surname == newItem.surname
                        && oldItem.phone == newItem.phone

        }), Filterable {

    var filteredContactList = contactList

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Contact) {
            binding.apply {
                Glide.with(itemView)
                    .load(item.imageUrl)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(ivContact)
                tvName.text = "${item.name}  ${item.surname}"
                tvPhone.text = item.phone

                itemContainer.setOnClickListener {
                    onClick(item)
                }
                itemContainer.setOnLongClickListener {
                    onLongClick(item)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(inflater, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun getFilter() = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val charSearch = charSequence.toString()
            filteredContactList = if (charSearch.isEmpty()) {
                contactList
            } else {
                val resultList = mutableListOf<Contact>()
                for (contact in contactList) {
                    if (contact.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))
                        || contact.surname.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                        resultList.add(contact)
                    }
                }
                resultList
            }
            val filterResults = FilterResults()
            filterResults.values = filteredContactList
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<Contact>)
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return super.convertResultToString(resultValue)
        }
    }
}