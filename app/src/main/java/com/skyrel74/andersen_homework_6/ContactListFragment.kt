package com.skyrel74.andersen_homework_6

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skyrel74.andersen_homework_6.databinding.FragmentContactListBinding

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {

    private val binding by viewBinding(FragmentContactListBinding::bind)

    private lateinit var contactAdapter: ContactAdapter

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataset()

        with(binding.rvContacts) {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = ContactAdapter(dataset,
                { onClickContact(it) },
                { onLongClickContact(it) }
            ).also {
                contactAdapter = it
            }
            val dividerItemDecoration = ContactOffsetDrawer(context, 8, 8, 8, 8)
            dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_drawable, null))
            this.addItemDecoration(dividerItemDecoration)
        }

        contactAdapter.submitList(dataset)

        binding.svContacts.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                contactAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun setupDataset() {
        for (i in 1..150) {
            dataset.add(
                Contact(
                    id = i,
                    imageUrl = "https://picsum.photos/seed/$i/200/200",
                    name = (1..5)
                        .map { kotlin.random.Random.nextInt(0, charPool.size) }
                        .map(charPool::get)
                        .joinToString(""),
                    surname = (1..5)
                        .map { kotlin.random.Random.nextInt(0, charPool.size) }
                        .map(charPool::get)
                        .joinToString(""),
                    phone = (0..10)
                        .map { kotlin.random.Random.nextInt(0, 10) }
                        .joinToString("")
                )
            )
        }
    }

    private fun onClickContact(contact: Contact) {
        val containerId =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                R.id.container
            else
                R.id.details_container

        parentFragmentManager.beginTransaction()
            .replace(containerId, ContactDetailsFragment.newInstance(contact))
            .addToBackStack(ROOT_TAG)
            .commit()
    }

    private fun onLongClickContact(contact: Contact) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete click")
            .setMessage("Do you want to delete contact?")
            .setPositiveButton("Yes") { _, _ ->
                val index = dataset.indexOf(contact)
                dataset.removeAt(index)
                contactAdapter.notifyItemRemoved(index)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()

    }

    companion object {

        private val dataset = mutableListOf<Contact>()

        fun newInstance(contact: Contact): ContactListFragment {
            dataset.find{ it.id == contact.id }!!.apply {
                name = contact.name
                surname = contact.surname
                phone = contact.phone
            }
            return ContactListFragment()
        }
    }
}