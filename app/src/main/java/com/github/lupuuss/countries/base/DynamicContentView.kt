package com.github.lupuuss.countries.base

import com.github.lupuuss.countries.model.dataclass.ErrorMessage

interface DynamicContentView : BaseView {

    var isProgressBarVisible: Boolean
    var isErrorMessageVisible: Boolean
    var isContentVisible: Boolean
    fun setErrorMsg(errorMsg: ErrorMessage)
}