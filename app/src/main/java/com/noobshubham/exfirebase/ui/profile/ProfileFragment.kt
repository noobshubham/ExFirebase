package com.noobshubham.exfirebase.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.noobshubham.exfirebase.databinding.FragmentProfileBinding
import java.util.*
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.noobshubham.exfirebase.R
import com.noobshubham.exfirebase.models.Profile

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private var photoUri: Uri? = null
    private var imageUrl: String? = null
    private val REQUEST_IMAGE_GET = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        db = Firebase.firestore
        auth = Firebase.auth
        storage = Firebase.storage
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileImage.setOnClickListener {
            selectImage()
        }

        binding.profileName.text = auth.currentUser?.displayName.toString()
        binding.profileEmail.editText?.setText(auth.currentUser?.email.toString())

        // date of birth code goes here
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            binding.profileBirthday.editText?.setText("$dayOfMonth/$month/$year")
        }
        binding.profileBirthday.setEndIconOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // select Gender
        val items = listOf("MALE", "FEMALE")
        val adapter = ArrayAdapter(requireContext(), R.layout.gender_list, items)
        (binding.genderMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.saveButton.setOnClickListener {
            Snackbar.make(binding.root, "Button Logic Haven't Completed Yet!", Snackbar.LENGTH_LONG)
                .show()
        }

        // update profile on button click
        binding.saveButton.setOnClickListener { updateProfile() }
    }

    private fun uploadImage() {
        val imageName = "dp/" + auth.currentUser?.displayName + ".jpg"
        val imageRef = storage.reference.child(imageName)
        photoUri?.let {
            imageRef.putFile(it)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { task ->
                        imageUrl = task.toString()
                        uploadDetails()
                    }
                }
        }
            ?.addOnFailureListener { exception ->
                Snackbar.make(
                    binding.root,
                    exception.message.toString(),
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }?.addOnProgressListener { task ->
                val percentage = task.bytesTransferred / task.totalByteCount
                Log.d("progress value", "${percentage}%")
            }
    }

    private fun uploadDetails() {
        val user = auth.currentUser
        if (user != null) {
            val bio = binding.bioEditText.editText?.text.toString()
            val dob = binding.profileBirthday.editText?.text.toString()
            val gender = binding.genderMenu.editText?.text.toString()
            db.collection("Users").document(user.uid).set(
                Profile(
                    user.email.toString(),
                    user.uid,
                    dob,
                    gender,
                    bio,
                    imageUrl!!
                )
            ).addOnFailureListener {
                Snackbar.make(
                    binding.root,
                    it.message.toString(),
                    Snackbar.LENGTH_LONG
                ).show()
            }.addOnSuccessListener {
                Snackbar.make(
                    binding.root,
                    "Details Updated!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun updateProfile() {
        uploadImage()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET) {
            if (resultCode == Activity.RESULT_OK) {
                photoUri = data?.data
                Glide.with(requireActivity()).load(photoUri).into(binding.profileImage)
            } else {
                Snackbar.make(binding.root, "Failed to Set the Image.", Snackbar.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
