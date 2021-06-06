package com.paypay.currencyconversion.data.remote

import com.paypay.currencyconversion.BuildConfig.API_KEY
import com.paypay.currencyconversion.data.Resource
import com.paypay.currencyconversion.data.database.AppPreferences
import com.paypay.currencyconversion.data.database.dao.CurrencyConversionDao
import com.paypay.currencyconversion.data.dto.currency.CurrenciesRatesResponse
import com.paypay.currencyconversion.data.dto.currency.CurrenciesResponse
import com.paypay.currencyconversion.data.error.NETWORK_ERROR
import com.paypay.currencyconversion.data.error.NO_INTERNET_CONNECTION
import com.paypay.currencyconversion.data.remote.service.CurrencyLayerService
import com.paypay.currencyconversion.utils.NetworkConnectivity
import com.paypay.currencyconversion.utils.toCurrencyList
import com.paypay.currencyconversion.utils.toRatesList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RemoteData @Inject
constructor(
    private val retrofitService: RetrofitService,
    private val networkConnectivity: NetworkConnectivity,
    private val currencyConversionDao: CurrencyConversionDao,
    private val preferences: AppPreferences
) : RemoteDataSource {
    override suspend fun currencies(): Resource<CurrenciesResponse> {
        val currenciesService = retrofitService.createService(CurrencyLayerService::class.java)
        val allCurrencies = currencyConversionDao.allCurrencies()
        return when {
            allCurrencies.isNotEmpty() -> {
                val map = allCurrencies.associateBy({ it.code }, { it.currencyName })
                Resource.Success(data = CurrenciesResponse(true, (map as Map<*, *>)))
            }
            else -> {

                when {
                    networkConnectivity.isConnected() -> {
                        val response =
                            processCall(currenciesService.fetchCurrencies(API_KEY))
                        when (response) {
                            is CurrenciesResponse -> {
                                when (response.success) {
                                    true -> {
                                        withContext(Dispatchers.IO) {
                                            val currenciesList =
                                                response.currencies.toCurrencyList()
                                            currencyConversionDao.insertCurrencies(currenciesList)
                                        }
                                        Resource.Success(data = response)
                                    }
                                    else -> {
                                        Resource.DataError(
                                            response.error?.code,
                                            response.error?.info
                                        )
                                    }
                                }
                            }
                            else -> {
                                Resource.DataError(response as Int)
                            }
                        }
                    }
                    else -> {
                        Resource.DataError(
                            NO_INTERNET_CONNECTION,
                            "Please check your internet connection"
                        )
                    }
                }
            }
        }
    }

    override suspend fun rates(): Resource<CurrenciesRatesResponse> {
        val rates = currencyConversionDao.allCurrencyRates()
        return when {
            (rates.isNotEmpty() && !preferences.liveDataRequired()) -> {
                val map = rates.associateBy({ it.currencyCode }, { it.rate })
                Resource.Success(
                    data = CurrenciesRatesResponse(
                        true,
                        System.currentTimeMillis(),
                        map
                    )
                )
            }
            else -> {
                return ratesAPI()
            }
        }

    }

    override suspend fun ratesAPI(): Resource<CurrenciesRatesResponse> {
        val ratesService = retrofitService.createService(CurrencyLayerService::class.java)
        return when {
            networkConnectivity.isConnected() -> {
                val response = processCall(ratesService.fetchRates(API_KEY))
                return when (response) {
                    is CurrenciesRatesResponse -> {
                        when (response.success) {
                            true -> {
                                withContext(Dispatchers.IO) {
                                    val rates = response.quotes.toRatesList()
                                    currencyConversionDao.insertRates(rates)
                                    preferences.setPreferences(
                                        AppPreferences.SYNC_TIME,
                                        System.currentTimeMillis()
                                    )
                                }
                                Resource.Success(data = response)

                            }
                            else -> {
                                Resource.DataError(response.error?.code, response.error?.info)
                            }
                        }
                    }
                    else -> {
                        Resource.DataError(response as Int)
                    }
                }

            }
            else -> {
                Resource.DataError(
                    NO_INTERNET_CONNECTION,
                    "Please check your internet connection"
                )
            }


        }
    }

    private fun processCall(responseCall: Response<*>): Any? {
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
