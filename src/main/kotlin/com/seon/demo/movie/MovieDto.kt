package com.seon.demo.movie

data class Movie(val title: String, val director: String)

sealed class Exception {
    abstract val code: Int
    data class ConnectionRefused(override val code: Int = 424, val message: String) : Exception()
    data class ResourceNotFound(override val code: Int = 404, val message: String) : Exception()
}

typealias ConnectionRefused = Exception.ConnectionRefused
typealias ResourceNotFound = Exception.ResourceNotFound
