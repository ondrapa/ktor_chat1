package com.chatserver.plugins

import com.chatserver.room.RoomController
import com.chatserver.routes.chatSocket
import com.chatserver.routes.getAllMessages
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val roomController by inject<RoomController>()
    install(Routing) {
        chatSocket(roomController)
        getAllMessages(roomController)
    }
}
