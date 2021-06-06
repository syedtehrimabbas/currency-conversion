package com.paypay.currencyconversion.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import com.paypay.currencyconversion.data.Resource
import com.paypay.currencyconversion.data.dto.currency.CurrenciesRatesResponse
import com.paypay.currencyconversion.data.dto.currency.CurrenciesResponse
import com.paypay.currencyconversion.utils.SingleEvent


abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    abstract fun observeViewModel()
    protected abstract fun init()
    protected abstract fun getResLayoutRes(): Int
    protected abstract fun getVariableId(): Int
    protected abstract fun handleRates(ratesResponse: Resource<CurrenciesRatesResponse>)
    protected abstract fun handleCurrency(currencyResponse: Resource<CurrenciesResponse>)
    protected abstract fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>)
    lateinit var binding: ViewDataBinding
    protected abstract val viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        init()
        observeViewModel()
    }

    private fun initViewBinding() {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                getResLayoutRes(),
                null,
                false
            )
        binding.setVariable(getVariableId(), viewModel)
        setContentView(binding.root)
    }

}
