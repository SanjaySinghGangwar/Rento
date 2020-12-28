package com.sanjaysgangwar.rento.functions

import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import com.sanjaysgangwar.rento.utils.mToast


object mSms {
    var message: String? = null
    var intent: Intent? = null

    fun sendDetailsedBill(
        context: Context,
        month: String,
        Total: String,
        rent: String,
        ElectrityBill: String,
        unitUsed: String,
        perUnitCst: String,
        currentUnit: String,

        ) {
        message =
            "$month Bill \nTotal : $Total/-\nRent : $rent/-\nElectricity Bill : $ElectrityBill/-\n\nDetails : \nUnit Used : $unitUsed\nPer Unit Cost = $perUnitCst/-\nCurrent Unit : $currentUnit"
        intent = Intent()
        intent!!.action = Intent.ACTION_SEND
        intent!!.putExtra(Intent.EXTRA_TEXT, message)
        intent!!.type = "text/plain"


        context.startActivity(Intent.createChooser(intent, "send to"))
    }

    fun sendBill(
        context: Context,
        month: String,
        Total: String,
        rent: String,
        ElectrityBill: String,
        unitUsed: String,
        perUnitCst: String

    ) {
        message =
            "$month Bill\nRent : $rent\nElectricity Bill : $ElectrityBill\nTotal : $Total\n\nDetails : \nUnit Used : $unitUsed\nPer Unit Cost = $perUnitCst"
        intent = Intent();
        intent!!.action = Intent.ACTION_SEND
        intent!!.putExtra(Intent.EXTRA_TEXT, message)
        intent!!.type = "text/plain"
        context.startActivity(Intent.createChooser(intent, "send to"))
    }
}