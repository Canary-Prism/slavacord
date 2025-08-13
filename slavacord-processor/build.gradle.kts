plugins {
    `java-library`
}

description = "annotation processor for slavacord"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(libs.discord.bridge)
    runtimeOnly(libs.discord.bridge.identity)
    implementation(project(":"))

    runtimeOnly(libs.discord4j)
    runtimeOnly(libs.javacord)
    runtimeOnly(libs.jda)
    runtimeOnly(libs.kord)



    implementation(libs.apache.commons.lang)
}

tasks.test {
    useJUnitPlatform()
}