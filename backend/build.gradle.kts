plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22" // Для сериализации
    application
}

application {
    mainClass.set("com.library.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // === KTOR ===
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-server-netty:2.3.7")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("io.ktor:ktor-server-cors:2.3.7")

    // === EXPOSED ORM ===
    implementation("org.jetbrains.exposed:exposed-core:0.45.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.45.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.45.0")

    // === PostgreSQL JDBC Driver ===
    implementation("org.postgresql:postgresql:42.7.1")

    // === Kotlinx Serialization ===
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")




    implementation("com.typesafe:config:1.4.2")

    // === Logging (Logback) ===
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // === Тесты (не обязательно сейчас, но рекомендуется) ===
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-tests:2.3.7")
}

sourceSets {
    main {
        resources {
            srcDir("src/main/resources")
        }
    }
}


tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
