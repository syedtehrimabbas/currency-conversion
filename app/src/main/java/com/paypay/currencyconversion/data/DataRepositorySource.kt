package com.paypay.currencyconversion.data

import com.paypay.currencyconversion.data.dto.currency.CurrenciesRatesResponse
import com.paypay.currencyconversion.data.dto.currency.CurrenciesResponse
import kotlinx.coroutines.flow.Flow


interface DataRepositorySource {
    suspend fun requestCurrencies(): Flow<Resource<CurrenciesResponse>>
    suspend fun requestCurrenciesRates(): Flow<Resource<CurrenciesRatesResponse>>
}
