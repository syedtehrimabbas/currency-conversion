package com.paypay.currencyconversion.data

import com.paypay.currencyconversion.data.dto.recipes.Currencies
import com.paypay.currencyconversion.data.local.LocalData
import com.paypay.currencyconversion.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class DataRepository @Inject constructor(private val remoteRepository: RemoteData, private val localRepository: LocalData, private val ioDispatcher: CoroutineContext) :
    DataRepositorySource {

    override suspend fun requestRecipes(): Flow<Resource<Currencies>> {
        return flow {
            emit(remoteRepository.requestRecipes())
        }.flowOn(ioDispatcher)
    }

}
