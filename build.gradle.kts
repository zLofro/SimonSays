plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.3.6"
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "vermillion.productions"
version = "1.0.0-SNAPSHOT"
description = "Template with all the basics to start developing a PaperMC 1.19.3 plugin fast."

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}


repositories {
    repositories {
        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    }
    maven {
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }

    mavenCentral()
}

dependencies {
    // Paper.
    paperDevBundle("1.19.3-R0.1-SNAPSHOT")

    // Bukkit.
    implementation("net.kyori:adventure-platform-bukkit:4.1.2")

    // Paper NMS.
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")

    // ProjectLombok.
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
    shadowJar {
        relocate ("co.aikar.taskchain", "vermillion.productions.taskchain")
        relocate ("co.aikar.commands", "vermillion.productions.acf")
        relocate ("co.aikar.locales", "vermillion.productions.locales")
    }
}

bukkit {
    main = "vermillion.productions.Main"
    version = "1.0"
    apiVersion = "1.19"
    author = "VermillionTeam"
    website = "https://vermillion.productions"
}