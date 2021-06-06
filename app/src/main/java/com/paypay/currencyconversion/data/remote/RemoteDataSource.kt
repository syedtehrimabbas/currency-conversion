package com.paypay.currencyconversion.data.remote

import com.paypay.currencyconversion.data.Resource
import com.paypay.currencyconversion.data.dto.currency.CurrenciesRatesResponse
import com.paypay.currencyconversion.data.dto.currency.CurrenciesResponse

internal interface RemoteDataSource {
    suspend fun currencies(): Resource<CurrenciesResponse>
    suspend fun rates(): Resource<CurrenciesRatesResponse>
    suspend fun ratesAPI(): Resource<CurrenciesRatesResponse>
}
