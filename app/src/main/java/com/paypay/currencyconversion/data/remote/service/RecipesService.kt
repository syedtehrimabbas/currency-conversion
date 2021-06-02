package com.paypay.currencyconversion.data.remote.service

import com.paypay.currencyconversion.data.dto.recipes.RecipesItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesService {
    @GET("list")
    suspend fun fetchCurrencies(
        @Query("access_key") accessKey: String
    ): Response<RecipesItem>
}
