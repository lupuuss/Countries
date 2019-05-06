package com.github.lupuuss.countries.base

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.github.lupuuss.countries.R
import com.github.lupuuss.countries.model.dataclass.ErrorMessage
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

abstract class DynamicContentActivity : AppCompatActivity(), DynamicContentView {

    protected abstract val errorTextView: TextView?
    protected abstract val progressBar: ProgressBar?
    protected abstract val content: View?
    protected abstract val refreshButton: Button?

    override var isProgressBarVisible: Boolean = true
        get() = countriesProgressBar?.isVisible ?: field
        set(value) {
            field = value
            progressBar?.isVisible = value
        }

    override var isErrorMessageVisible: Boolean = false
        get() = errorMessageTextView?.isVisible ?: field
        set(value) {
            field = value
            errorTextView?.isVisible = value
            refreshButton?.isVisible = value
        }

    override var isContentVisible: Boolean = false
        get() = true
        set(value) {
            field = value
            content?.isVisible = value
        }

    override fun showErrorMsg(errorMsg: ErrorMessage) {

        errorTextView?.text = when (errorMsg) {
            ErrorMessage.NO_INTERNET_CONNECTION ->  getString(R.string.noInternetConnection)
            ErrorMessage.UNKNOWN -> getString(R.string.somethingGoesWrong)
        }
    }
}