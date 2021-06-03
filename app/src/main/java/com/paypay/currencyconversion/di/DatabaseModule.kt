package com.paypay.currencyconversion.di

import android.app.Application
import androidx.room.Room
import com.paypay.currencyconversion.BuildConfig
import com.paypay.currencyconversion.data.database.CurrencyConversionDatabase
import com.paypay.currencyconversion.data.database.dao.CurrencyConversionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideCurrencyDB(application: Application): CurrencyConversionDatabase {
        return Room.databaseBuilder(application, CurrencyConversionDatabase::class.java, BuildConfig.DB_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
    @Provides
    @Singleton
    fun provideCurrencyConversionDao (currencyConversionDatabase:CurrencyConversionDatabase): CurrencyConversionDao {
        return currencyConversionDatabase.currencyConversionDao
    }
}
