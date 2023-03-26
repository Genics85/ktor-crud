package com.genics85.core

data class APIResponse<T>(
    val systemCode: String,
    val code: String,
    val message: String,
    val data: List<T>
)
