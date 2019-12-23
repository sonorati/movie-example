package com.seon.demo.movie

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class MovieController(val movieClient: MovieClient) {

    @GetMapping("/greeting/{movie}")
    fun greeting(@PathVariable movie: String): ResponseEntity<Any> =
            movieClient.callMovieService(movie)
                .fold(
                    { ex -> ResponseEntity(ex, HttpStatus.FAILED_DEPENDENCY) },
                    { movieResponse -> ResponseEntity(movieResponse, HttpStatus.OK) }
                )
}