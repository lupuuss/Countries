package com.github.lupuuss.countries.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.lupuuss.countries.R

abstract class BaseActivity : AppCompatActivity(), BaseView {

    private lateinit var toast: Toast

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toast = Toast.makeText(this, "", Toast.LENGTH_LONG)
    }

    override fun postMessage(message: BaseView.Message) {

        when (message) {

            BaseView.Message.GOOGLE_MAPS_UNAVAILABLE -> toast.setText(R.string.error_google_maps_unavailable)
        }

        toast.show()
    }

    override fun postString(msg: String) {

        toast.setText(msg)
        toast.show()
    }
}