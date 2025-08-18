plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.34.0"
}

group = "io.github.canary-prism"
description = "A library addon for Discord Bridge that adds a more convenient syntax for creating slash commands."

val root = project

allprojects {

    apply(plugin = "java-library")
    plugins.apply("com.vanniktech.maven.publish")

    group = "io.github.canary-prism"
    version = "8.1.3"

    mavenPublishing {
        publishToMavenCentral(true)

        signAllPublications()

        pom {

            name = project.name
            description = project.description

            url = "https://github.com/Canary-Prism/slavacord"

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

    repositories {
        mavenCentral()
    }

    java {
        modularity.inferModulePath = true
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
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

    dependencies {
        testImplementation(root.libs.junit.jupiter.api)
        testImplementation(root.libs.junit.jupiter.params)
        testImplementation(platform("org.junit:junit-bom:5.11.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")

        testAnnotationProcessor(project(":slavacord-processor"))

        compileOnly(root.libs.jetbrains.annotations)
        implementation(root.libs.slf4j.api)

    }

    tasks.test {
        useJUnitPlatform()
    }


    tasks.withType<PublishToMavenRepository>().configureEach {
        notCompatibleWithConfigurationCache("Publishing tasks involve non-cacheable external interactions.")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<Javadoc> {
        options.encoding = "UTF-8"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.slf4j.simple)
    testImplementation(libs.javacord)

    api(libs.discord.bridge)

    implementation(libs.apache.commons.lang)
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