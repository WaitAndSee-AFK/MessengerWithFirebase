package com.example.messenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

private const val VIEW_TYPE_MY_MESSAGE = 100
private const val VIEW_TYPE_OTHER_MESSAGE = 101

class MessagesAdapter(val currentUserID: String) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {
    private var messages: List<Message> = mutableListOf<Message>()

    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = messages.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        val layoutResID = when(viewType) {
            VIEW_TYPE_MY_MESSAGE -> R.layout.my_message_item
            VIEW_TYPE_OTHER_MESSAGE -> R.layout.other_message_item
            else -> throw IllegalArgumentException("Неудовлетворительный view type")
        }
        val view: View = LayoutInflater.from(parent.context).inflate(
            layoutResID,
            parent,
            false
        )
        return MessageViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        if (message.senderID.equals(currentUserID)) {
            return VIEW_TYPE_MY_MESSAGE
        } else {
            return VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onBindViewHolder(
        holder: MessageViewHolder,
        position: Int
    ) {
        val message = messages[position]
        holder.textViewMessage.text = message.text
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewMessage: TextView = itemView.findViewById(R.id.textViewMessage)
    }
}