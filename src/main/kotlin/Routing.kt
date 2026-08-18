package com.vinish

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val tasks = mutableListOf<Task>(
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

        // default
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

        // return the requested task
        get("/api/tasks/{id}"){

            //extract the id from request
            val id = call.parameters["id"]?.toIntOrNull()

            val task = tasks.find { it.id == id }

            if (task != null){
                call.respond(task)
            } else{
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // receive task from client
        post("/api/tasks"){
            val task = call.receive<Task>()

            tasks.add(task)
            call.respond(HttpStatusCode.Created, task)
        }

    }
}