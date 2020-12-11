package com.sanjaysgangwar.rento.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.LoginBinding
import com.sanjaysgangwar.rento.utils.mToast


class Login : Fragment(), View.OnClickListener {
    private var binding: LoginBinding? = null
    private val bind get() = binding!!
    private lateinit var mAuth: FirebaseAuth
    lateinit var navController: NavController
    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents(view)

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.loginButton -> {
                if (bind.email.text.isNullOrEmpty() || bind.password.text.isNullOrEmpty()) {
                    mToast.errorMessageShow(v.context, "Enter Details")
                } else {
                    loginUserUsingEmailId(
                        v.context,
                        bind.email.text.toString().trim(),
                        bind.password.text.toString().trim()
                    )
                }
            }
            R.id.signUpButton -> {
                navController.navigate(R.id.login_to_sign_up)
            }
        }
    }

    private fun loginUserUsingEmailId(context: Context, email: String, password: String) {
        showProgressDialog()
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { signInTask ->

            if (signInTask.isSuccessful) {
                hideProgressDialog()
                navController.navigate(R.id.login_to_home)
            } else {
                if (bind.ProgressBar.root.isVisible) {
                    bind.ProgressBar.root.isGone
                }

                mToast.errorMessageShow(
                    context,
                    signInTask.exception?.message.toString()
                )
                hideProgressDialog()

            }

        }
    }

    private fun hideProgressDialog() {
        bind.ProgressBar.root.visibility = View.GONE
    }

    private fun showProgressDialog() {
        bind.ProgressBar.root.visibility = View.VISIBLE
    }

    private fun initAllComponents(view: View) {
        bind.loginButton.setOnClickListener(this)
        bind.signUpButton.setOnClickListener(this)
        mAuth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view)
    }
}