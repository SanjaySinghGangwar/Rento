package com.sanjaysgangwar.rento.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.SignUpBinding
import com.sanjaysgangwar.rento.utils.mToast

class signUp : Fragment(), View.OnClickListener {
    private var binding: SignUpBinding? = null
    private val bind get() = binding!!
    private lateinit var mAuth: FirebaseAuth
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignUpBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents(view)
    }

    private fun initAllComponents(view: View) {
        mAuth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
        bind.loginButton.setOnClickListener(this)
        bind.signUpButton.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.loginButton -> {
                navController.navigate(R.id.signUp_to_login)
            }
            R.id.signUpButton -> {
                if (bind.email.text.isNullOrEmpty() || bind.password.text.isNullOrEmpty() || bind.confirmPassword.text.isNullOrEmpty()) {
                    mToast.errorMessageShow(v.context, "Enter Details")
                } else {
                    if (bind.password.text.toString().trim() == bind.confirmPassword.text.toString()
                            .trim()
                    ) {
                        createUserInDatabase(
                            v,
                            bind.email.text.toString().trim(),
                            bind.password.text.toString().trim()
                        )
                    } else {
                        mToast.errorMessageShow(v.context, "Password Mismatch !!")
                    }

                }
            }
        }
    }

    private fun createUserInDatabase(
        v: View,
        email: String,
        password: String
    ) {
        showProgressDialog()
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { createUser ->
            if (createUser.isSuccessful) {
                hideProgressDialog()
                mToast.successShowMessage(v.context, "WELCOME")
                navController.navigate(R.id.signUp_to_home)
            } else {
                hideProgressDialog()
                mToast.errorMessageShow(
                    v.context,
                    createUser.exception?.message.toString()
                )
            }

        }
    }

    private fun hideProgressDialog() {
        bind.ProgressBar.root.visibility = View.GONE
    }

    private fun showProgressDialog() {
        bind.ProgressBar.root.visibility = View.VISIBLE
    }

}