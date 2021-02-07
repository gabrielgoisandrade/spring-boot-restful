package com.gga.restful.services

import com.gga.restful.errors.exceptions.FileException
import com.gga.restful.errors.exceptions.FileNotFoundException
import com.gga.restful.models.dto.FileDTO
import com.gga.restful.utils.FileUtil.Companion.getPath
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils.cleanPath
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

@Service
class FileService {

    @Value("\${file.upload-dir}")
    private lateinit var path: String

    /**
     * # Armazenamento de arquivos
     *
     * Salva os arquivos enviados durante a requisição (nesse caso, num diretório específico).
     *
     * @see findFile
     * @param file arquivo a ser salvo
     * @throws FileException Erro caso não consiga salvar
     * @return FileDTO com as informações do arquivo
     * @author Gabriel Gois Andrade
     * */
    @Throws(FileException::class)
    fun storeFile(file: MultipartFile): FileDTO =
        try {
            val fileName: String = cleanPath(file.originalFilename!!)

            if (fileName.contains("..")) throw FileException("Invalid path sequence.")

            val `in`: InputStream = file.inputStream // local a ser armazenado

            val targetLocation: Path = getPath(this.path, fileName) // caminho do local

            val copyOption: StandardCopyOption = REPLACE_EXISTING // substitui o arquivo já existente

            Files.copy(`in`, targetLocation, copyOption)

            FileDTO(fileName, file.contentType!!, file.size)
        } catch (e: IOException) {
            throw FileException(e.message!!)
        }

    /**
     * # Busca do arquivo armazenado
     *
     * Busca o arquivo armazenado para que possa ser baixado
     *
     * @see storeFile
     * @param fileName arquivo a ser buscado
     * @throws FileNotFoundException Erro caso não encontre o arquivo
     * @return arquivo a ser visualizado
     * @author Gabriel Gois Andrade
     * */
    @Throws(FileNotFoundException::class)
    fun findFile(fileName: String): Resource =
        try {
            val uri: URI = getPath(this.path, fileName).toUri()

            val resource: Resource = UrlResource(uri)

            if (resource.exists()) resource else throw FileNotFoundException("File not found.")
        } catch (e: IOException) {
            throw FileNotFoundException(e.message!!)
        }

}