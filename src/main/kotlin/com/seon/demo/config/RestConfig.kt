package com.seon.demo.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration.ofSeconds


@Configuration
class RestConfig {

    @Value("\${custom.rest.connection.connect-timeout}")
    lateinit var connectTimeout: String

    @Value("\${custom.rest.connection.read-timeout}")
    lateinit var readTimeout: String

    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
        return restTemplateBuilder
                .setConnectTimeout(ofSeconds(connectTimeout.toLong()))
                .setReadTimeout(ofSeconds(readTimeout.toLong()))
                .build()
    }

}