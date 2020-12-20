package com.sanjaysgangwar.rento.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.GenerateBillBinding
import com.sanjaysgangwar.rento.utils.mToast
import java.util.*


class generateBill(
    private val userID: String,
    private val rent: String,
    private val number: String,
    private val lastUnit: String
) : BottomSheetDialogFragment(), View.OnClickListener {
    private var binding: GenerateBillBinding? = null
    private val bind get() = binding!!
    private lateinit var myRef: DatabaseReference
    lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GenerateBillBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents(view)
        fetchUnitCostFromDatabase()
    }

    private fun fetchUnitCostFromDatabase() {
        myRef.child("profile").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild("unit_cost")) {
                    bind.perUnitCost.setText(dataSnapshot.child("unit_cost").value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                mToast.errorMessageShow(
                    context?.applicationContext!!,
                    error.toException().message
                )
            }
        })
    }

    private fun initAllComponents(view: View) {
        bind.rent.setText(rent)
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference(view.resources.getString(R.string.app_name))
            .child(FirebaseAuth.getInstance().uid.toString())
        bind.lastUnit.setText(lastUnit)
        bind.createBill.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.createBill -> {
                if (!bind.currentUnit.text.toString()
                        .isNullOrEmpty() && !bind.lastUnit.text.toString()
                        .isNullOrEmpty() && !bind.perUnitCost.text.toString().isNullOrEmpty()
                    && bind.currentUnit.text.toString() > bind.lastUnit.text.toString()
                ) {
                    var currentUnit = bind.currentUnit.text.toString().toDouble()
                    var lastUnit = bind.lastUnit.text.toString().toDouble()
                    var perUnitCst = bind.perUnitCost.text.toString().toDouble()
                    var rent = bind.rent.text.toString().toDouble()
                    var unitUsed = currentUnit - lastUnit
                    var total =
                        ((unitUsed)
                                * perUnitCst) + rent
                    sendBillToTheDatabase(
                        v.context,
                        rent.toString(),
                        perUnitCst.toString(),
                        currentUnit.toString(),
                        unitUsed.toString(),
                        total.toString()
                    )
                    bind.total.setText(total.toString())
                } else {
                    mToast.errorShow(v.context)
                }
            }
        }


    }

    private fun sendBillToTheDatabase(
        context: Context,
        rent: String,
        perUnitCst: String,
        currentUnit: String,
        unitUsed: String,
        total: String
    ) {
        showProgressDialog()
        var month = Calendar.getInstance()
            .getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())

        var dataToSend = HashMap<String, String>()
        dataToSend["perUnitCst"] = perUnitCst
        dataToSend["currentUnit"] = currentUnit
        dataToSend["unitUsed"] = unitUsed
        dataToSend["total"] = total
        dataToSend["rent"] = rent

        myRef.child("tenants").child(userID).child("bill").child(month.toString())
            .setValue(dataToSend)
            .addOnCompleteListener { sendToDatabase ->
                if (sendToDatabase.isSuccessful) {
                    myRef.child("tenants").child(userID).child("last_unit").setValue(currentUnit)
                        .addOnCompleteListener { done ->
                            if (done.isSuccessful) {
                                hideProgressDialog()
                                mToast.successShowMessage(context, "Created !!")

                            } else {
                                hideProgressDialog()
                                mToast.errorMessageShow(context, "ERROR !!")

                            }
                        }

                } else {
                    hideProgressDialog()
                    mToast.errorMessageShow(
                        context,
                        sendToDatabase.exception?.localizedMessage.toString()
                    )
                }
            }
    }

    private fun hideProgressDialog() {
        bind.ProgressBar.root.visibility = View.GONE
        bind.createBill.visibility = View.VISIBLE
    }

    private fun showProgressDialog() {
        bind.ProgressBar.root.visibility = View.VISIBLE
        bind.createBill.visibility = View.GONE
    }
}