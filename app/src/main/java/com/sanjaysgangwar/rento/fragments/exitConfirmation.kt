package com.sanjaysgangwar.rento.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.ExitConfirmationBinding
import com.sanjaysgangwar.rento.utils.mToast

class exitConfirmation : BottomSheetDialogFragment(), View.OnClickListener {
    private var binding: ExitConfirmationBinding? = null
    private val bind get() = binding!!
    private lateinit var navController: NavController

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
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.yes -> {
                FirebaseAuth.getInstance().signOut()
                navController = Navigation.findNavController(v)
                navController.navigate(R.id.home_to_splashScreen)
                mToast.successShowMessage(v.context, "Log out Successfully")

            }
            R.id.no -> {
                dismiss()
            }
        }
    }

}