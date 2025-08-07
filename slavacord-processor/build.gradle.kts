plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(libs.discord.bridge)
    implementation(project(":"))

    implementation(libs.apache.commons.lang)
}

tasks.test {
    useJUnitPlatform()
}