package com.paypay.currencyconversion.data.dto.currency

import com.google.gson.annotations.SerializedName

data class CurrenciesRatesResponse(
    @SerializedName("success")
    var success: Boolean,
    @SerializedName("timestamp")
    var timestamp: Long,
    @SerializedName("quotes")
    var quotes: Map<String, Double> = HashMap(),
    @SerializedName("error")
    var error: Error? = null
)