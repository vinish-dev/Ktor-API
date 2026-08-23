package com.vinish.plugins

import com.vinish.model.CreateTaskRequest
import com.vinish.model.ErrorResponse
import com.vinish.model.UpdateTaskRequest
import com.vinish.repository.TaskRepository
import com.vinish.service.TaskService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val repository = TaskRepository()
    val service: TaskService = TaskService(repository)


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
            call.respond(service.getAll())
        }

        // return the requested task
        get("/api/tasks/{id}") {

            //extract the id from request
            val id = call.parameters["id"]?.toIntOrNull()

            // for non int id
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid task Id"))
                return@get
            }

            val task = service.getById(id)

            if (task != null) {
                call.respond(task)
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("Task not found")
                )
            }
        }

        // add task
        post("/api/tasks") {
            val request = call.receive<CreateTaskRequest>()

            val task = service.create(request)

            call.respond(HttpStatusCode.Created, task)
        }

        //update existing task
        put("/api/tasks/{id}") {

            val id = call.parameters["id"]?.toIntOrNull()

            //non int id
            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Invalid task Id")
                )
                return@put
            }

            val request = call.receive<UpdateTaskRequest>()

            val updatedTask = service.update(id = id, request = request)

            if (updatedTask != null) {
                call.respond(updatedTask)
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("Task not found")
                )  //task id not found
            }
        }

        //delete task
        delete("/api/tasks/{id}") {

            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid task Id"))
                return@delete
            }

            // true if task was removed from the list
            val removed: Boolean = service.delete(id = id)

            if (removed) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("Task not found")
                )
            }
        }
    }
}