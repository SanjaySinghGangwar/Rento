package com.sanjaysgangwar.rento.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.DonateBinding


class donate : Fragment(), View.OnClickListener {
    private var binding: DonateBinding? = null
    private val bind get() = binding!!
    var intent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DonateBinding.inflate(layoutInflater, container, false)
        return bind.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents(view)
    }

    private fun initAllComponents(view: View) {
        bind.textMessage.text =
            "Help us to keep " + getString(R.string.app_name) + " free to download, share and use by contributing to ..."
        bind.donate.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.donate -> {
                intent = Intent()
                intent!!.action = Intent.ACTION_VIEW
                intent!!.data = Uri.parse("https://www.buymeacoffee.com/TheAverageGuy")
                context?.startActivity(Intent.createChooser(intent, "Donate Via ♥"))
            }
        }
    }


}