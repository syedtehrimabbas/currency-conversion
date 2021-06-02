package com.paypay.currencyconversion.ui.base

import androidx.lifecycle.ViewModel
import com.paypay.currencyconversion.usecase.errors.ErrorManager
import javax.inject.Inject



abstract class BaseViewModel : ViewModel() {
    /**Inject Singleton ErrorManager
     * Use this errorManager to get the Errors
     */
    @Inject
    lateinit var errorManager: ErrorManager
}
