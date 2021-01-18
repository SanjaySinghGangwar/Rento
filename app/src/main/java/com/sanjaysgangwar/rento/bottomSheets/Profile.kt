package com.sanjaysgangwar.rento.bottomSheets

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sanjaysgangwar.rento.R
import com.sanjaysgangwar.rento.databinding.ProfileBinding
import com.sanjaysgangwar.rento.utils.mToast
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class profile : BottomSheetDialogFragment(), View.OnClickListener {
    private var binding: ProfileBinding? = null
    private val bind get() = binding!!
    lateinit var mStorageRef: StorageReference
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference


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
        fetchDetailsForProfile()
    }


    private fun initAllComponents(view: View) {
        bind.name.setOnClickListener(this)
        bind.profileImage.setOnClickListener(this)
        bind.add.setOnClickListener(this)
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference(view.resources.getString(R.string.app_name))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add -> {
                if (bind.editName.visibility == GONE) {
                    bind.editName.visibility = View.VISIBLE
                } else {
                    sendNameToDatabase(bind.editName.text.toString())
                    bind.editName.visibility = GONE
                    //mToast.checker(context?.applicationContext!!)
                }
            }
            R.id.profileImage -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 100)
            }
            R.id.name -> {
                if (bind.editName.visibility == GONE) {
                    bind.editName.visibility = View.VISIBLE
                } else {
                    sendNameToDatabase(bind.editName.text.toString())
                    bind.editName.visibility = GONE
                }
            }
        }
    }

    private fun sendNameToDatabase(newName: String) {
        myRef.child(FirebaseAuth.getInstance().uid.toString())
            .child("profile").child("name")
            .setValue(newName)
            .addOnCompleteListener { sendToDatabase ->
                if (sendToDatabase.isSuccessful) {
                    hideProgressDialog()
                    mToast.successShowMessage(
                        context?.applicationContext!!,
                        "ADDED !!"
                    )
                    dismiss()
                } else {
                    hideProgressDialog()
                    mToast.errorMessageShow(
                        context?.applicationContext!!,
                        sendToDatabase.exception?.localizedMessage.toString()
                    )
                }
            }
    }

    private fun hideProgressDialog() {
        dismiss()
        bind.ProgressBar.root.visibility = View.GONE
        bind.add.visibility = View.VISIBLE
    }

    private fun showProgressDialog() {
        bind.ProgressBar.root.visibility = View.VISIBLE
        bind.add.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                showProgressDialog()
                val uri: Uri = intent?.data!!
                val random = Random()
                val timestamp: String =
                    SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) + random.nextInt(400)
                val firebaseStorageLocation: StorageReference =
                    mStorageRef.child(view?.resources?.getString(R.string.app_name).toString())
                        .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                        .child("imagesProfile/")
                        .child(timestamp)

                firebaseStorageLocation.putFile(uri).addOnCompleteListener { uploadTask ->
                    if (uploadTask.isSuccessful) {
                        firebaseStorageLocation.downloadUrl.addOnSuccessListener { usri ->
                            val myuri = usri.toString()
                            sendToDatabase(myuri)
                        }


                    } else {
                        hideProgressDialog()
                        mToast.errorMessageShow(
                            context?.applicationContext!!,
                            uploadTask.exception?.localizedMessage + uploadTask.exception?.stackTrace
                        )
                    }

                }
            }


        } else {
            mToast.errorShow(context?.applicationContext!!)
        }
    }

    private fun sendToDatabase(myuri: String) {
        myRef.child(FirebaseAuth.getInstance().uid.toString())
            .child("profile").child("profileUri")
            .setValue(myuri)
            .addOnCompleteListener { sendToDatabase ->
                if (sendToDatabase.isSuccessful) {
                    hideProgressDialog()
                    mToast.successShowMessage(
                        context?.applicationContext!!,
                        "ADDED !!"
                    )
                    dismiss()
                } else {
                    hideProgressDialog()
                    mToast.errorMessageShow(
                        context?.applicationContext!!,
                        sendToDatabase.exception?.localizedMessage.toString()
                    )
                }
            }
    }

    private fun fetchDetailsForProfile() {
        myRef.child(FirebaseAuth.getInstance().uid.toString())
            .child("profile").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.hasChild("profileUri")) {
                        Picasso.get()
                            .load(dataSnapshot.child("profileUri").value.toString())
                            .placeholder(R.drawable.icon)
                            .error(R.drawable.icon)
                            .into(bind.profileImage);
                    }
                    if (dataSnapshot.hasChild("name")) {
                        bind.name.setText(dataSnapshot.child("name").value.toString())
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