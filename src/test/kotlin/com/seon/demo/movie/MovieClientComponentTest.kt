package com.seon.demo.movie

import arrow.core.Left
import arrow.core.Right
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureWireMock(port = 9999)
class MovieClientComponentTest {

    @Autowired
    private lateinit var movieClient: MovieClient

    @Test
    fun `should successfully call movie catalogue`() {
        stubCallToMovieCatalogue()

        val title = "Star Wars"
        val movie = Movie(title, "J. J. Abrams")
        val movieResult = movieClient.callMovieService("StarWars")

        assertThat(movieResult.isRight()).isTrue()
        AssertionsForClassTypes.assertThat(movieResult).isEqualTo(Right(movie))
    }

    @Test
    fun `should timeout after 3 seconds`() {
        stubCallToMovieCatalogue(3000)

        val errorMessage = "I/O error on GET request for \"http://localhost:9999/movie-catalogue/StarWars\": Read timed out; nested exception is java.net.SocketTimeoutException: Read timed out"
        val movie = ConnectionRefused(message = errorMessage)
        val movieResult = movieClient.callMovieService("StarWars")

        assertThat(movieResult.isLeft()).isTrue()
        AssertionsForClassTypes.assertThat(movieResult).isEqualTo(Left(movie))
    }

    private fun stubCallToMovieCatalogue(delay: Int = 0) {
        WireMock.stubFor(WireMock.get(urlEqualTo("/movie-catalogue/StarWars"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withFixedDelay(delay)
                        .withBody(" { \n" +
                                "  \"title\":\"Star Wars\",\n" +
                                "  \"director\":\"J. J. Abrams\"\n" +
                                "  }")))

    }
}