package com.gga.restful.services

import com.gga.restful.errors.exceptions.FileNotFoundException
import com.gga.restful.errors.exceptions.FileStorageException
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils.cleanPath
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileStorageService {

    @Value("\${file.upload-dir}")
    private lateinit var path: String

    fun storeFile(file: MultipartFile): String {
        val fileName: String = cleanPath(file.originalFilename!!)

        try {
            if (fileName.contains("..")) throw FileStorageException("Invalid path sequence.")

            val inputStream: InputStream = file.inputStream

            val targetLocation: Path = Paths.get(path + fileName)

            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)

            return fileName
        } catch (e: IOException) {
            throw FileStorageException(e.message!!)
        }
    }

    fun loadFileAsResource(fileName: String): Resource {
        try {
            val filePath: Path = Paths.get(path + fileName)

            val resource: Resource = UrlResource(filePath.toUri())

            if (resource.exists())
                return resource

            throw FileNotFoundException("File not found.")

        } catch (e: IOException) {
            throw FileNotFoundException(e.message!!)
        }
    }

}