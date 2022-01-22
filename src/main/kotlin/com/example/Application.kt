package com.example

import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import com.example.plugins.configureSockets
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database

fun main() {
    val port = Integer.parseInt(System.getenv("PORT"))
    Database.connect(
        "jdbc:postgresql://ec2-54-204-128-96.compute-1.amazonaws.com:5432/d43qcb6atqt206?sslmode=require",
        driver = "org.postgresql.Driver",
        user = "zpikpxknmoscgo",
        password ="7d2459c6a2a75d029f3ecaf7b4564bbf0114f96083a4e504886771cb212c344f"
    )

    embeddedServer(Netty, port = port) {
install(ContentNegotiation) {
    json()

}
        configureRouting()

        configureSerialization()
        configureSockets()
    }.start(wait = true)
}


