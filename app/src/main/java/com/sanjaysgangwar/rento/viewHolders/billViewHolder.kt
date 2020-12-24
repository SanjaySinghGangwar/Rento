package com.sanjaysgangwar.rento.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sanjaysgangwar.rento.R

class billViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var unitUsed: TextView = itemView!!.findViewById(R.id.unitUsed)
    var perUnitCost: TextView = itemView!!.findViewById(R.id.perUnitCost)
    var rentAmount: TextView = itemView!!.findViewById(R.id.rentAmount)
    var totalBill: TextView = itemView!!.findViewById(R.id.totalBill)
    var month: TextView = itemView!!.findViewById(R.id.month)
    var electrictyAmount: TextView = itemView!!.findViewById(R.id.electrictyAmount)
    var sendBill: ImageView = itemView!!.findViewById(R.id.sendBill)
}