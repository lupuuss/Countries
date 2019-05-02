package com.github.lupuuss.countries.ui.modules.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.github.lupuuss.countries.databinding.ActivityMainCountriesItemBinding
import com.github.lupuuss.countries.model.dataclass.ShortCountry
import java.text.Collator

class FilteredCountriesAdapter : RecyclerView.Adapter<FilteredCountriesAdapter.ViewHolder>() {


    class ViewHolder(private val binding: ActivityMainCountriesItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shortCountry: ShortCountry) {

            binding.country = shortCountry
        }
    }

    private val collator = Collator.getInstance()

    private var countriesList: List<ShortCountry>? = null

    private val dataSet: SortedList<ShortCountry> = SortedList(ShortCountry::class.java, object : SortedList.Callback<ShortCountry>() {

        override fun areItemsTheSame(item1: ShortCountry?, item2: ShortCountry?): Boolean {

            return item1 == item2
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            this@FilteredCountriesAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int) {
            this@FilteredCountriesAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onInserted(position: Int, count: Int) {
            this@FilteredCountriesAdapter.notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            this@FilteredCountriesAdapter.notifyItemRangeRemoved(position, count)
        }

        override fun compare(o1: ShortCountry, o2: ShortCountry): Int {
            return collator.compare(o1.name, o2.name)
        }

        override fun areContentsTheSame(oldItem: ShortCountry, newItem: ShortCountry): Boolean {
            return oldItem.name == newItem.name
        }

    })

    fun setCountries(countries: List<ShortCountry>) {

        dataSet.clear()
        dataSet.addAll(countries)
        countriesList = countries
    }

    fun filter(query: String) {

        if (query == "") {
            dataSet.clear()
            dataSet.addAll(countriesList ?: emptyList())
        } else {

            countriesList?.let { notNullList ->

                val filtered = notNullList.filter {
                    it.name.toLowerCase().contains(query)
                }.toList()

                dataSet.replaceAll(filtered)
            }

        }
    }

    fun clearCountries() {
        countriesList = null
        dataSet.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ActivityMainCountriesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount() = dataSet.size()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(dataSet[position])
    }
}