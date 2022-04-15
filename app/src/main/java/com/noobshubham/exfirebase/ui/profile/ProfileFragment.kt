package com.noobshubham.exfirebase.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.noobshubham.exfirebase.databinding.FragmentProfileBinding
import java.util.*
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
import com.noobshubham.exfirebase.R

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val REQUEST_IMAGE_GET = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        db = Firebase.firestore
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileImage.setOnClickListener {
//            val dialog = SelectorDialogFragment()
//            dialog.show(childFragmentManager, "choose")
            selectImage()
        }

        var username: String
        binding.profileName.text = auth.currentUser?.displayName.toString()
        binding.profileEmail.editText?.setText(auth.currentUser?.email.toString())

        // Gender
        val items = listOf("MALE", "FEMALE")
        val adapter = ArrayAdapter(requireContext(), R.layout.gender_list, items)
        (binding.genderMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.saveButton.setOnClickListener {
            Snackbar.make(binding.root, "Button Logic Haven't Completed Yet!", Snackbar.LENGTH_LONG)
                .show()
        }
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
                val fullPhotoUri: Uri? = data?.data
                Glide.with(requireActivity()).load(fullPhotoUri).into(binding.profileImage)
            } else {
                Snackbar.make(binding.root, "Failed to Set the Image.", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    class SelectorDialogFragment : DialogFragment() {
        // TODO
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
