package com.example.homework3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homework3.databinding.ContactItemBinding
import com.example.homework3.databinding.ContactItemEnabledBinding

class ContactAdapter : ListAdapter<ContactModel, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    var mod = STANDARD_MOD
    var onItemClickListener: ((ContactModel) -> Unit) = {}
    var onItemClickListenerSelected: ((ContactModel) -> Unit) = {}

    inner class ContactViewHolder(
        private val binding: ContactItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ContactModel) {
            with(binding) {
                tvId.text = item.id.toString()
                tvFirstName.text = item.firstName
                tvLastName.text = item.lastName
                tvPhoneNumber.text = item.phoneNumber
            }
        }
    }

    class ContactViewHolderEnabled(
        private val binding: ContactItemEnabledBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ContactModel) {
            with(binding) {
                tvId.text = item.id.toString()
                tvFirstName.text = item.firstName
                tvLastName.text = item.lastName
                tvPhoneNumber.text = item.phoneNumber
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_ENABLED -> ContactViewHolderEnabled(
                ContactItemEnabledBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_DISABLED -> ContactViewHolder(
                ContactItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw RuntimeException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (currentList[position].isEnabled) {
            holder as ContactViewHolderEnabled
            holder.bind(currentList[position])
        } else {
            holder as ContactViewHolder
            holder.bind(currentList[position])
        }

        holder.itemView.setOnClickListener {
            if (mod) {
                onItemClickListener.invoke(getItem(position))
            } else {
                onItemClickListenerSelected.invoke(getItem(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].isEnabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    companion object {

        private const val VIEW_TYPE_ENABLED = 1
        private const val VIEW_TYPE_DISABLED = 2

        const val STANDARD_MOD = true
        const val DELETE_MOD = false

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ContactModel>() {

            override fun areItemsTheSame(
                oldItem: ContactModel,
                newItem: ContactModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ContactModel,
                newItem: ContactModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}