package com.vinish.plugins

import com.vinish.repository.TaskRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.vinish.model.Task

fun Application.configureRouting() {

val repository = TaskRepository()

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
            call.respond(repository.getAll())
        }

        // return the requested task
        get("/api/tasks/{id}"){

            //extract the id from request
            val id = call.parameters["id"]?.toIntOrNull()

            val task = repository.getById(id ?: -1)

            if (task != null){
                call.respond(task)
            } else{
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // add task
        post("/api/tasks"){
            val task = call.receive<Task>()

            repository.addTask(task)
            call.respond(HttpStatusCode.Created, task)
        }

        //update existing task
        put("/api/tasks/{id}") {

            val id = call.parameters["id"]?.toIntOrNull()
            val updatedTask = call.receive<Task>()

            val index = repository.updateTask(id ?: -1, updatedTask)

            if (index != null){
                call.respond(updatedTask)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        //delete task
        delete("/api/tasks/{id}"){

            val id = call.parameters["id"]?.toIntOrNull()

            // true if task was removed from the list
            val removed: Boolean = repository.deleteTask(id ?: -1)

            if (removed){
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}