package de.lkrnrw.testingkotlinwithspring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class MyApp

fun main(args: Array<String>) {
    runApplication<MyApp>(*args)
}

@Table("MESSAGES")
data class Message(@Id val id: String?, val text: String)

@RestController()
@RequestMapping("/message")
class MessageController(val service: MessageService) {

    /**
     * This is a Test Documentation for a single GET call.
     */
    @GetMapping
    fun getAll(): List<Message> = service.findAll()

    @PostMapping
    fun postMessage(@RequestBody message: Message) = service.postMessage(message)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String) = service.findByID(id)

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: String) = service.deleteMessageByID(id)
}

@Service
class MessageService(private val repository: MessageRepository) {

    fun findAll(): List<Message> = repository.findAllMessages()

    fun findByID(id: String) = repository.findByIdOrNull(id)

    fun postMessage(message: Message) = repository.save(message)

    fun deleteMessageByID(id: String) = repository.deleteById(id)
}

interface MessageRepository : CrudRepository<Message, String> {

    @Query("select * from messages")
    fun findAllMessages(): List<Message>
}

