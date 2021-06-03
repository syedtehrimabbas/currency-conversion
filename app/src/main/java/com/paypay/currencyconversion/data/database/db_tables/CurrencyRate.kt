package com.paypay.currencyconversion.data.database.db_tables

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.paypay.currencyconversion.data.database.db_tables.TableNames.CURRENCY_RATES
import kotlinx.android.parcel.Parcelize

/**
 * A DTO which holds currency details like currency code and its rate
 * @param currencyCode : currency code
 * @param rate : Rate of currency
 *
 */

@Entity(tableName = CURRENCY_RATES)
@Parcelize
class CurrencyRate(@PrimaryKey val currencyCode: String, val rate: Double?) : Parcelable {
    override fun toString(): String {
        return rate.toString()
    }
}