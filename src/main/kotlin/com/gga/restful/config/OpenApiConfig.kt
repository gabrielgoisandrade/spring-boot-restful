package com.gga.restful.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * # OpenAPI
 *
 * Pode-se importar a documentação da API diretamente para o Postman (ou qualquer outra ferramenta), acessando o
 * link que fica logo abaixo do título (isso com o Swagger já aberto e customizado).
 *
 * O Postman vai criar uma collection com as informações do swagger já com todas as requisições prontas.
 *
 * @author Gabriel Gois Andrade
 * */
@Configuration
class OpenApiConfig {

    private fun info(): Info = Info()
        .version("V1")
        .description("From Rest to RestFul and glory of Rest.")
        .title("RestFul API")
        .contact(this.contact())
        .license(this.license())
        .termsOfService("http://swagger.io/terms")

    private fun license(): License = License()
        .name("Apache License 2.0")
        .url("http://springdoc.org")

    private fun contact(): Contact = Contact()
        .url("https://github.com/gabrielgoisandrade")
        .name("Gabriel Gois Andrade")
        .email("gabriel.gois.andrade14@gmail.com")

    private fun component(): Components = Components().addSecuritySchemes("Access token", this.securityScheme())

    private fun securityScheme(): SecurityScheme = SecurityScheme()
        .`in`(SecurityScheme.In.HEADER)
        .name("Authorization")
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")

    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .info(this.info())
        .components(this.component())

}