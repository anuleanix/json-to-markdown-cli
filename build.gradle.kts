import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "net.leanix.converter"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.jayway.jsonpath:json-path:2.7.0")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")
    implementation("ch.qos.logback:logback-core:1.2.10")
    implementation("ch.qos.logback:logback-classic:1.2.10")
    implementation("org.slf4j:slf4j-api:1.7.36")
}

val createConfig by tasks.registering {
    val config = layout.buildDirectory.dir("config")
    outputs.dir(config)
    doLast {
        config.get().asFile.mkdirs()
        File("src/main/resources/metro-retro-config.json").copyTo(File("${config.get().asFile.absolutePath}/metro-retro-config.json"))
    }
}

distributions {
    main {
        contents {
            from(createConfig) {
                into("config")
            }
        }
    }
}


tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("net.leanix.converter.MainKt")
}
