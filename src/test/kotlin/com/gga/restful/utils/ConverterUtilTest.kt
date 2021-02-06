package com.gga.restful.utils

import com.gga.restful.models.UserModel
import com.gga.restful.models.dto.UserDTO
import com.gga.restful.utils.ConverterUtil.Companion.parseListObjects
import com.gga.restful.utils.ConverterUtil.Companion.parseObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicLong

class ConverterUtilTest {

    @Test
    @DisplayName("Os valores passados no conversor devem ser os mesmo do personModel.")
    fun parseObjectTest() {

        val personModel = UserModel(1L, "Gabriel", "Gois", "SP", "Male")

        val output: UserDTO = parseObject(personModel, UserDTO::class.java)

        assertEquals(personModel.id, output.personId)
        assertEquals(personModel.name, output.name)
        assertEquals(personModel.lastName, output.lastName)
        assertEquals(personModel.email, output.email)
        assertEquals(personModel.password, output.password)
    }

    @Test
    @DisplayName("Os valores passados no conversor devem ser os mesmo do personModel.")
    fun parseListObjectTest() {
        val personModel = UserModel(1L, "Gabriel", "Gois", "SP", "Male")

        val output: List<UserDTO> = parseListObjects(personModels(), UserDTO::class.java)

        output.forEach {
            assertEquals(personModel.id, it.personId)
            assertEquals(personModel.name, it.name)
            assertEquals(personModel.lastName, it.lastName)
            assertEquals(personModel.email, it.email)
            assertEquals(personModel.password, it.password)
        }
    }

    private fun personModels(): MutableList<UserModel> {
        val atomicLong = AtomicLong()

        val userModels: MutableList<UserModel> = mutableListOf()

        val personModel = UserModel(atomicLong.incrementAndGet(), "Gabriel", "Gois", "SP", "Male")

        userModels.add(personModel)

        return userModels
    }

}