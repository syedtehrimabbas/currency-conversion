package com.paypay.currencyconversion.usecase.errors

import com.paypay.currencyconversion.data.error.Error
import javax.inject.Inject


class ErrorManager @Inject constructor() : ErrorUseCase {
    override fun getError(errorCode: Int): Error {
        return Error()
    }
}
