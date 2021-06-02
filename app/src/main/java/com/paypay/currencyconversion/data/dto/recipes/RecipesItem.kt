package com.paypay.currencyconversion.data.dto.recipes


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class RecipesItem(
    @Json(name = "currencies")
    var currencies: Map<String, String> = HashMap())


