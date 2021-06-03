package com.paypay.currencyconversion.ui.base

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.paypay.currencyconversion.data.Resource
import com.paypay.currencyconversion.data.dto.currency.CurrenciesRatesResponse
import com.paypay.currencyconversion.data.dto.currency.CurrenciesResponse


abstract class BaseActivity : AppCompatActivity() {

    abstract fun observeViewModel()
    protected abstract fun initViewBinding()
    protected abstract fun handleRates(status: Resource<CurrenciesRatesResponse>)
    protected abstract fun handleRecipesList(status: Resource<CurrenciesResponse>)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        observeViewModel()
    }

    fun setSpinnerItems(spinner: AppCompatSpinner, list: List<String>) {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(baseContext, R.layout.simple_spinner_item,list)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

}
