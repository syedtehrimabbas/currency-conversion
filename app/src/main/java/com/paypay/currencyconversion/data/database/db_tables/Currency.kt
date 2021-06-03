package com.paypay.currencyconversion.data.database.db_tables

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.paypay.currencyconversion.data.database.db_tables.TableNames.CURRENCY
import kotlinx.android.parcel.Parcelize

/**
 * A DTO which holds currency details like currency code and its name
 * @param code : currency code
 * @param currencyName : Name of the currency
 *
 */

@Entity(tableName = CURRENCY)
@Parcelize
class Currency(@PrimaryKey val code:String, val currencyName:String?) : Parcelable {
    override fun toString(): String {
        return currencyName.toString()
    }
}