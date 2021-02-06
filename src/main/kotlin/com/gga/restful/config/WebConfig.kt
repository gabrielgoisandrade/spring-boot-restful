package com.gga.restful.config

import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.support.WebStack
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_XML
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Faz com que o servidor seja capaz de retornar e receber mais de um tipo de dado, além do JSON
 * */
@Configuration
class WebConfig : WebMvcConfigurer {

    companion object {
        private val MEDIA_TYPE_YAML: MediaType = MediaType.valueOf("application/x-yaml")
    }

    // content negotiation para yaml [FUNCIONA]
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(YamlConfig())
    }

    // content negotiation via header (Accept) [FUNCIONOU]
    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer.favorParameter(false)
            .ignoreAcceptHeader(false) // não ignora o cabeçalho da request
            .useRegisteredExtensionsOnly(false)
            .defaultContentType(APPLICATION_JSON) // o conteúdo padrão é JSON
            .mediaType("json", APPLICATION_JSON)
            .mediaType("xml", APPLICATION_XML)
            .mediaType("yaml", MEDIA_TYPE_YAML)
    }

    /*
    // content negotiation via extension [NÃO FUNCIONOU]
    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
         configurer.favorParameter(false)
             .ignoreAcceptHeader(false) // não ignora o cabeçalho da request
             .defaultContentType(APPLICATION_JSON) // o conteúdo padrão é JSON
             .mediaType("json", APPLICATION_JSON)
             .mediaType("xml", APPLICATION_XML)
     }
     */

    /*
    // content negotiation via query parameter [FUNCIONOU]
    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
         configurer.favorParameter(true)
             .parameterName("mediaType")
             .ignoreAcceptHeader(true) // não ignora o cabeçalho da request
             .useRegisteredExtensionsOnly(false)
             .defaultContentType(APPLICATION_JSON) // o conteúdo padrão é JSON
             .mediaType("json", APPLICATION_JSON)
             .mediaType("xml", APPLICATION_XML)
     }
     */

}