package com.vinish.service

import com.vinish.model.CreateTaskRequest
import com.vinish.model.Task
import com.vinish.model.UpdateTaskRequest
import com.vinish.repository.TaskRepository
import kotlin.jvm.Throws

class TaskService(
    val repository: TaskRepository
) {
    fun getAll(): List<Task> {
        return repository.getAll()
    }

    fun getById(id: Int): Task? {
        return repository.getById(id)
    }

    fun create(request: CreateTaskRequest): Task{
        validate(request.title, request.description)

        return repository.addTask(request)
    }

    fun update(id: Int, request: UpdateTaskRequest): Task? {
        validate(request.title, request.description)

        return repository.updateTask(id, request)
    }

    fun delete(id: Int): Boolean{
        return repository.deleteTask(id)
    }

    private fun validate(title: String, description: String){
        if(title.isBlank()){
            throw InvalidTaskException("Title cannot be blank")
        }

        if(description.isBlank()){
            throw InvalidTaskException("Description cannot be blank")
        }
    }
}