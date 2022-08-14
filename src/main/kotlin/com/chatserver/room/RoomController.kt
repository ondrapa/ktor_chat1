package com.chatserver.room

import com.chatserver.data.MessageDataSource
import com.chatserver.data.model.Message
import io.ktor.http.cio.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource
) {
    private val members = ConcurrentHashMap<String, MutableList<Member>>()

    suspend fun onJoin(
        username: String,
        chat: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        val onlineBefore = members[chat]
        if (onlineBefore != null) {
            members[chat] = (onlineBefore + Member(
                username = username,
                sessionId = sessionId,
                socket = socket
            )) as MutableList<Member>
        } else {
            members[chat] = mutableListOf(
                Member(
                    username = username,
                    sessionId = sessionId,
                    socket = socket
                )
            )
        }
    }

    suspend fun sendMessage(senderUsername: String, chat: String, message: String) {
        members[chat]?.forEach { member ->
            val messageEntity = Message(
                text = message,
                username = senderUsername,
                chat = chat,
                timestamp = System.currentTimeMillis()
            )
            messageDataSource.insertMessage(messageEntity)

            val parsedMessage = Json.encodeToString(messageEntity)
            member.socket.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun getAllMessages(chat: String): List<Message> {
        return messageDataSource.getAllMessages().filter { it.chat == chat }
    }

    fun tryDisconnect(chat: String, username: String) {
        members[chat]?.remove(members[chat]?.filter { it.username == username }?.get(0))
    }
}