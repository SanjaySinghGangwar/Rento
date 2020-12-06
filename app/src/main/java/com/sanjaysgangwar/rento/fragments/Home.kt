package com.sanjaysgangwar.rento.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.utils.mToast

class Home : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home, container, false)
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(view.context)
        customAlertDialogView = LayoutInflater.from(context)
            .inflate(R.layout.add_users, null, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fab -> {
                openDialogToEnterInfo()
            }
        }
    }


    private fun openDialogToEnterInfo() {
        val ad = addUser()
        ad.showNow(parentFragmentManager,"add User")
    }
}