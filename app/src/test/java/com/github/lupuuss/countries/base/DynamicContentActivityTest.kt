package com.github.lupuuss.countries.base

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.github.lupuuss.countries.R
import com.github.lupuuss.countries.TestCountriesApp
import com.github.lupuuss.countries.model.dataclass.ErrorMessage
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestCountriesApp::class)
class DynamicContentActivityTest {

    private class DynamicActivity : DynamicContentActivity() {

        public override val errorTextView: TextView? = mock {}
        public override val progressBar: ProgressBar? = mock {}
        public override val content: View? = mock {}
        public override val refreshButton: Button? = mock {}
    }

    private val controller = Robolectric.buildActivity(DynamicActivity::class.java)

    private val activity = controller.setup().get()

    @Test
    fun isProgressBarVisible_shouldChangeProgressBarVisibility() {

        activity.isProgressBarVisible = false

        verify(activity.progressBar, times(1))!!.isVisible = false

        activity.isProgressBarVisible = true

        verify(activity.progressBar, times(1))!!.isVisible = true
    }

    @Test
    fun isProgressBarVisible_shouldReturnProgressBarVisibility() {
        activity.isProgressBarVisible

        verify(activity.progressBar, times(1))!!.isVisible
    }

    @Test
    fun isErrorMessageVisible_shouldChangeErrorTextAndRefreshButtonVisibility() {
        activity.isErrorMessageVisible = false

        verify(activity.errorTextView, times(1))!!.isVisible = false
        verify(activity.refreshButton, times(1))!!.isVisible = false

        activity.isErrorMessageVisible = true

        verify(activity.errorTextView, times(1))!!.isVisible = true
        verify(activity.refreshButton, times(1))!!.isVisible = true
    }

    @Test
    fun isErrorMessageVisible_shouldReturnErrorMessageTextVisibility() {

        activity.isErrorMessageVisible

        verify(activity.errorTextView, times(1))!!.isVisible
    }

    @Test
    fun isContentVisible_shouldChangeContentVisibility() {

        activity.isContentVisible = false

        verify(activity.content, times(1))!!.isVisible = false

        activity.isContentVisible = true

        verify(activity.content, times(1))!!.isVisible = true
    }

    @Test
    fun isContentVisible_shouldReturnContentViewVisibility() {

        activity.isContentVisible

        verify(activity.content, times(1))!!.isVisible
    }

    @Test
    fun setErrorMsg_shouldSetProperValuesFromErrorMessageEnum() {

        activity.setErrorMsg(ErrorMessage.NO_INTERNET_CONNECTION)

        verify(activity.errorTextView, times(1))!!.setText(R.string.error_no_internet_connection)

        activity.setErrorMsg(ErrorMessage.UNKNOWN)

        verify(activity.errorTextView, times(1))!!.setText(R.string.error_unknown)

        activity.setErrorMsg(ErrorMessage.COUNTRY_NOT_FOUND)

        verify(activity.errorTextView, times(1))!!.setText(R.string.error_country_not_found)
    }
}