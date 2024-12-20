import com.vanniktech.maven.publish.SonatypeHost

plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.30.0"
}

group = "io.github.canary-prism"
version = "3.0.3"
description = "slavacord"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.log4j.core)
    testImplementation(libs.javacord)

    compileOnly(libs.jetbrains.annotations)

    implementation(libs.discord.bridge)

    implementation(libs.apache.commons.lang)

    implementation(libs.log4j.api)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }

    withSourcesJar()
    withJavadocJar()
    modularity.inferModulePath = true
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks.javadoc {
    javadocTool = javaToolchains.javadocToolFor {
        languageVersion = JavaLanguageVersion.of(23)
    }

    (options as StandardJavadocDocletOptions).tags(
        "apiNote:a:API Note:",
        "implSpec:a:Implementation Requirements:",
        "implNote:a:Implementation Note:"
    )
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    pom {

        name = project.name
        description = project.description

        url = "https://github.com/Canary-Prism/discord-bridge"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }

        developers {
            developer {
                id = "Canary-Prism"
                name = "Canary Prism"
                email = "canaryprsn@gmail.com"
            }
        }

        scm {
            url = "https://github.com/Canary-Prism/slavacord"
            connection = "scm:git:git://github.com/Canary-Prism/slavacord.git"
            developerConnection = "scm:git:ssh://git@github.com:Canary-Prism/slavacord.git"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}