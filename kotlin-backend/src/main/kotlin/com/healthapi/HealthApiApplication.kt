package com.healthapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HealthApiApplication

fun main(args: Array<String>) {
    runApplication<HealthApiApplication>(*args)
}
