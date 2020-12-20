package com.sanjaysgangwar.rento.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.TenantDetailsBinding
import com.sanjaysgangwar.rento.utils.mToast
import com.squareup.picasso.Picasso


class tenantDetails : Fragment(), View.OnClickListener {


    private var binding: TenantDetailsBinding? = null
    private val bind get() = binding!!
    private lateinit var userID: String
    lateinit var database: FirebaseDatabase
    val args: tenantDetailsArgs by navArgs()
    private lateinit var myRef: DatabaseReference


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

    private fun fetchBills() {

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
        database = FirebaseDatabase.getInstance()
        userID = args.userID
        myRef = database.getReference(view.resources.getString(R.string.app_name))
            .child(FirebaseAuth.getInstance().uid.toString())
        bind.fab.setOnClickListener(this)
        (activity as AppCompatActivity?)!!.setSupportActionBar(bind.toolbar)

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
        }
        return super.onOptionsItemSelected(item)
    }

}