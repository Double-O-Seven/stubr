import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka") version "0.10.0"
}

repositories {
    jcenter()
    maven {
        setUrl("https://dl.bintray.com/spekframework/spek")
    }
}

val spekVersion = "2.0.9"

dependencies {
    api(project(":stubr-core"))

    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8")
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-reflect")

    testImplementation(testFixtures(project(":stubr-core")))
    testImplementation(group = "org.spekframework.spek2", name = "spek-dsl-jvm", version = spekVersion)

    testRuntimeOnly(group = "org.spekframework.spek2", name = "spek-runner-junit5", version = spekVersion)
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
}
