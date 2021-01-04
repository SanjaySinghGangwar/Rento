package com.sanjaysgangwar.rento.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.TenantDetailsBinding
import com.sanjaysgangwar.rento.functions.mSms.sendBill
import com.sanjaysgangwar.rento.model.modelClass
import com.sanjaysgangwar.rento.utils.mToast
import com.sanjaysgangwar.rento.viewHolders.billViewHolder
import com.squareup.picasso.Picasso


class tenantDetails : Fragment(), View.OnClickListener {


    private var binding: TenantDetailsBinding? = null
    private val bind get() = binding!!
    private lateinit var userID: String
    lateinit var database: FirebaseDatabase
    val args: tenantDetailsArgs by navArgs()
    private lateinit var myRef: DatabaseReference
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TenantDetailsBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents(view)
        fetchMyDetails()
        fetchBills()
    }


    private fun fetchMyDetails() {
        myRef.child("tenants").child(userID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild("name")) {
                    bind.name.setText(dataSnapshot.child("name").value.toString())
                    //bind.toolbar.title = dataSnapshot.child("name").value.toString()

                }
                if (dataSnapshot.hasChild("date")) {
                    bind.date.setText(dataSnapshot.child("date").value.toString())

                }
                if (dataSnapshot.hasChild("last_unit")) {
                    bind.lastUnit.setText(dataSnapshot.child("last_unit").value.toString())

                }
                if (dataSnapshot.hasChild("image")) {
                    Picasso.get()
                        .load(dataSnapshot.child("image").value.toString())
                        .placeholder(R.drawable.icon)
                        .error(R.drawable.icon)
                        .into(bind.userImage);

                }
                if (dataSnapshot.hasChild("number")) {
                    bind.number.setText(dataSnapshot.child("number").value.toString())

                }
                if (dataSnapshot.hasChild("rent")) {
                    bind.rent.setText(dataSnapshot.child("rent").value.toString())
                }
                if (dataSnapshot.hasChild("id_card")) {
                    bind.UID.setText(dataSnapshot.child("id_card").value.toString())
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

    private fun initAllComponents(view: View) {
        navController = Navigation.findNavController(view)
        database = FirebaseDatabase.getInstance()
        userID = args.userID
        myRef = database.getReference(view.resources.getString(R.string.app_name))
            .child(FirebaseAuth.getInstance().uid.toString())
        bind.fab.setOnClickListener(this)
        (activity as AppCompatActivity?)!!.setSupportActionBar(bind.toolbar)
        bind.bills.layoutManager = LinearLayoutManager(context)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> {
                val createBill = generateBill(
                    userID,
                    bind.rent.text.toString(),
                    bind.number.text.toString(),
                    bind.lastUnit.text.toString()
                )
                createBill.showNow(parentFragmentManager, "create Bill")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                mToast.showToastMessage(
                    context?.applicationContext!!,
                    "Tap and hold field to edit"
                )
            }
            R.id.delete -> {
                val exit = exitConfirmation("deleteUser", userID, navController);
                exit.showNow(parentFragmentManager, "Delete User")
                /*myRef.child("tenants")
                    .child(userID).removeValue().addOnCompleteListener { onComplete ->
                        if (onComplete.isSuccessful) {
                            mToast.successShowMessage(view?.context!!, "Done !!")
                            navController.navigate(R.id.tenantDetails_to_home)
                        } else {
                            mToast.errorMessageShow(
                                view?.context!!,
                                onComplete.exception?.localizedMessage
                            )
                        }

                    }*/
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchBills() {
        val option: FirebaseRecyclerOptions<modelClass> =
            FirebaseRecyclerOptions.Builder<modelClass>()
                .setQuery(
                    myRef.child("tenants").child(userID).child("bill").orderByKey(),
                    modelClass::class.java
                )
                .build()

        val recyclerAdapter =
            object : FirebaseRecyclerAdapter<modelClass, billViewHolder>(option) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): billViewHolder {
                    val view =
                        LayoutInflater.from(context)
                            .inflate(R.layout.show_bills_layout, parent, false)
                    return billViewHolder(view)
                }


                override fun onBindViewHolder(
                    holder: billViewHolder,
                    position: Int,
                    model: modelClass
                ) {
                    if (position == 0) {
                        bind.noBillYet.visibility = GONE
                    }
                    holder.month.text = getRef(position).key.toString()
                    holder.perUnitCost.text = model.perUnitCst + "/-"
                    holder.rentAmount.text = model.rent + "/-"
                    holder.electrictyAmount.text = model.electricity + "/-"
                    holder.totalBill.text = "Total : " + model.total + "/-"
                    holder.unitUsed.text = model.unitUsed
                    holder.sendBill.setOnClickListener { sendSms ->
                        operationToPerform(
                            view?.context,
                            getRef(
                                position
                            ).key.toString(),
                            model.rent + "/-",
                            model.electricity + "/-",
                            model.unitUsed,
                            model.perUnitCst + "/-", model.total
                        )
                    }
                }


            }

        bind.bills.adapter = recyclerAdapter
        recyclerAdapter.startListening()

    }

    private fun operationToPerform(
        context: Context?,
        month: String,
        rent: String,
        electricity: String,
        unitUsed: String,
        perUnitCost: String,
        total: String
    ) {
        sendBill(
            context!!,
            month,
            total,
            rent,
            electricity,
            unitUsed,
            perUnitCost
        )
    }

}