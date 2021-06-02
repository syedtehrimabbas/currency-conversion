package com.paypay.currencyconversion.ui.component.currency

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.paypay.currencyconversion.data.Resource
import com.paypay.currencyconversion.data.dto.recipes.Currencies
import com.paypay.currencyconversion.databinding.ActivityCurrencyConversionBinding
import com.paypay.currencyconversion.ui.base.BaseActivity
import com.paypay.currencyconversion.utils.*
import com.paypay.currencyconversion.utils.SingleEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrencyConversionActivity : BaseActivity() {
    private lateinit var binding: ActivityCurrencyConversionBinding

    private val recipesListViewModel: CurrenciesViewModel by viewModels()
//    private lateinit var recipesAdapter: RecipesAdapter

    override fun initViewBinding() {
        binding = ActivityCurrencyConversionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesListViewModel.getRecipes()
    }

    private fun bindListData(recipes: Currencies) {
        if (false) {
//            recipesAdapter = RecipesAdapter(recipesListViewModel, recipes.recipesList)
//            binding.rvRecipesList.adapter = recipesAdapter
            showDataView(true)
        } else {
            showDataView(false)
        }
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


    private fun handleRecipesList(status: Resource<Currencies>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(recipes = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { recipesListViewModel.showToastMessage(it) }
            }
        }
    }

    override fun observeViewModel() {
        observe(recipesListViewModel.recipesLiveData, ::handleRecipesList)
        observeSnackBarMessages(recipesListViewModel.showSnackBar)
        observeToast(recipesListViewModel.showToast)
    }
}