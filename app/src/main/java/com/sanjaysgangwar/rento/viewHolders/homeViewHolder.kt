package com.sanjaysgangwar.rento.viewHolders

import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.sanjaysgangwar.rento.R

class homeViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var namE: TextInputEditText = itemView!!.findViewById(R.id.name)
    var card: CardView = itemView!!.findViewById(R.id.card)
    var numbeR: TextInputEditText = itemView!!.findViewById(R.id.number)

}
