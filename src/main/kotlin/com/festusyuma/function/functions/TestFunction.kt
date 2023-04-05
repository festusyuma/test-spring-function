package com.festusyuma.function.functions

import com.festusyuma.function.model.Person
import com.festusyuma.function.service.PersonService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestFunction(private val personService: PersonService) {

    @Bean
    fun findAll(): () -> List<Person> = { personService.findAll() }

    @Bean
    fun create(): (person: Person) -> Person = {
        personService.create(it)
    }
}