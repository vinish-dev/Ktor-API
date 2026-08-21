package com.vinish.repository

import com.vinish.model.Task

class TaskRepository {

    // task list
    private val tasks = mutableListOf<Task>(
        Task(
            id = 1,
            title = "Learn Ktor",
            description = "Build a CRUD API",
            completed = false
        ),
        Task(
            id = 2,
            title = "Build compose app",
            description = "Connect it to Ktor server",
            completed = false
        )
    )

    // get all task
    fun getAll() = tasks

    // get task by id
    fun getById(id: Int): Task? {
        return tasks.find { it.id == id }
    }

    // add task
    fun addTask(task: Task): Task {
        tasks.add(task)
        return task
    }

    fun updateTask(id: Int, task: Task): Task?{
        val index = tasks.indexOfFirst { it.id == id }  //find index of the task in list

        if (index == -1){
            return null
        }

        tasks[index] = task //update
        return task
    }

    fun deleteTask(id: Int): Boolean{
       return tasks.removeIf { it.id == id}  // returns true if element was removed
    }

}