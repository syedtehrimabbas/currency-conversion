package com.paypay.currencyconversion.data.remote

import com.paypay.currencyconversion.BuildConfig.API_KEY
import com.paypay.currencyconversion.data.Resource
import com.paypay.currencyconversion.data.database.dao.CurrencyConversionDao
import com.paypay.currencyconversion.data.database.db_tables.Currency
import com.paypay.currencyconversion.data.dto.currency.CurrenciesRatesResponse
import com.paypay.currencyconversion.data.dto.currency.CurrenciesResponse
import com.paypay.currencyconversion.data.error.NETWORK_ERROR
import com.paypay.currencyconversion.data.error.NO_INTERNET_CONNECTION
import com.paypay.currencyconversion.data.remote.service.RecipesService
import com.paypay.currencyconversion.utils.NetworkConnectivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RemoteData @Inject
constructor(
    private val serviceGenerator: ServiceGenerator,
    private val networkConnectivity: NetworkConnectivity,
    private val currencyConversionDao: CurrencyConversionDao
) :
    RemoteDataSource {
    override suspend fun currencies(): Resource<CurrenciesResponse> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        val allCurrencies = currencyConversionDao.allCurrencies()
        return when {
            allCurrencies.isNotEmpty() -> {
                val map = allCurrencies.associateBy({ it.code }, { it.currencyName })
                Resource.Success(data = CurrenciesResponse(true, map as Map<String, String>))
            }
            else -> {
                when (val response =
                    processCall(recipesService.fetchCurrencies(API_KEY))) {
                    is CurrenciesResponse -> {
                        withContext(Dispatchers.IO) {
                            currencyConversionDao.insertCurrencies(response.currencies.map {
                                Currency(
                                    it.key,
                                    it.value
                                )
                            })
                        }

                        Resource.Success(data = response)
                    }
                    else -> {
                        Resource.DataError(errorCode = response as Int)
                    }
                }
            }
        }

        /* return when (val response =
             processCall(recipesService.fetchCurrencies(API_KEY))) {
             is CurrenciesResponse -> {
                 Resource.Success(data =response)
             }
             else -> {
                 Resource.DataError(errorCode = response as Int)
             }
         }*/
    }

    override suspend fun rates(): Resource<CurrenciesRatesResponse> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)

        return when (val response =
            processCall(recipesService.fetchRates(API_KEY))) {
            is CurrenciesRatesResponse -> {
                Resource.Success(data = response)
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
