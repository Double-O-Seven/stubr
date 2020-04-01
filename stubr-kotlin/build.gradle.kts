import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `java-test-fixtures`
    jacoco
    `maven-publish`
    kotlin("jvm")
    id("org.jetbrains.dokka") version "0.10.0"
}

repositories {
    jcenter()
    maven {
        setUrl("https://dl.bintray.com/spekframework/spek")
    }
}

dependencies {
    api(project(":stubr-core"))

    compileOnly(kotlin("reflect"))
    compileOnly(kotlin("stdlib-jdk8"))

    testImplementation(testFixtures(project(":stubr-core")))
    testImplementation(kotlin("reflect"))
    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation(group = "org.spekframework.spek2", name = "spek-dsl-jvm")

    testRuntimeOnly(group = "org.spekframework.spek2", name = "spek-runner-junit5")
}

tasks {
    withType(KotlinCompile::class) {
        sourceCompatibility = "1.8"
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=compatibility")
        }
    }

    test {
        useJUnitPlatform {
            includeEngines("spek2", "junit-jupiter")
        }
    }

    withType(Jar::class).named("javadocJar") {
        from(dokka)
    }
}
