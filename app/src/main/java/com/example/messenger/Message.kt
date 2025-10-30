package com.example.messenger

data class Message(
    val text: String,
    val senderID: String,
    val receiverID: String
) {
    constructor() : this("", "", "")
}
