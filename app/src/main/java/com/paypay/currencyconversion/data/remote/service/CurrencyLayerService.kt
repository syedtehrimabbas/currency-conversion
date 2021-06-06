package com.paypay.currencyconversion.data.remote.service

import com.paypay.currencyconversion.data.dto.currency.CurrenciesRatesResponse
import com.paypay.currencyconversion.data.dto.currency.CurrenciesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyLayerService {
    @GET("list")
    suspend fun fetchCurrencies(
        @Query("access_key") accessKey: String
    ): Response<CurrenciesResponse>


    @GET("live")
    suspend fun fetchRates(
        @Query("access_key") accessKey: String
    ): Response<CurrenciesRatesResponse>
}
