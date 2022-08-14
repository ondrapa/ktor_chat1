package com.chatserver.plugins

import com.chatserver.session.ChatSession
import io.ktor.sessions.*
import io.ktor.application.*
import io.ktor.util.*

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<ChatSession>("SESSION")
    }

    intercept(ApplicationCallPipeline.Features) {
        if(call.sessions.get<ChatSession>() == null) {
            val data = call.parameters["username"] ?: "Guest*All"
            val betterData = data.split("*")
            call.sessions.set(ChatSession(betterData[0],betterData[1], generateNonce()))
        }
    }
}
