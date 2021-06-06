package com.paypay.currencyconversion.di

import com.paypay.currencyconversion.adapters.CurrencyRatesAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {

    @Provides
    @Singleton
    fun provideCurrencyAdapter(): CurrencyRatesAdapter {
        return CurrencyRatesAdapter(mutableListOf())
    }

}