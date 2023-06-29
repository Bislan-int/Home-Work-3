package com.example.homework3

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.homework3.databinding.CountryItemBinding

class CountryAdapter(listCountry: List<CountryModel>) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(
        private val binding: CountryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: CountryModel) {
            with(binding) {
                tvId.text = "Nᵒ ${item.id}:"
                tvCountryName.text = item.countryName
                imFlag.setImageResource(item.countryFlag)
            }
        }

        companion object {
            fun createViewHolder(parent: ViewGroup) = CountryViewHolder(
                CountryItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(listCountry[position])
    }

    override fun getItemCount() = listCountry.size
}