package com.festusyuma.function.service

import com.festusyuma.function.model.Person
import org.springframework.stereotype.Service

@Service
class PersonService {

    val subscribers = mutableListOf<Person>()

    fun findAll() = subscribers

    fun create(person: Person): Person {
        subscribers.add(person)
        return person
    }
}