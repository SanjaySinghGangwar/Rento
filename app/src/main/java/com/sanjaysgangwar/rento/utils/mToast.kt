package com.sanjaysgangwar.rento.utils

import android.R
import android.content.Context
import android.graphics.PorterDuff
import android.widget.TextView
import android.widget.Toast

object mToast {
    fun errorShow(context: Context) {
        val toast = Toast.makeText(context, "Something went wrong !!", Toast.LENGTH_LONG)
        val view = toast.view
        view!!.background.setColorFilter(
            context.resources.getColor(R.color.holo_red_dark),
            PorterDuff.Mode.SRC_IN
        )
        val text = view.findViewById<TextView>(R.id.message)
        text.textSize
        text.setTextColor(context.resources.getColor(R.color.white))
        toast.show()
    }

    fun errorMessageShow(context: Context, Message: String?) {
        val toast = Toast.makeText(context, Message, Toast.LENGTH_LONG)
        val view = toast.view
        view!!.background.setColorFilter(
            context.resources.getColor(R.color.holo_red_dark),
            PorterDuff.Mode.SRC_IN
        )
        val text = view.findViewById<TextView>(R.id.message)
        text.textSize
        text.setTextColor(context.resources.getColor(R.color.white))
        toast.show()
    }

    fun toastShow(context: Context, Message: String?) {
        val toast = Toast.makeText(context, Message, Toast.LENGTH_LONG)
        val view = toast.view
        view!!.background.setColorFilter(
            context.resources.getColor(R.color.primary_text_light),
            PorterDuff.Mode.SRC_IN
        )
        val text = view.findViewById<TextView>(R.id.message)
        text.textSize
        text.setTextColor(context.resources.getColor(R.color.white))
        toast.show()
    }

    fun successShow(context: Context) {
        val toast = Toast.makeText(context, "Done", Toast.LENGTH_LONG)
        val view = toast.view
        view!!.background.setColorFilter(
            context.resources.getColor(R.color.holo_green_dark),
            PorterDuff.Mode.SRC_IN
        )
        val text = view.findViewById<TextView>(R.id.message)
        text.textSize
        text.setTextColor(context.resources.getColor(R.color.white))
        toast.show()
    }

    fun checker(context: Context?) {
        Toast.makeText(context, "Checker !! ", Toast.LENGTH_LONG).show()
    }

    fun showToast(context: Context?, message: String?, BackgroundColor: Int, textColor: Int) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        val view = toast.view
        view!!.background.setColorFilter(BackgroundColor, PorterDuff.Mode.SRC_IN)
        val text = view.findViewById<TextView>(R.id.message)
        text.textSize
        text.setTextColor(textColor)
        toast.show()
    }
}
