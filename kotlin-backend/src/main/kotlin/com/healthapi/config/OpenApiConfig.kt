package com.healthapi.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(apiInfo())
            .servers(listOf(
                Server().url("http://localhost:8080").description("Local Development Server")
            ))
            .components(Components())
    }

    private fun apiInfo(): Info {
        return Info()
            .title("Health API")
            .description("RESTful API for managing patients and test results")
            .version("1.0.0")
            .contact(
                Contact()
                    .name("Health API Team")
                    .email("support@healthapi.com")
                    .url("https://healthapi.com")
            )
            .license(
                License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")
            )
    }
}
