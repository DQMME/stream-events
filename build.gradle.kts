val javaVersion = 18
val kSpigotVersion = "1.19.0"

plugins {
    kotlin("jvm") version "1.7.10"
    id("io.papermc.paperweight.userdev") version "1.3.8"
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("de.nycode.spigot-dependency-loader") version "1.1.2"
    kotlin("plugin.serialization") version "1.7.10"
}

group = "de.dqmme"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // PaperMC Dependency
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")

    // KSpigot dependency
    spigot("net.axay", "kspigot", kSpigotVersion)

    //Kord dependency
    spigot("dev.kord", "kord-core", "0.8.0-M15")

    //KMongo dependency
    spigot("org.litote.kmongo", "kmongo-coroutine-serialization", "4.7.0")
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xjdk-release=$javaVersion",
            )
            jvmTarget = "$javaVersion"
        }
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(javaVersion)
    }
    assemble {
        dependsOn(reobfJar)
    }

    task<Copy>("installPlugin") {
        dependsOn(jar)
        from(jar)
        include("*.jar")
        into("C:\\Users\\dqmme\\mcserv\\stream-events\\plugins")
    }
}
