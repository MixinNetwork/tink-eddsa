plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.10"

    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lib"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.squareup.okio:okio:3.2.0")
}
