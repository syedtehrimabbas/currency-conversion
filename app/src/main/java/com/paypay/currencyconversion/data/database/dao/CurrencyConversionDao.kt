package com.paypay.currencyconversion.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paypay.currencyconversion.data.database.db_tables.Currency
import com.paypay.currencyconversion.data.database.db_tables.CurrencyRate
import com.paypay.currencyconversion.data.database.db_tables.TableNames.CURRENCY
import com.paypay.currencyconversion.data.database.db_tables.TableNames.CURRENCY_RATES

@Dao
interface CurrencyConversionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(currencyRates: List<CurrencyRate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<Currency>)

    @Query("SELECT * FROM $CURRENCY")
    fun allCurrencies(): List<Currency>

    @Query("SELECT * FROM $CURRENCY_RATES")
    fun allCurrencyRates(): List<CurrencyRate>
}