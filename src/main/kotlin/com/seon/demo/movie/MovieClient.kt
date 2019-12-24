package com.seon.demo.movie

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Component
class MovieClient(private val restTemplate: RestTemplate) {

    @Value("\${movie-catalogue.base-url}")
    lateinit var movieUrl: String

    fun callMovieService(movie: String): Either<Exception, Movie> =
        try {
            val url = "$movieUrl/movie-catalogue/$movie"
            val response = restTemplate.getForEntity(url, Movie::class.java)
            Right(response.body!!)
        } catch (notFoundEx: HttpClientErrorException) {
            Left(ResourceNotFound(message = notFoundEx.statusText, code = notFoundEx.rawStatusCode))
        } catch (e: RestClientException) {
            Left(ConnectionRefused(message = e.message ?: "Downstream error", code = 424))
        }
}