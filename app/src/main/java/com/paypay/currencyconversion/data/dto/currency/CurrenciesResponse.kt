package com.paypay.currencyconversion.data.dto.currency

import com.google.gson.annotations.SerializedName

data class CurrenciesResponse(
    @SerializedName("success")
    var success: Boolean,
    @SerializedName("currencies")
    var currencies: Map<*, *>,
    @SerializedName("error")
    var error: Error? = null
)