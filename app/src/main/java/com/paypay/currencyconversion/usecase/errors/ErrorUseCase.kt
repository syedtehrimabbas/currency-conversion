package com.paypay.currencyconversion.usecase.errors

import com.paypay.currencyconversion.data.error.Error

interface ErrorUseCase {
    fun getError(errorCode: Int): Error
}
