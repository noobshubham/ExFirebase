package com.noobshubham.exfirebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noobshubham.exfirebase.R
import com.noobshubham.exfirebase.models.Message


class MessageAdapter(
    private val layout: Int,
    private val dataset: List<Message>
) : RecyclerView.Adapter<MessageAdapter.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val username: TextView = view.findViewById(R.id.username)
        private val msg: TextView = view.findViewById(R.id.message)

        fun bind(message: Message) {
            username.text = message.name
            msg.text = message.text

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        // create a new view
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return Holder(adapterLayout)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val message = dataset[position]
        holder.bind(message)
    }

    override fun getItemCount() = dataset.size
}
