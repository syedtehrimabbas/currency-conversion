package com.paypay.currencyconversion.data

import com.paypay.currencyconversion.data.dto.recipes.Currencies
import kotlinx.coroutines.flow.Flow


interface DataRepositorySource {
    suspend fun requestRecipes(): Flow<Resource<Currencies>>
}
