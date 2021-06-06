package com.paypay.currencyconversion.utils

import com.paypay.currencyconversion.data.database.db_tables.Currency
import com.paypay.currencyconversion.data.database.db_tables.CurrencyRate

fun Map<*, *>.toCurrencyList() = map {
    Currency(
        it.key.toString(),
        it.value.toString()
    )
}

fun Map<String, Double>.toRatesList() = map {
    CurrencyRate(
        it.key,
        it.value
    )
}