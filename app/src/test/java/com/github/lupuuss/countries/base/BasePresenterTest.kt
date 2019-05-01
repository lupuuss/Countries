package com.github.lupuuss.countries.base

import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.junit.Assert.*

class BasePresenterTest {

    class TestPresenterImpl : BasePresenter<BaseView>() {
        fun exposeView() = view
    }

    private val presenter: TestPresenterImpl = TestPresenterImpl()

    @Test
    fun shouldBeNullAtStart() {

        assertEquals("View should be null at start!", null, presenter.exposeView())
    }

    @Test
    fun shouldAttachView() {
        val exampleView: BaseView = mock { }
        presenter.attachView(exampleView)
        assertEquals("View should be attached!", presenter.exposeView(), exampleView)
    }

    @Test
    fun detachView_shouldSetViewNull() {
        val exampleView: BaseView = mock { }
        presenter.attachView(exampleView)
        presenter.detachView()
        assertEquals("View should be null after detach!", presenter.exposeView(), null)
    }
}