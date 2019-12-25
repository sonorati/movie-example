package com.seon.demo.errorhandler

import com.seon.demo.movie.ResourceNotFound
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class CustomErrorController : ErrorController {

    @RequestMapping("/error", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun handleError(): ResponseEntity<ResourceNotFound> {
        return ResponseEntity(ResourceNotFound(message = "boom", code = 400), HttpStatus.BAD_REQUEST)
    }

    override fun getErrorPath(): String {
        return "/error"
    }
}