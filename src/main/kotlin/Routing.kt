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

        // receive task from client and add it to task list
        post("/api/tasks"){
            val task = call.receive<Task>()

            tasks.add(task)
            call.respond(HttpStatusCode.Created, task)
        }

        //update existing task
        put("/api/tasks/{id}") {

            val id = call.parameters["id"]?.toIntOrNull()
            val updatedTask = call.receive<Task>()

            // find index: id 1 is index 0
            val index = tasks.indexOfFirst { it.id == id }

            if (index != -1){
                tasks[index] = updatedTask
                call.respond(updatedTask)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        delete("/api/tasks/{id}"){

            val id = call.parameters["id"]?.toIntOrNull()

            // true if task was removed from the list
            val removed: Boolean = tasks.removeIf { it.id == id }

            if (removed){
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}