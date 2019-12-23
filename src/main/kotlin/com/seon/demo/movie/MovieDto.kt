package com.seon.demo.movie

data class Movie(val title: String, val director: String)

sealed class Exception {
    data class ConnectionRefused(val code: Int, val message: String) : Exception()
    data class ResourceNotFound(val code: Int, val message: String) : Exception()
}

typealias ConnectionRefused = Exception.ConnectionRefused
typealias ResourceNotFound = Exception.ResourceNotFound
