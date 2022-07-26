plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.10"

    `java-library`
    `maven-publish`
    `application`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.squareup.okio:okio:3.2.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("com.google.code.gson:gson:2.9.0")
}

group = "com.github.MixinNetwork"
version = "0.0.1"

tasks.jar {
    archiveBaseName.set("tink-eddsa")
}




