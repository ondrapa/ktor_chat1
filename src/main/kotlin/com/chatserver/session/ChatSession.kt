package com.chatserver.session

data class ChatSession(
    val username: String,
    val chat: String,
    val sessionId: String
)
