package com.valloo.demo.nationalparks.infra.http

data class ApiResponse<T>(
    val total: Int,
    val limit: Int,
    val start: Int,
    val data: List<T>
)