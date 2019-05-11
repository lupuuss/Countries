package com.github.lupuuss.countries.ui.modules.main

import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.toSpanned
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.github.lupuuss.countries.R
import com.github.lupuuss.countries.boldQuery
import com.github.lupuuss.countries.htmlToSpanned
import com.github.lupuuss.countries.model.dataclass.ShortCountry
import java.text.Collator

private class ShortCountryQuery(
    shortCountry: ShortCountry,
    val displayText: Spanned =  shortCountry.name.toSpanned()
) : ShortCountry(shortCountry.name, shortCountry.alpha3Code)

class FilteredCountriesAdapter : RecyclerView.Adapter<FilteredCountriesAdapter.ViewHolder>() {

    interface OnCountryClickListener {
        fun onCountryClick(view: View, name: String, position: Int)
    }

    var onCountryClickListener: OnCountryClickListener? = null

    class ViewHolder(val countryNameText: TextView) : RecyclerView.ViewHolder(countryNameText)

    private val collator = Collator.getInstance()

    private var countriesList: List<ShortCountry>? = null

    private val dataSet: SortedList<ShortCountryQuery> = SortedList(
        ShortCountryQuery::class.java,
        object : SortedList.Callback<ShortCountryQuery>() {
            override fun areItemsTheSame(item1: ShortCountryQuery?, item2: ShortCountryQuery?): Boolean {

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

            override fun compare(o1: ShortCountryQuery, o2: ShortCountryQuery): Int {
                return collator.compare(o1.name, o2.name)
            }

            override fun areContentsTheSame(oldItem: ShortCountryQuery, newItem: ShortCountryQuery): Boolean {
                return oldItem.displayText == newItem.displayText
            }

    })

    fun setCountries(countries: List<ShortCountry>) {

        dataSet.clear()
        dataSet.addAll(countries.map { ShortCountryQuery(it) })
        countriesList = countries
    }

    fun filter(query: String) {

        if (query == "") {
            dataSet.clear()
            dataSet.addAll(countriesList?.map { ShortCountryQuery(it) } ?: emptyList())
        } else {

            countriesList?.let { notNullList ->

                val filtered = notNullList.filter {
                    it.name.toLowerCase().contains(query.toLowerCase())
                }.toList()

                dataSet.replaceAll(filtered.map { ShortCountryQuery(it, it.name.boldQuery(query).htmlToSpanned()) })
            }
        }
    }

    fun clearCountries() {
        countriesList = null
        dataSet.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.countries_list_item, parent, false)

        return ViewHolder(view as TextView)
    }

    override fun getItemCount() = dataSet.size()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.countryNameText.text = dataSet[position].displayText
        holder.countryNameText.setOnClickListener {

            onCountryClickListener?.onCountryClick(it, dataSet[position].name, position)
        }
    }
}