package com.paypay.currencyconversion.ui.component.currency

import androidx.recyclerview.widget.RecyclerView
import com.paypay.currencyconversion.data.database.db_tables.CurrencyRate
import com.paypay.currencyconversion.databinding.ItemCurrencyRateBinding

class CurrencyRateViewHolder(private val itemYapStoreBinding: ItemCurrencyRateBinding) :
    RecyclerView.ViewHolder(itemYapStoreBinding.root) {

    fun onBind(currencyRate: CurrencyRate?) {
        itemYapStoreBinding.rate = currencyRate?.rate
        itemYapStoreBinding.currency = currencyRate?.currencyCode
        itemYapStoreBinding.executePendingBindings()
    }
}