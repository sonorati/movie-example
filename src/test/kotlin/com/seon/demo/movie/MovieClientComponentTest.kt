package com.seon.demo.movie

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

    private fun stubCallToMovieCatalogue() {
        WireMock.stubFor(WireMock.get(urlEqualTo("/movie-catalogue/StarWars"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(" { \n" +
                                "  \"title\":\"Star Wars\",\n" +
                                "  \"director\":\"J. J. Abrams\"\n" +
                                "  }")))

    }
}