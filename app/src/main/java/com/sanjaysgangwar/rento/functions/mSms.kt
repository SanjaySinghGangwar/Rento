package com.sanjaysgangwar.rento.functions

import android.content.Context
import android.telephony.SmsManager
import com.sanjaysgangwar.rento.utils.mToast

object mSms {
    var smsManager: SmsManager? = null
    var message: String? = null

    fun sendDetailsedBill(
        context: Context,
        number: String,
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
        smsManager = SmsManager.getDefault();
        //smsManager.sendDataMessage(number,)
        smsManager?.sendTextMessage(number, null, message, null, null);
        mToast.checker(context)
    }

    fun sendBill(
        context: Context,
        month: String,
        number: String,
        Total: String,
        rent: String,
        ElectrityBill: String,
        unitUsed: String,
        perUnitCst: String

    ) {
        message =
            "$month Bill\nRent : $rent\nElectricity Bill : $ElectrityBill\nTotal : $Total\n\nDetails : \nUnit Used : $unitUsed\nPer Unit Cost = $perUnitCst"
        smsManager = SmsManager.getDefault();
        smsManager?.sendTextMessage(
            number,
            null,
            message,
            null,
            null
        );
        mToast.successShowMessage(context, "Sent !!")
    }
}