package com.github.lupuuss.countries.ui.modules.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.lupuuss.countries.R
import com.github.lupuuss.countries.base.DynamicContentActivity
import com.github.lupuuss.countries.kotlin.AntiSpam
import com.github.lupuuss.countries.model.dataclass.ShortCountry
import com.github.lupuuss.countries.ui.modules.details.DetailsActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DynamicContentActivity(), MainView, View.OnClickListener, SearchView.OnQueryTextListener,
    FilteredCountriesAdapter.OnCountryClickListener {

    @Inject
    lateinit var presenter: MainPresenter

    @Inject
    lateinit var antiSpam: AntiSpam

    private var countriesAdapter: FilteredCountriesAdapter = FilteredCountriesAdapter()

    override val errorTextView: TextView?
        get() = errorMessageTextView

    override val progressBar: ProgressBar?
        get() = countriesProgressBar

    override val refreshButton: Button?
        get() = refreshButtonView

    override val content: View?
        get() = countriesRecyclerView

    override fun onClick(p0: View?) {

        when (p0!!.id) {
            R.id.refreshButtonView -> {

                antiSpam.doAction("MainActivity.onClickRefreshButton", AntiSpam.STANDARD_DELTA) {
                    presenter.refreshCountriesList()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = countriesAdapter
        }


        isErrorMessageVisible = false
        refreshButton?.setOnClickListener(this)

        countriesSearchView.setOnQueryTextListener(this)
        countriesAdapter.onCountryClickListener = this
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        presenter.filterCountriesList(newText)
        return true
    }

    override fun displayCountriesList(countries: List<ShortCountry>) {
        countriesAdapter.setCountries(countries)
    }

    override fun filterCountriesList(query: String) {
        countriesAdapter.filter(query)
        countriesRecyclerView.scrollToPosition(0)
    }

    override fun clearCountriesList() {
        countriesAdapter.clearCountries()
    }


    override fun onCountryClick(view: View, name: String, position: Int) {

        antiSpam.doAction("MainActivity.onCountryClick", AntiSpam.STANDARD_DELTA) {
            presenter.navigateToCountryDetails(name)
        }
    }

    override fun navigateToCountryDetails(name: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.COUNTRY_NAME, name)
        startActivity(intent)
    }
}
