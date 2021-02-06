package com.gga.restful.utils

import com.github.dozermapper.core.DozerBeanMapperBuilder
import com.github.dozermapper.core.Mapper
import java.util.stream.Collectors

/**
 * ## Realiza a conversão de um objeto do tipo VO (ou DTO) para MODEL e vice-versa
 *
 * O que irá acontecer é que: os valores do objeto de origem serão copiados para o objeto de destino.
 * Para isso, é necessário que os atributos de ambos os objetos tenham o mesmo tipo e nome.
 *
 * Caso isso não aconteça, a classe que contém um atributo com nome diferente, precisa que o mesmo seja
 * anotado com @Mapping, passando como argumento o nome que deverá ser considerado durante o mapeamento.
 *
 * @author *Gabriel Gois Andrade*
 * */
class ConverterUtil private constructor() {
    companion object {
        private val mapper: Mapper = DozerBeanMapperBuilder.buildDefault()

        @JvmStatic
        fun <O, D> parseObject(origin: O, destination: Class<D>): D = mapper.map(origin, destination)

        @JvmStatic
        fun <O, D> parseListObjects(origin: List<O>, destination: Class<D>): List<D> =
            origin.stream().map { this.mapper.map(it, destination) }.collect(Collectors.toList())

    }
}