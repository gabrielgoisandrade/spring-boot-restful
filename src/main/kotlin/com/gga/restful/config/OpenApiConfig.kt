package com.gga.restful.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
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
 * Quando adicionado um recurso de segurança no swagger, todas as requisições ficam passíveis a receber uma autorização.
 * Para que haja uma sincronização, quando o campo de autorização principal for preenchido, fazendo com que todos os outros
 * também sejam, é necessário que a chave do componente de segurança seja igual a todos os outros.
 *
 * @sample component
 * @sample securityScheme
 *
 * @author Gabriel Gois Andrade
 * */
@Configuration
class OpenApiConfig {

    @Value("\${doc.info.name}")
    private lateinit var title: String

    @Value("\${doc.info.version}")
    private lateinit var version: String

    @Value("\${doc.info.description}")
    private lateinit var description: String

    @Value("\${doc.info.contact.url}")
    private lateinit var contactUrl: String

    @Value("\${doc.info.contact.name}")
    private lateinit var contactName: String

    @Value("\${doc.info.contact.email}")
    private lateinit var contactEmail: String

    @Value("\${doc.info.license.url}")
    private lateinit var licenseUrl: String

    @Value("\${doc.info.license.name}")
    private lateinit var licenseName: String

    @Value("\${doc.info.terms-of-service}")
    private lateinit var termsOfService: String

    @Value("\${doc.security.global-key}")
    private lateinit var securityKey: String

    @Value("\${doc.security.scheme}")
    private lateinit var securitySchema: String

    @Value("\${doc.security.bearer-format}")
    private lateinit var securityBearerFormat: String

    @Value("\${doc.security.description}")
    private lateinit var securityDescription: String

    @Value("\${doc.server.description}")
    private lateinit var serverDescription: String

    @Value("\${doc.server.url}")
    private lateinit var serverUrl: String

    private fun info(): Info = Info()
        .title(this.title)
        .description("## *${this.description}*")
        .version(this.version)
        .contact(this.contact())
        .license(this.license())
        .termsOfService(this.termsOfService)

    private fun contact(): Contact = Contact()
        .url(this.contactUrl)
        .name(this.contactName)
        .email(this.contactEmail)

    private fun license(): License = License().name(this.licenseName).url(this.licenseUrl)

    private fun component(): Components = Components().addSecuritySchemes(this.securityKey, this.securityScheme())

    private fun securityScheme(): SecurityScheme = SecurityScheme()
        .name(this.securityKey)
        .`in`(SecurityScheme.In.HEADER)
        .type(SecurityScheme.Type.HTTP)
        .scheme(this.securitySchema)
        .bearerFormat(this.securityBearerFormat)
        .description("##### ${this.securityDescription}")

    private fun server(): Server = Server()
        .description(this.serverDescription)
        .url(this.serverUrl)

    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .info(this.info())
        .components(this.component())
        .addServersItem(this.server())

}