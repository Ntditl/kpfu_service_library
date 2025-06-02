package com.library
import com.library.database.DatabaseFactory
import com.library.routes.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.callloging.*
import org.slf4j.event.Level
import io.ktor.events.*

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    // Настройка логирования
    install(CallLogging) {
        level = Level.DEBUG
    }

    // Логирование событий приложения
    environment.monitor.subscribe(ApplicationStarted) {
        log.info("Application started: ${environment.developmentMode}")
    }
    environment.monitor.subscribe(ApplicationStopped) {
        log.info("Application stopped")
    }

    install(ContentNegotiation) {
        json()
    }

    println(">>> DB URL: ${environment.config.propertyOrNull("ktor.database.url")?.getString()} ")

    install(CORS) {
        allowHost("localhost:3000", schemes = listOf("http", "https"))
        allowHost("localhost:5173", schemes = listOf("http", "https"))
        allowHost("*.ngrok-free.app", schemes = listOf("https"))
        
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("ngrok-skip-browser-warning")
        allowCredentials = true
        maxAgeInSeconds = 3600
    }

    routing {
        roleRoutes()
        bookRoutes()
        authRoutes()
        userRoutes()
        bookCopyRoutes()
        borrowingRoutes()
        borrowingsToRoleRoutes()
    }

    DatabaseFactory.init(environment)
}

// Вспомогательная функция для вывода маршрутов
fun printRoutes(route: Route, indent: String = "") {
    route.children.forEach { child ->
        val path = buildString {
            append(indent)
            append(child.toString())
        }
        println(path)
        printRoutes(child, indent + "  ")
    }
}



