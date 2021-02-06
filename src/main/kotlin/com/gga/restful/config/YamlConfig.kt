package com.gga.restful.config

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import org.springframework.http.MediaType
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter

/**
 * # Suporte a MediaType application/x-yaml
 *
 * Configura o suporte ao MediaType YAML.
 *
 * Também faz com que valores como null seja ignorados.
 *
 * @author *Gabriel Gois Andrade*
 * */
class YamlConfig : AbstractJackson2HttpMessageConverter(
    YAMLMapper().setSerializationInclusion(NON_NULL),
    MediaType.parseMediaType("application/x-yaml")
)