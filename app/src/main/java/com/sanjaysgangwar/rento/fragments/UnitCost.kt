package com.sanjaysgangwar.rento.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.UnitCostBinding
import com.sanjaysgangwar.rento.utils.mToast

class unitCost : BottomSheetDialogFragment(), View.OnClickListener {
    private var binding: UnitCostBinding? = null
    private val bind get() = binding!!
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UnitCostBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents(view)
    }

    private fun initAllComponents(view: View) {
        bind.calculate.setOnClickListener(this)
        bind.add.setOnClickListener(this)
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference(view.resources.getString(R.string.app_name))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.calculate -> {
                if (!bind.electricityBill.text.isNullOrEmpty() && !bind.unitUsed.text.isNullOrEmpty()) {
                    var electricityBill = bind.electricityBill.text.toString().toDouble()
                    var unitUsed = bind.unitUsed.text.toString().toDouble()
                    val value = electricityBill / unitUsed
                    bind.unitCost.setText(value.toString())

                } else {
                    mToast.errorShow(v.context)
                }
            }
            R.id.add -> {
                if (!bind.unitCost.text.isNullOrEmpty()) {
                    sendValueToDatabase(v, bind.unitCost.text.toString())
                } else {
                    mToast.errorShow(v.context)
                }

            }
        }
    }

    private fun sendValueToDatabase(v: View, UnitCharge: String) {
        myRef.child(FirebaseAuth.getInstance().uid.toString())
            .child("profile").child("unit_cost")
            .setValue(UnitCharge)
            .addOnCompleteListener { sendToDatabase ->
                if (sendToDatabase.isSuccessful) {
                    hideProgressDialog()
                    mToast.successShowMessage(
                        context?.applicationContext!!,
                        "ADDED !!"
                    )
                    dismiss()
                } else {
                    hideProgressDialog()
                    mToast.errorMessageShow(
                        context?.applicationContext!!,
                        sendToDatabase.exception?.localizedMessage.toString()
                    )
                }
            }
    }
    private fun hideProgressDialog() {
        dismiss()
        bind.ProgressBar.root.visibility = View.GONE
        bind.add.visibility = View.VISIBLE
    }

    private fun showProgressDialog() {
        bind.ProgressBar.root.visibility = View.VISIBLE
        bind.add.visibility = View.GONE
    }

}