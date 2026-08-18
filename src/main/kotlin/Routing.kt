package com.vinish

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val tasks = listOf<Task>(
        Task(
            id = 1,
            title = "Learn Ktor",
            description = "Build a CRUD API",
            completed = false
        ),
        Task(
            id = 2,
            title = "Build compose app",
            description = "Connect it to Ktor srever",
            completed = false
        )
    )

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }

        //my end points

        //return all tasks
        get("/api/tasks") {
            call.respond(tasks)
        }

    }
}