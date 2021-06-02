package com.paypay.currencyconversion.data.remote

import com.paypay.currencyconversion.data.Resource
import com.paypay.currencyconversion.data.dto.recipes.Currencies
import com.paypay.currencyconversion.data.dto.recipes.RecipesItem
import com.paypay.currencyconversion.data.error.NETWORK_ERROR
import com.paypay.currencyconversion.data.error.NO_INTERNET_CONNECTION
import com.paypay.currencyconversion.data.remote.service.RecipesService
import com.paypay.currencyconversion.utils.NetworkConnectivity
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


class RemoteData @Inject
constructor(
    private val serviceGenerator: ServiceGenerator,
    private val networkConnectivity: NetworkConnectivity
) :
    RemoteDataSource {
    override suspend fun requestRecipes(): Resource<Currencies> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response =
            processCall(recipesService.fetchCurrencies("72ebfa84bb9e44ee2fe0416c27300552"))) {
            is RecipesItem -> {
                Resource.Success(data = Currencies(response))
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    private fun processCall(responseCall: Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            return NO_INTERNET_CONNECTION
        }
        return try {
            val responseCode = responseCall.code()
            if (responseCall.isSuccessful) {
                responseCall.body()
            } else {
                responseCode
            }
        } catch (e: IOException) {
            NETWORK_ERROR
        }
    }
}
