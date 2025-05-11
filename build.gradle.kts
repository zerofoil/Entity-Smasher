plugins {
    kotlin("jvm") version "1.9.22"
}

group = "Entity Smasher"
version = "1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

sourceSets {
    main {
        kotlin.srcDirs("src/EntitySmasher")
        resources.srcDirs("src")
        resources.includes.addAll(listOf("plugin.yml", "config.yml"))
    }
}

tasks.jar {
    from("src") {
        include("plugin.yml", "config.yml")
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
