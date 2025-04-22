package com.library
import com.library.database.DatabaseFactory
import com.library.routes.roleRoutes
import com.library.routes.bookRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)



fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    println(">>> DB URL: ${environment.config.propertyOrNull("ktor.database.url")?.getString()} ")

    install(CORS) {
        anyHost() // ОК для разработки
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
        allowHeader("ngrok-skip-browser-warning") // 👈 ДОБАВЬ ЭТО
    }

    routing {
        roleRoutes()
        bookRoutes()
    }

    DatabaseFactory.init(environment)
}




