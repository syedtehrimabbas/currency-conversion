package com.paypay.currencyconversion.ui.component.currency

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.paypay.currencyconversion.BuildConfig
import com.paypay.currencyconversion.adapters.CurrencyRatesAdapter
import com.paypay.currencyconversion.data.DataRepositorySource
import com.paypay.currencyconversion.data.Resource
import com.paypay.currencyconversion.data.database.db_tables.CurrencyRate
import com.paypay.currencyconversion.data.dto.currency.CurrenciesRatesResponse
import com.paypay.currencyconversion.data.dto.currency.CurrenciesResponse
import com.paypay.currencyconversion.ui.base.BaseViewModel
import com.paypay.currencyconversion.utils.SingleEvent
import com.paypay.currencyconversion.utils.formattedAmount
import com.paypay.currencyconversion.utils.parseDouble
import com.paypay.currencyconversion.utils.toRatesList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CurrenciesViewModel @Inject constructor(private val dataRepositoryRepository: DataRepositorySource) :
    BaseViewModel() {

    private val currenciesLiveDataPrivate = MutableLiveData<Resource<CurrenciesResponse>>()
    val currenciesLiveData: LiveData<Resource<CurrenciesResponse>> get() = currenciesLiveDataPrivate

    private val ratesLiveDataPrivate = MutableLiveData<Resource<CurrenciesRatesResponse>>()
    val ratesLiveData: LiveData<Resource<CurrenciesRatesResponse>> get() = ratesLiveDataPrivate

    val errorMessagePrivate = MutableLiveData<SingleEvent<Any>>()
    val errorMessage: LiveData<SingleEvent<Any>> get() = errorMessagePrivate

    @Inject
    lateinit var currencyRatesAdapter: CurrencyRatesAdapter

    var selectedCurrency: String = ""
    private var amount: Double = 1.0

    fun loadCurrencies() {
        viewModelScope.launch {
            currenciesLiveDataPrivate.value = Resource.Loading()
            dataRepositoryRepository.requestCurrencies().collect {
                currenciesLiveDataPrivate.value = it
            }
        }
    }

    fun loadRates() {
        viewModelScope.launch {
            currenciesLiveDataPrivate.value = Resource.Loading()
            dataRepositoryRepository.requestCurrenciesRates().collect {
                ratesLiveDataPrivate.value = it
            }
        }
    }

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        amount = s.toString().parseDouble()
        calculateRates()
    }


    fun onSelectItem(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        parent?.selectedItem.apply {
            selectedCurrency = this as String
            calculateRates()
        }
    }

    private fun calculateRates() {
        ratesLiveDataPrivate.value?.data?.let {
            currencyRatesAdapter.setList(
                getRatesForCurrency(
                    amount,
                    selectedCurrency,
                    it.quotes.toRatesList()
                )
            )
        }
    }

    private fun getRatesForCurrency(
        amount: Double,
        selectedCurrency: String,
        actualRates: List<CurrencyRate>
    ): List<CurrencyRate> {

        val rate =
            actualRates.find { it.currencyCode == "${BuildConfig.DEFAULT_CURRENCY}$selectedCurrency" }
        val sourceRate = amount.div(rate?.rate ?: 0.0)
        return actualRates.map {
            CurrencyRate(
                it.currencyCode.replace(BuildConfig.DEFAULT_CURRENCY, "$selectedCurrency → "),
                (sourceRate.times(it.rate).formattedAmount())
            )
        }
    }
}
