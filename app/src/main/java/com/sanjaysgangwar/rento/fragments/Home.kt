package com.sanjaysgangwar.rento.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.HomeBinding
import com.sanjaysgangwar.rento.model.modelClass
import com.sanjaysgangwar.rento.viewHolders.homeViewHolder


class Home : Fragment(), View.OnClickListener {
    private var binding: HomeBinding? = null
    private val bind get() = binding!!
    private lateinit var myRef: DatabaseReference
    lateinit var database: FirebaseDatabase
    private lateinit var navController: NavController
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents(view)
    }

    private fun initAllComponents(view: View) {
        navController = Navigation.findNavController(view)

        database = FirebaseDatabase.getInstance()
        myRef = database.getReference(view.resources.getString(R.string.app_name))
            .child(FirebaseAuth.getInstance().uid.toString())
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(view.context)
        customAlertDialogView = LayoutInflater.from(context)
            .inflate(R.layout.add_users, null, false)
        bind.fab.setOnClickListener(this)
        bind.showTenents.layoutManager = LinearLayoutManager(context)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fab -> {
                openDialogToEnterInfo()
            }
        }
    }


    private fun openDialogToEnterInfo() {
        val add = addUser()
        add.showNow(parentFragmentManager, "add User")
    }

    override fun onStart() {
        super.onStart()
        initRecycler()

    }

    private fun initRecycler() {
        val option: FirebaseRecyclerOptions<modelClass> =
            FirebaseRecyclerOptions.Builder<modelClass>()
                .setQuery(myRef.orderByChild("name"), modelClass::class.java)
                .build()
        val recyclerAdapter =
            object : FirebaseRecyclerAdapter<modelClass, homeViewHolder>(option) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeViewHolder {
                    val view =
                        LayoutInflater.from(context)
                            .inflate(R.layout.show_user_layout, parent, false)
                    return homeViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: homeViewHolder,
                    position: Int,
                    model: modelClass
                ) {
                    holder.namE.setText(model.name)
                    holder.numbeR.setText(model.number)


                    holder.numbeR.setOnClickListener { clicked ->
                        operationToPerform(view, position)
                    }
                    holder.namE.setOnClickListener { clicked ->
                        operationToPerform(view, position)
                    }

                    holder.card.setOnClickListener { clicked ->
                        operationToPerform(view, position)
                    }
                }


            }

        bind.showTenents.adapter = recyclerAdapter
        recyclerAdapter.startListening()
    }

    private fun operationToPerform(view: View?, position: Int) {
        Log.i("OnClick ", "operationToPerform: $position")
    }
}


