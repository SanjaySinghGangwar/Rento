package com.sanjaysgangwar.rento.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.ProfileBinding
import com.sanjaysgangwar.rento.utils.mToast

class profile : BottomSheetDialogFragment(), View.OnClickListener {
    private var binding: ProfileBinding? = null
    private val bind get() = binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProfileBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents(view)
    }

    private fun initAllComponents(view: View) {
        bind.name.setOnClickListener(this)
        bind.profileImage.setOnClickListener(this)
        bind.add.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add -> {
                mToast.checker(v.context)
            }
            R.id.profileImage -> {
                mToast.checker(v.context)
            }
            R.id.name -> {
                mToast.checker(v.context)
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
}