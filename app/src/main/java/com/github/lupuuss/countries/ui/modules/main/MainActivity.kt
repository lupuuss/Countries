package com.github.lupuuss.countries.ui.modules.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.lupuuss.countries.R
import com.github.lupuuss.countries.model.dataclass.ShortCountry
import com.github.lupuuss.countries.model.dataclass.ErrorMessage
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView, View.OnClickListener {

    @Inject
    lateinit var presenter: MainPresenter

    private var countriesAdapter: CountriesAdapter = CountriesAdapter()

    override var isProgressBarVisible: Boolean = true
        get() = countriesProgressBar?.isVisible ?: field
        set(value) {
            field = value
            countriesProgressBar?.isVisible = value
        }

    override var isErrorMessageVisible: Boolean = false
        get() = errorMessageTextView?.isVisible ?: field
        set(value) {
            field = value
            errorMessageTextView?.isVisible = value
            refreshButton?.isVisible = value
        }

    override fun onClick(p0: View?) {

        when (p0!!.id) {
            R.id.refreshButton -> presenter.onClickRefreshButton()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.attachView(this)
        countriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = countriesAdapter
        }

        refreshButton.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun displayCountriesList(countries: List<ShortCountry>) {
        countriesAdapter.setDataAndNotify(countries)
    }

    override fun clearCountriesList() {

        countriesAdapter.clearAndNotify()
    }

    override fun showErrorMsg(errorMsg: ErrorMessage) {

        errorMessageTextView.text = when (errorMsg) {
            ErrorMessage.NO_INTERNET_CONNECTION ->  getString(R.string.noInternetConnection)
            ErrorMessage.UNKNOWN -> getString(R.string.somethingGoesWrong)
        }
    }

    override fun postString(msg: String) {

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
