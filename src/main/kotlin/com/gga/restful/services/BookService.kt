package com.gga.restful.services

import com.gga.restful.errors.exceptions.BookNotFoundException
import com.gga.restful.models.BookModel
import com.gga.restful.models.dto.BookDTO
import com.gga.restful.repositories.BookRepository
import com.gga.restful.utils.ConverterUtil.Companion.parseObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService {

    @Autowired
    private lateinit var repository: BookRepository

    fun findById(id: Long): BookDTO = this.repository.findById(id).orElseThrow {
        BookNotFoundException("Book not found.")
    }.run {
        parseObject(this, BookDTO::class.java)
    }

    fun findAll(pageable: Pageable): Page<BookDTO> =
        this.repository.findAll(pageable).run {
            this.map { parseObject(it, BookDTO::class.java) }
        }

    @Transactional
    fun createBook(BookDTO: BookDTO): BookDTO {
        val entity: BookModel = parseObject(BookDTO, BookModel::class.java)

        this.repository.save(entity).run {
            return parseObject(this, BookDTO::class.java)
        }
    }

    @Transactional
    fun updateBook(BookDTO: BookDTO): BookDTO {
        // Houve outra chamada do findById porque a conexão com o database fecha a cada vez que um comando é executado.
        val currentBook: BookModel = this.repository.findById(BookDTO.bookId).orElseThrow {
            BookNotFoundException("Book not found.")
        }

        val newBook: BookModel = currentBook.apply {
            with(BookDTO) {
                this@apply.author = this.author
                this@apply.title = this.title
                this@apply.price = this.price
                this@apply.launchDate = this.launchDate
            }
        }

        this.repository.save(newBook).run {
            return parseObject(this, BookDTO::class.java)
        }

    }

    @Transactional
    fun deleteBook(id: Long): Unit = this.findById(id).run { this@BookService.repository.deleteById(this.bookId) }

}