package com.github.lupuuss.countries.ui.modules.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.lupuuss.countries.R
import com.github.lupuuss.countries.model.dataclass.ShortCountry

class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {

    private var dataSet: List<ShortCountry>? = null

    class ViewHolder(layout: ViewGroup) : RecyclerView.ViewHolder(layout) {
        val countryName: TextView = layout.findViewById(R.id.countryNameTextView)
    }

    fun clearAndNotify() {
        dataSet = null
        notifyDataSetChanged()
    }

    fun setDataAndNotify(dataSet: List<ShortCountry>) {

        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_main_countries_item, parent, false)
                as ViewGroup

        return ViewHolder(view)
    }

    override fun getItemCount() = dataSet?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.countryName.text = dataSet!![position].name
    }
}