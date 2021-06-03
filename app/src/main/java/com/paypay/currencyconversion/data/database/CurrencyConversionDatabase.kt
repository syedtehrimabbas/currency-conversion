package com.paypay.currencyconversion.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paypay.currencyconversion.BuildConfig
import com.paypay.currencyconversion.data.database.dao.CurrencyConversionDao
import com.paypay.currencyconversion.data.database.db_tables.Currency
import com.paypay.currencyconversion.data.database.db_tables.CurrencyRate

@Database(
    entities = [Currency::class, CurrencyRate::class],
    version = BuildConfig.DB_VERION, exportSchema = false
)
abstract class CurrencyConversionDatabase : RoomDatabase() {
    abstract val currencyConversionDao: CurrencyConversionDao
}