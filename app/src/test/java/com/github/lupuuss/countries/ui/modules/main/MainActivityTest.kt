package com.github.lupuuss.countries.ui.modules.main

import android.content.ComponentName
import android.widget.Button
import android.widget.TextView
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.lupuuss.countries.R
import com.github.lupuuss.countries.TestCountriesApp
import com.github.lupuuss.countries.kotlin.AntiSpam
import com.github.lupuuss.countries.model.dataclass.ShortCountry
import com.github.lupuuss.countries.ui.modules.details.DetailsActivity
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestCountriesApp::class)
class MainActivityTest {


    private val activityController = Robolectric.buildActivity(MainActivity::class.java)

    private val activity: MainActivity = activityController.setup().get()


    @Test
    fun onClick_shouldCallRefreshCountriesListOnPresenterThroughAntiSpam() {


        activity.onClick(mock { on { id }.then { R.id.refreshButtonView }})
        activity.onClick(mock { on { id }.then { 0 }})

        verify(activity.antiSpam, times(1))
            .doAction(eq("MainActivity.onClickRefreshButton"), eq(AntiSpam.STANDARD_DELTA), any())

        verify(activity.presenter, times(1)).refreshCountriesList()
    }

    @Test
    fun onCreate_shouldAttachPresenter() {

        verify(activity.presenter, times(1)).attachView(activity)
    }

    @Test
    fun onCreate_shouldSetRecyclerView() {

        val recyclerView = activity.findViewById<RecyclerView>(R.id.countriesRecyclerView)

        Assert.assertTrue(recyclerView.layoutManager is LinearLayoutManager)
        Assert.assertTrue(recyclerView.adapter is FilteredCountriesAdapter)
        Assert.assertEquals(activity, (recyclerView.adapter as FilteredCountriesAdapter).onCountryClickListener)
    }

    @Test
    fun onCreate_shouldRefreshButtonListener() {

        val button = activity.findViewById<Button>(R.id.refreshButtonView)

        Assert.assertEquals(activity, Shadows.shadowOf(button).onClickListener)
    }

    @Test
    fun onCreate_shouldSetIsErrorMessageVisibleToFalse() {

        Assert.assertEquals(false, activity.isErrorMessageVisible)
    }

    @Test
    fun onDestroy_shouldDetachView() {

        activityController.destroy()

        verify(activity.presenter, times(1)).detachView()
    }

    @Test
    fun onQueryTextSubmit_shouldReturnFalse() {

        Assert.assertEquals(false, activity.onQueryTextSubmit(""))
    }

    @Test
    fun onQueryTextChange_shouldCallFilterCountriesListOnPresenter() {

        activity.onQueryTextChange("query")

        verify(activity.presenter, times(1)).filterCountriesList("query")
    }

    @Test
    fun displayCountriesList_shouldDisplayCountriesInRecyclerView() {

        val countries = listOf(
            ShortCountry("Germany", "GER"),
            ShortCountry("Poland", "POL")
        )

        activity.displayCountriesList(countries)

        val recycler = activity.findViewById<RecyclerView>(R.id.countriesRecyclerView)

        Assert.assertEquals(2, recycler!!.adapter!!.itemCount)

        recycler.forEachIndexed { index, view ->

            Assert.assertEquals(countries[index].name, (view as TextView).text.toString())
        }
    }

    @Test
    fun clearCountriesList_shouldClearRecyclerView() {


        val countries = listOf(
            ShortCountry("Germany", "GER"),
            ShortCountry("Poland", "POL")
        )

        val recycler = activity.findViewById<RecyclerView>(R.id.countriesRecyclerView)

        activity.displayCountriesList(countries)
        activity.clearCountriesList()

        Assert.assertEquals(0, recycler!!.adapter!!.itemCount)
    }

    @Test
    fun onCountryClick_shouldCallNavigateToCountryDetailsOnPresenterThroughAntiSpam() {

        activity.onCountryClick(mock {}, "Poland", 0)

        verify(activity.antiSpam, times(1))
            .doAction(eq("MainActivity.onCountryClick"), eq(AntiSpam.STANDARD_DELTA), any())

        verify(activity.presenter, times(1)).navigateToCountryDetails("Poland")
    }

    @Test
    fun navigateToCountryDetails() {

        activity.navigateToCountryDetails("Poland")

        val intent = Shadows.shadowOf(activity).nextStartedActivity

        Assert.assertEquals("Poland", intent!!.extras!!.getString(DetailsActivity.COUNTRY_NAME))
        Assert.assertEquals(intent.component, ComponentName(activity, DetailsActivity::class.java))
    }
}