package com.app.vakna.modele.api

data class ApiResponse<T>(
    val statusCode: Int,
    val statusMsg: String,
    val data: T?
)