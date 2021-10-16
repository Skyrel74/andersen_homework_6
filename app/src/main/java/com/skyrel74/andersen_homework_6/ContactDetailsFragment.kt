package com.skyrel74.andersen_homework_6

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.skyrel74.andersen_homework_6.databinding.FragmentContactDetailsBinding

const val CONTACT_TAG = "CONTACT_TAG"

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {

    private val binding by viewBinding(FragmentContactDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contact = arguments?.getSerializable(CONTACT_TAG) as Contact?

        if (contact != null) {
            binding.apply {
                Glide.with(this@ContactDetailsFragment)
                    .load(contact.imageUrl)
                    .centerCrop()
                    .into(ivContact)
                etName.setText(contact.name)
                etSurname.setText(contact.surname)
                etPhone.setText(contact.phone)
                btnSave.setOnClickListener {
                    val changedContact = Contact(contact.id,
                        contact.imageUrl,
                        etName.text.toString(),
                        etSurname.text.toString(),
                        etPhone.text.toString())
                    when(resources.configuration.orientation) {
                        Configuration.ORIENTATION_PORTRAIT -> {
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.container,
                                    ContactListFragment.newInstance(changedContact))
                                .commit()
                        }
                        Configuration.ORIENTATION_LANDSCAPE -> {
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.list_container,
                                    ContactListFragment.newInstance(changedContact))
                                .remove(this@ContactDetailsFragment)
                                .commit()
                        }
                    }
                }
            }
        }
    }

    companion object {

        fun newInstance(contact: Contact): ContactDetailsFragment {
            val args = Bundle()
            args.putSerializable(CONTACT_TAG, contact)

            val fragment = ContactDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}