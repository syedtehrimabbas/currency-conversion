package com.paypay.currencyconversion.adapters

import androidx.databinding.ViewDataBinding
import com.paypay.currencyconversion.R
import com.paypay.currencyconversion.data.database.db_tables.CurrencyRate
import com.paypay.currencyconversion.databinding.ItemCurrencyRateBinding
import com.paypay.currencyconversion.ui.base.BaseBindingRecyclerAdapter
import com.paypay.currencyconversion.ui.component.currency.CurrencyRateViewHolder

class CurrencyRatesAdapter(val data: MutableList<CurrencyRate>) :
    BaseBindingRecyclerAdapter<CurrencyRate, CurrencyRateViewHolder>(data) {

    override fun onCreateViewHolder(binding: ViewDataBinding): CurrencyRateViewHolder =
        CurrencyRateViewHolder(
            binding as ItemCurrencyRateBinding
        )

    override fun onBindViewHolder(holder: CurrencyRateViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(getDataForPosition(position))
    }

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_currency_rate

}
