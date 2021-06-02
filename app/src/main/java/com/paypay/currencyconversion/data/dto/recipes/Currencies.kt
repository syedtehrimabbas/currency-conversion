package com.paypay.currencyconversion.data.dto.recipes

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class Currencies(val currencies: RecipesItem)