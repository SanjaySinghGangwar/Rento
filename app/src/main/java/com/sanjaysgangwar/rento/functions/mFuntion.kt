package com.sanjaysgangwar.rento.functions

import android.content.Context
import com.sanjaysgangwar.rento.utils.mToast

object mFuntion {
    fun sendBillToThePhoneNumber(
        context: Context,
        number: String,
        rent: String,
        perUnitCst: String,
        currentUnit: String,
        unitUsed: String,
        total: String
    ) {
        mToast.checker(context)
    }
}