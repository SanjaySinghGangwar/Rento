package com.sanjaysgangwar.rento.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.bottomSheets.addUser
import com.sanjaysgangwar.rento.bottomSheets.exitConfirmation
import com.sanjaysgangwar.rento.bottomSheets.profile
import com.sanjaysgangwar.rento.databinding.HomeBinding
import com.sanjaysgangwar.rento.model.modelClass
import com.sanjaysgangwar.rento.utils.mToast
import com.sanjaysgangwar.rento.viewHolders.homeViewHolder
import com.squareup.picasso.Picasso


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents(view)
        //makeStatusBarTransparent(activity!!)
        fetchDetailsForMyProfile()

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
        (activity as AppCompatActivity?)!!.setSupportActionBar(bind.toolbar)


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
                .setQuery(myRef.child("tenants").orderByChild("name"), modelClass::class.java)
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
                    if (position == 0) {
                        bind.noUser.visibility = View.GONE
                    }
                    holder.namE.setText(model.name)
                    holder.numbeR.setText(model.number)
                    holder.numbeR.setOnClickListener { clicked ->
                        operationToPerform(view, getRef(position).key.toString())
                    }
                    holder.namE.setOnClickListener { clicked ->
                        operationToPerform(view, getRef(position).key.toString())
                    }

                    holder.card.setOnClickListener { clicked ->
                        operationToPerform(view, getRef(position).key.toString())
                    }
                }


            }

        bind.showTenents.adapter = recyclerAdapter
        recyclerAdapter.startListening()
    }


    private fun operationToPerform(view: View?, userID: String) {
        Log.i("OnClick ", "operationToPerform: $userID")

        val action = HomeDirections.homeToTenantDetails(userID)
        view?.findNavController()?.navigate(action)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                openProfileDialog()
            }
            R.id.unitCost -> {
                openUnitCostDialog()
            }
            R.id.logOut -> {
                openDialogForConfirmation()
            }
            R.id.donate -> {
                navController.navigate(R.id.home_to_donate)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDialogForConfirmation() {
        val exit = exitConfirmation("logOut", "0", navController);
        exit.showNow(parentFragmentManager, "exit User")
    }


    private fun openProfileDialog() {
        val profile = profile()
        profile.showNow(parentFragmentManager, "add User")
    }

    private fun openUnitCostDialog() {
        val unitCost = unitCost()
        unitCost.showNow(parentFragmentManager, "add Unit")
    }

    private fun fetchDetailsForMyProfile() {
        myRef.child("profile").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild("profileUri")) {
                    Picasso.get()
                        .load(dataSnapshot.child("profileUri").value.toString())
                        .placeholder(R.drawable.icon)
                        .error(R.drawable.icon)
                        .into(bind.userImage);

                }
            }

            override fun onCancelled(error: DatabaseError) {
                mToast.errorMessageShow(
                    context?.applicationContext!!,
                    error.toException().message
                )
            }
        })
    }
}


