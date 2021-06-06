package com.paypay.currencyconversion.ui.component.currency

import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.paypay.currencyconversion.BR
import com.paypay.currencyconversion.R
import com.paypay.currencyconversion.data.Resource
import com.paypay.currencyconversion.data.database.AppPreferences
import com.paypay.currencyconversion.data.dto.currency.CurrenciesRatesResponse
import com.paypay.currencyconversion.data.dto.currency.CurrenciesResponse
import com.paypay.currencyconversion.databinding.ActivityCurrencyConversionBinding
import com.paypay.currencyconversion.ui.base.BaseActivity
import com.paypay.currencyconversion.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CurrencyConversionActivity : BaseActivity<CurrenciesViewModel>() {

    @Inject
    lateinit var preferences: AppPreferences

    override val viewModel: CurrenciesViewModel by viewModels()

    override fun getResLayoutRes() = R.layout.activity_currency_conversion

    override fun getVariableId() = BR.currenciesViewModel

    override fun init() {
        viewModel.loadCurrencies()
        viewModel.loadRates()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_actions, menu)
        if (!preferences.liveDataRequired())
            menu?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_sync_disabled_24)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                if (preferences.liveDataRequired())
                    init()
                else viewModel.errorMessagePrivate.value =
                    SingleEvent(getString(R.string.live_rates_not_available))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindCurrencyAdapter(currenciesResponse: CurrenciesResponse) {
        if (currenciesResponse.success) {
            setSpinnerItems(
                binding().currencyAS,
                currenciesResponse.currencies.entries.map { it.key.toString() })
        }
    }

    private fun binding(): ActivityCurrencyConversionBinding =
        binding as ActivityCurrencyConversionBinding

    private fun showLoadingView() {
        binding().pbLoading.toVisible()
        binding().rvCurrencyRates.toGone()
    }

    private fun hideLoading() {
        binding().pbLoading.toGone()
        binding().rvCurrencyRates.toVisible()
    }

    override fun handleCurrency(currencyResponse: Resource<CurrenciesResponse>) {
        when (currencyResponse) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> {
                hideLoading()
                currencyResponse.data?.let {
                    bindCurrencyAdapter(
                        currenciesResponse = it
                    )
                }
            }
            is Resource.DataError -> {
                hideLoading()
                currencyResponse.message.let {
                    viewModel.errorMessagePrivate.value =
                        SingleEvent(it.toString())
                }
            }
        }
    }

    override fun handleRates(ratesResponse: Resource<CurrenciesRatesResponse>) {
        when (ratesResponse) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> {
                hideLoading()
                ratesResponse.data?.let {

                    viewModel.currencyRatesAdapter.setList(ratesResponse.data.quotes.toRatesList())
                }
            }
            is Resource.DataError -> {
                hideLoading()
                ratesResponse.message.let {
                    viewModel.errorMessagePrivate.value =
                        SingleEvent(it.toString())
                }
            }
        }
    }

    override fun observeViewModel() {
        observe(viewModel.currenciesLiveData, ::handleCurrency)
        observe(viewModel.ratesLiveData, ::handleRates)
        observeSnackBarMessages(viewModel.errorMessage)
    }

    private fun setSpinnerItems(spinner: AppCompatSpinner, list: List<String>) {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(baseContext, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding().root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }
}