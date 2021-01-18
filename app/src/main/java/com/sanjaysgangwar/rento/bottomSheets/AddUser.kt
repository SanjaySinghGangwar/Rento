package com.sanjaysgangwar.rento.bottomSheets

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.AddUsersBinding
import com.sanjaysgangwar.rento.utils.mToast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class addUser : BottomSheetDialogFragment(), View.OnClickListener {
    private var binding: AddUsersBinding? = null
    private val bind get() = binding!!
    lateinit var mAuth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var datePicker: MaterialDatePicker.Builder<*>
    lateinit var picker: MaterialDatePicker<*>
    lateinit var date: String
    lateinit var myRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddUsersBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents(view)


    }

    private fun initAllComponents(view: View) {
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference(view.resources.getString(R.string.app_name))
        datePicker = MaterialDatePicker.Builder.datePicker()
        datePicker.setTitleText("Select a Date")
        picker = datePicker.build()
        bind.date.setOnClickListener(this)
        bind.add.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.date -> {
                picker.show(parentFragmentManager, picker.toString())
                picker.addOnPositiveButtonClickListener {
                    date = picker.headerText
                    bind.date.setText(date)
                }
            }
            R.id.add -> {
                if (bind.name.text.toString().trim().isNullOrEmpty() ||
                    bind.number.text.toString().trim().isNullOrEmpty() ||
                    bind.rent.text.toString().trim().isNullOrEmpty() ||
                    bind.ccp.defaultCountryCodeWithPlus.toString().trim().isNullOrEmpty() ||
                    bind.date.text.toString().trim().isNullOrEmpty()
                ) {
                    mToast.errorMessageShow(v.context, "Enter All Details")
                } else {
                    sendToDatabase(
                        v.context,
                        bind.name.text.toString().trim(),
                        bind.ccp.defaultCountryCodeWithPlus.toString().trim(),
                        bind.number.text.toString().trim(),
                        bind.rent.text.toString().trim(),
                        bind.date.text.toString().trim()

                    )
                }
            }
        }
    }

    private fun sendToDatabase(
        context: Context,
        name: String,
        countryCode: String,
        number: String,
        rent: String,
        date: String,
    ) {
        showProgressDialog()
        val random = Random()
        val timestamp: String =
            SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) + random.nextInt(400)

        var dataToSend = HashMap<String, String>()
        dataToSend["name"] = name
        dataToSend["number"] = countryCode + number
        dataToSend["rent"] = rent
        dataToSend["date"] = date
        myRef.child(FirebaseAuth.getInstance().uid.toString()).child("tenants")
            .child(timestamp)
            .setValue(dataToSend)
            .addOnCompleteListener { sendToDatabase ->
                if (sendToDatabase.isSuccessful) {
                    hideProgressDialog()
                    mToast.successShowMessage(context, "ADDED !!")
                    dismiss()
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
        bind.add.visibility = View.VISIBLE
    }

    private fun showProgressDialog() {
        bind.ProgressBar.root.visibility = View.VISIBLE
        bind.add.visibility = View.GONE
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        super.showNow(manager, tag)
    }

    override fun dismiss() {
        super.dismiss()
    }
}