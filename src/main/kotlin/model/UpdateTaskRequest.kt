package com.vinish.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTaskRequest(
    val title:String,
    val description: String,
    val completed: Boolean
)