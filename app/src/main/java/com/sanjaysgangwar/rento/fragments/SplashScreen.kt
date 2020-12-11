package com.sanjaysgangwar.rento.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sanjaysgangwar.rento.R

class SplashScreen : Fragment() {
    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        user = mAuth!!.currentUser
        navController = Navigation.findNavController(view)

        Handler().postDelayed({
            if (user?.uid.isNullOrEmpty()) {
                navController.navigate(R.id.splash_to_login)
            } else {
                navController.navigate(R.id.splash_to_home)
            }

        }, 1500)
    }


}