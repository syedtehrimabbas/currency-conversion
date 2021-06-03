package com.paypay.currencyconversion.data

import com.paypay.currencyconversion.data.dto.currency.CurrenciesRatesResponse
import com.paypay.currencyconversion.data.dto.currency.CurrenciesResponse
import com.paypay.currencyconversion.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class DataRepository @Inject constructor(
    private val remoteRepository: RemoteData,
    private val ioDispatcher: CoroutineContext
) :
    DataRepositorySource {

    override suspend fun requestCurrencies(): Flow<Resource<CurrenciesResponse>> {
        return flow {
            emit(remoteRepository.currencies())
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestCurrenciesRates(): Flow<Resource<CurrenciesRatesResponse>> {
        return flow {
            emit(remoteRepository.rates())
        }.flowOn(ioDispatcher)
    }

}
