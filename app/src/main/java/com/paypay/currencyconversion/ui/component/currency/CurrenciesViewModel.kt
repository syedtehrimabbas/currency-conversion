package com.paypay.currencyconversion.ui.component.currency

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.paypay.currencyconversion.data.DataRepositorySource
import com.paypay.currencyconversion.data.Resource
import com.paypay.currencyconversion.data.dto.currency.CurrenciesRatesResponse
import com.paypay.currencyconversion.data.dto.currency.CurrenciesResponse
import com.paypay.currencyconversion.ui.base.BaseViewModel
import com.paypay.currencyconversion.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CurrenciesViewModel @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel() {

    /**
     * Data --> LiveData, Exposed as LiveData, Locally in viewModel as MutableLiveData
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val currenciesLiveDataPrivate = MutableLiveData<Resource<CurrenciesResponse>>()
    val currenciesLiveData: LiveData<Resource<CurrenciesResponse>> get() = currenciesLiveDataPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val ratesLiveDataPrivate = MutableLiveData<Resource<CurrenciesRatesResponse>>()
    val ratesLiveData: LiveData<Resource<CurrenciesRatesResponse>> get() = ratesLiveDataPrivate

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val openRecipeDetailsPrivate = MutableLiveData<SingleEvent<CurrenciesResponse>>()
    val openRecipeDetails: LiveData<SingleEvent<CurrenciesResponse>> get() = openRecipeDetailsPrivate

    /**
     * Error handling as UI
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate


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

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }
}
