package com.gga.restful.models.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel
import java.io.Serializable

/**
 * Numa dataclass, quando os argumentos são declarados com valores opcionais, um construtor vazio é criado
 *
 * Ela foi anotada com @Mapping do DozerMapper porque ela é usada na hora de converter um DTO para Model e vice-versa.
 *
 * Também foi anotada com @JvmOverloads pelo uso de argumentos com valores opcionais.
 *
 * @see [com.gga.restful.utils.ConverterUtil]
 *
 * @author *Gabriel Gois Andrade*
 * */
data class UserDTO @JvmOverloads constructor(
    @Mapping("id")
    @JsonProperty("id")
    var personId: Long = 0L,

    var name: String = "",

    var lastName: String = "",

    var email: String = "",

    var password: String = ""
) : Serializable, RepresentationModel<UserDTO>()