package com.paypay.currencyconversion.utils

fun String?.parseDouble(): Double {
    return if (this != null && this.isNotEmpty()) {
        this.toDouble()
    } else 1.0
}