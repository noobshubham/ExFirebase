package com.noobshubham.exfirebase.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.noobshubham.exfirebase.R
import com.noobshubham.exfirebase.adapter.MessageAdapter
import com.noobshubham.exfirebase.databinding.FragmentHomeBinding
import com.noobshubham.exfirebase.models.Message


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: FirebaseFirestore
    private lateinit var messageList: ArrayList<Message>
    private lateinit var adapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        db = Firebase.firestore
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageList = arrayListOf()
        // binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        adapter = MessageAdapter(R.layout.message_card, messageList)
        binding.recyclerview.adapter = adapter
        loadMessage()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadMessage() {
        db = FirebaseFirestore.getInstance()
        db.collection("messages").get()

            .addOnSuccessListener {
                it.forEach { data ->
                    messageList.add(data.toObject<Message>())
                }
                binding.recyclerview.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_INDEFINITE).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
