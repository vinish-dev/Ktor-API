package com.vinish.plugins

import com.vinish.model.CreateTaskRequest
import com.vinish.model.ErrorResponse
import com.vinish.model.UpdateTaskRequest
import com.vinish.repository.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("Task not found")
                )
            }
        }

        // add task
        post("/api/tasks"){
            val request = call.receive<CreateTaskRequest>()

            if (request.title.isBlank()){
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Title cannot be blank"))
                return@post
            }

            if (request.description.isBlank()){
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Description cannot be blank"))
                return@post
            }



            val task = repository.addTask(request)
            call.respond(HttpStatusCode.Created, task)
        }

        //update existing task
        put("/api/tasks/{id}") {

            val id = call.parameters["id"]?.toIntOrNull()

            //non int id
            if (id==null){
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Invalid task ID")
                )
                return@put
            }

            val request = call.receive<UpdateTaskRequest>()

            if (request.title.isBlank()){
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Title cannot be blank"))
                return@put
            }

            if (request.description.isBlank()){
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Description cannot be blank"))
                return@put
            }


            val updatedTask = repository.updateTask(id, request)

            if (updatedTask != null){
                call.respond(updatedTask)
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("Task not found")
                )  //task id not found
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
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("Task not found")
                    )
            }
        }
    }
}