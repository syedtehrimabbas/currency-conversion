package com.paypay.currencyconversion.usecase.errors

import com.paypay.currencyconversion.data.error.mapper.ErrorMapper
import com.paypay.currencyconversion.data.error.Error
import javax.inject.Inject


class ErrorManager @Inject constructor(private val errorMapper: ErrorMapper) : ErrorUseCase {
    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }
}
