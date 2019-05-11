package com.github.lupuuss.countries.base

import com.github.lupuuss.countries.R
import com.github.lupuuss.countries.TestCountriesApp
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestCountriesApp::class)
class BaseActivityTest {

    private class BaseActivityImpl : BaseActivity() {

        var exposedToast
            get() = toast
            set(value) {
                toast = value
            }
    }

    private val controller = Robolectric.buildActivity(BaseActivityImpl::class.java)

    private val activity = controller.setup().get()

    @Before
    fun setUp() {
        activity.exposedToast = spy(activity.exposedToast)
    }

    @Test
    fun postMessage_shouldToastStringFromResources() {


        activity.postMessage(BaseView.Message.GOOGLE_MAPS_UNAVAILABLE)

        verify(activity.exposedToast, times(1)).setText(R.string.error_google_maps_unavailable)
        verify(activity.exposedToast, times(1)).show()
    }

    @Test
    fun postString_shouldToastString() {

        activity.postString("Any string")

        verify(activity.exposedToast, times(1)).setText("Any string")
        verify(activity.exposedToast, times(1)).show()
    }
}