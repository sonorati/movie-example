package com.seon.demo.movie

import arrow.core.Left
import arrow.core.Right
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(MovieController::class)
class MovieControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockBean
    private lateinit var movieClient: MovieClient

    @Test
    fun `should get 200 and expected movie when calling movies endpoint`() {
        val title = "StarWars"
        val movie = Movie("Star Wars", "J. J. Abrams")

        `when`(movieClient.callMovieService(title)).thenReturn(Right(movie))

        mvc.perform(MockMvcRequestBuilders.get("/movies/StarWars"))
                .andExpect(status().is2xxSuccessful)
                .andExpect(jsonPath("title").value("Star Wars"))

    }

    @Test
    fun `should get 404 when calling with unknown movie`() {
        val title = "Alien"

        `when`(movieClient.callMovieService(title)).thenReturn(Left(ResourceNotFound(404, "not found")))

        val mvcResponse = mvc.perform(MockMvcRequestBuilders.get("/movies/Alien"))
                .andExpect(status().isNotFound)
                .andReturn()

        assertThat(mvcResponse.response.contentAsString).isEqualTo("{\"code\":404,\"message\":\"not found\"}")
    }

    @Test
    fun `should get 424 when movieCatalogue not available`() {
        val title = "Alien"

        `when`(movieClient.callMovieService(title)).thenReturn(Left(ConnectionRefused(424, "boom")))

        val mvcResponse = mvc.perform(MockMvcRequestBuilders.get("/movies/Alien"))
                .andExpect(status().isFailedDependency)
                .andReturn()

        assertThat(mvcResponse.response.contentAsString).isEqualTo("{\"code\":424,\"message\":\"boom\"}")
    }

}