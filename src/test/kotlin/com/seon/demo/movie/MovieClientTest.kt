package com.seon.demo.movie

import arrow.core.Left
import arrow.core.Right
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@ExtendWith(MockitoExtension::class)
class MovieClientTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    @InjectMocks
    private lateinit var movieClient: MovieClient


    @Test
    fun `should get the movie by name`() {
        val title = "StarWars"
        val movie = Movie(title, "J. J. Abrams")
        val url = "http://localhost:9999/movies/$title"
        `when`(restTemplate.getForEntity(url, Movie::class.java)).thenReturn(ResponseEntity(movie, HttpStatus.OK))

        val movieResult = movieClient.callMovieService(title)

        assertThat(movieResult.isRight()).isTrue()
        assertThat(movieResult).isEqualTo(Right(movie))
    }

    @Test
    fun `should get resource not found`() {
        val title = "UnknownMovie"
        val url = "http://localhost:9999/movies/$title"
        val notFoundEx = HttpClientErrorException(HttpStatus.NOT_FOUND, "not found")
        `when`(restTemplate.getForEntity(url, Movie::class.java)).thenThrow(notFoundEx)

        val movieResult = movieClient.callMovieService(title)

        assertThat(movieResult.isLeft()).isTrue()
        assertThat(movieResult).isEqualTo(Left(ResourceNotFound(404, "not found")))
    }

    @Test
    fun `should get connection refused when 3rd party service is down`() {
        val title = "Alien"
        val url = "http://localhost:9999/movies/$title"
        val connectionDownEx = RestClientException("Connection refused")
        `when`(restTemplate.getForEntity(url, Movie::class.java)).thenThrow(connectionDownEx)

        val movieResult = movieClient.callMovieService(title)

        assertThat(movieResult.isLeft()).isTrue()
        assertThat(movieResult).isEqualTo(Left(ConnectionRefused(424, "Connection refused")))
    }
}