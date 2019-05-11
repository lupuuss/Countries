package com.github.lupuuss.countries.base

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.github.lupuuss.countries.R
import com.github.lupuuss.countries.model.dataclass.ErrorMessage

abstract class DynamicContentActivity : BaseActivity(), DynamicContentView {

    protected abstract val errorTextView: TextView?
    protected abstract val progressBar: ProgressBar?
    protected abstract val content: View?
    protected abstract val refreshButton: Button?

    override var isProgressBarVisible: Boolean = true
        get() = progressBar?.isVisible ?: field
        set(value) {
            field = value
            progressBar?.isVisible = value
        }

    override var isErrorMessageVisible: Boolean = false
        get() = errorTextView?.isVisible ?: field
        set(value) {
            field = value
            errorTextView?.isVisible = value
            refreshButton?.isVisible = value
        }

    override var isContentVisible: Boolean = false
        get() = content?.isVisible ?: field
        set(value) {
            field = value
            content?.isVisible = value
        }

    override fun showErrorMsg(errorMsg: ErrorMessage) {

        errorTextView?.setText(when (errorMsg) {
            ErrorMessage.NO_INTERNET_CONNECTION ->  R.string.no_internet_connection
            ErrorMessage.UNKNOWN -> R.string.something_goes_wrong
            ErrorMessage.COUNTRY_NOT_FOUND -> R.string.country_not_found
        })
    }
}