package com.paypay.currencyconversion.data.database


import android.content.Context
import android.content.SharedPreferences
import com.paypay.currencyconversion.BuildConfig
import java.util.concurrent.TimeUnit

class AppPreferences constructor(val context: Context?) {

    /**
     * FEILD OF CLASS *
     */
    private var mPrefName = PREFS_NAME
    private var mSharedPre: SharedPreferences? = null
    private var mEditor: SharedPreferences.Editor? = null

    init {
        if (null != context) {
            if (mSharedPre == null) {
                mSharedPre = context.getSharedPreferences(mPrefName, Context.MODE_PRIVATE)

                mEditor = mSharedPre?.edit()
            } else {
                mEditor = null
                mSharedPre = null
            }
        }
    }

    /**
     * Set data for Long
     *
     * @param preName Preferences name
     * @param value   Long input
     */
    @Synchronized
    fun setPreferences(
        preName: String,
        value: Long
    ) {
        mEditor!!.putLong(preName, value)
        mEditor!!.commit()
    }

    /**
     * Get data for Long
     *
     * @param preName      Preferences name
     * @param defaultValue
     * @return Long or 0 if Name not existed
     */
    fun getPreferences(
        preName: String,
        defaultValue: Long
    ): Long {
        return mSharedPre!!.getLong(preName, defaultValue)
    }


    fun liveDataRequired(): Boolean {
        val currentTimestamp = System.currentTimeMillis()
        val savedTimestamp = getPreferences(AppPreferences.SYNC_TIME, -1)
        val diff = TimeUnit.MINUTES.toMillis(30)
        return currentTimestamp.minus(savedTimestamp) > diff
    }

    companion object {
        private val PREFS_NAME = BuildConfig.APPLICATION_ID + BuildConfig.BUILD_TYPE + "currency"
        val SYNC_TIME = "SYNC_TIME"
    }
}
