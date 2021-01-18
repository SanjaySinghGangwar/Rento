package com.sanjaysgangwar.rento.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.ExitConfirmationBinding
import com.sanjaysgangwar.rento.utils.mToast

class exitConfirmation(
    private val status: String,
    private val userID: String,
    private val navController: NavController
) :
    BottomSheetDialogFragment(),
    View.OnClickListener {
    private var binding: ExitConfirmationBinding? = null
    private val bind get() = binding!!

    private lateinit var myRef: DatabaseReference
    lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ExitConfirmationBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents(view)
    }

    private fun initAllComponents(view: View) {
        bind.yes.setOnClickListener(this)
        bind.no.setOnClickListener(this)
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference(view.resources.getString(R.string.app_name))
            .child(FirebaseAuth.getInstance().uid.toString())
        if (status == "logOut") {
            bind.text.text = "Do You Want To Logout ?"
        } else if (status == "deleteUser") {
            bind.text.text = "Are you sure you want to delete the tenant ?"
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.yes -> {
                if (status == "logOut") {
                    FirebaseAuth.getInstance().signOut()
                    navController?.navigate(R.id.home_to_splashScreen)
                    mToast.successShowMessage(v.context, "Logout Successfully")
                    dismiss()
                } else if (status == "deleteUser") {
                    myRef.child("tenants")
                        .child(userID).removeValue().addOnCompleteListener { onComplete ->
                            if (onComplete.isSuccessful) {
                                mToast.successShowMessage(view?.context!!, "Done !!")
                                navController.navigate(R.id.tenantDetails_to_home)
                                dismiss()
                            } else {
                                mToast.errorMessageShow(
                                    view?.context!!,
                                    onComplete.exception?.localizedMessage
                                )
                            }

                        }
                }
            }
            R.id.no -> {
                dismiss()
            }
        }
    }

}