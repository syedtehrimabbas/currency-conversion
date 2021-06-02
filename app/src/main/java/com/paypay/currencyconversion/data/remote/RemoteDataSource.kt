package com.paypay.currencyconversion.data.remote

import com.paypay.currencyconversion.data.Resource
import com.paypay.currencyconversion.data.dto.recipes.Currencies

internal interface RemoteDataSource {
    suspend fun requestRecipes(): Resource<Currencies>
}
