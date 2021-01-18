package com.sanjaysgangwar.rento.bottomSheets

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.UpdateDataBinding
import com.sanjaysgangwar.rento.utils.mToast

class updateData(
    private val userID: String,
    private val case: String,
    private val hint: String,
    private val tittle: String
) :
    BottomSheetDialogFragment(), View.OnClickListener {
    private var _binding: UpdateDataBinding? = null
    private val bind get() = _binding!!
    lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UpdateDataBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents(view)
        initDialog()
    }

    private fun initDialog() {
        bind.tittle.text = tittle
        bind.data.hint = hint
        if (case == "name") {
            bind.data.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

    private fun initAllComponents(view: View) {
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference(view.resources.getString(R.string.app_name))
            .child(FirebaseAuth.getInstance().uid.toString()).child("tenants").child(userID)
        bind.update.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.update -> {
                if (bind.data.text.isNullOrEmpty()) {
                    mToast.errorMessageShow(v.context, "Enter a valid input")
                } else {
                    myRef.child(case).setValue(bind.data.text.toString().trim())
                        .addOnCompleteListener { push ->
                            if (push.isSuccessful) {
                                dismiss()
                            }
                        }


                }

            }
        }
    }

}