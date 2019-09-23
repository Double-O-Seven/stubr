plugins {
    `java-library`
    kotlin("jvm") version "1.3.50"
}

val junitVersion by extra { "5.5.1" }
val kotlinVersion by extra { "1.3.50" }
val spekVersion by extra { "2.0.7" }

dependencies {
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = kotlinVersion)

    testImplementation(group = "io.mockk", name = "mockk", version = "1.9.3")
    testImplementation(group = "org.assertj", name = "assertj-core", version = "3.13.2")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitVersion)
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-params", version = junitVersion)
    testImplementation(group = "org.spekframework.spek2", name = "spek-dsl-jvm", version = spekVersion)

    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = junitVersion)
    testRuntimeOnly(group = "org.spekframework.spek2", name = "spek-runner-junit5", version = spekVersion)
}

tasks {
    compileKotlin {
        sourceCompatibility = "1.8"
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=compatibility")
        }
    }

    compileTestKotlin {
        sourceCompatibility = "1.8"
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=compatibility")
        }
    }

    test {
        useJUnitPlatform {
            includeEngines("junit-jupiter", "spek2")
        }
    }
}