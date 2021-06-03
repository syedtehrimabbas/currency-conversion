package com.paypay.currencyconversion.ui.component.currency

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.paypay.currencyconversion.data.Resource
import com.paypay.currencyconversion.data.dto.currency.CurrenciesRatesResponse
import com.paypay.currencyconversion.data.dto.currency.CurrenciesResponse
import com.paypay.currencyconversion.databinding.ActivityCurrencyConversionBinding
import com.paypay.currencyconversion.ui.base.BaseActivity
import com.paypay.currencyconversion.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrencyConversionActivity : BaseActivity() {
    private lateinit var binding: ActivityCurrencyConversionBinding
    private val currencyConversionListViewModel: CurrenciesViewModel by viewModels()

    override fun initViewBinding() {
        binding = ActivityCurrencyConversionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currencyConversionListViewModel.loadCurrencies()
//        currencyConversionListViewModel.loadRates()
    }

    private fun bindCurrencyAdapter(currenciesResponse: CurrenciesResponse) {
        if (currenciesResponse.success) {
            setSpinnerItems(
                binding.currencyAS,
                currenciesResponse.currencies.entries.map { "${it.key} - ${it.value}" })
            showDataView(true)
        }
    }

    private fun bindRatesAdapter(currenciesRatesResponse: CurrenciesRatesResponse) {
        showDataView(currenciesRatesResponse.success)
    }

    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    private fun showDataView(show: Boolean) {
        binding.tvNoData.visibility = if (show) GONE else VISIBLE
        binding.rvRecipesList.visibility = if (show) VISIBLE else GONE
        binding.pbLoading.toGone()
    }

    private fun showLoadingView() {
        binding.pbLoading.toVisible()
        binding.tvNoData.toGone()
        binding.rvRecipesList.toGone()
    }


    override fun handleRecipesList(status: Resource<CurrenciesResponse>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindCurrencyAdapter(currenciesResponse = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { currencyConversionListViewModel.showToastMessage(it) }
            }
        }
    }

    override fun handleRates(status: Resource<CurrenciesRatesResponse>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindRatesAdapter(currenciesRatesResponse = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { currencyConversionListViewModel.showToastMessage(it) }
            }
        }
    }

    override fun observeViewModel() {
        observe(currencyConversionListViewModel.currenciesLiveData, ::handleRecipesList)
        observe(currencyConversionListViewModel.ratesLiveData, ::handleRates)
        observeSnackBarMessages(currencyConversionListViewModel.showSnackBar)
        observeToast(currencyConversionListViewModel.showToast)
    }
}