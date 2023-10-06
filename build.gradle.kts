plugins {
    id ("java")
    id("io.qameta.allure") version "2.11.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("com.codeborne:selenide:6.18.0")
    testImplementation("com.github.romankh3:image-comparison:4.4.0")
    testImplementation("io.qameta.allure:allure-junit5:2.24.0")


}

tasks.test {
    useJUnitPlatform()
}