import groovy.lang.Closure

plugins {
    `java-library`
    id("com.palantir.git-version") version "0.12.0-rc2"
}

val junitVersion by extra { "5.5.1" }
val guavaVersion by extra { "28.2-jre" }

val gitVersion: Closure<String> by extra

allprojects {
    group = "ch.leadrian.stubr"

    version = gitVersion()
}

subprojects {
    apply(plugin = "java-library")

    repositories {
        jcenter()
    }

    dependencies {
        testImplementation(group = "org.assertj", name = "assertj-core", version = "3.12.1")
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitVersion)
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-params", version = junitVersion)
        testImplementation(group = "org.mockito", name = "mockito-core", version = "3.2.4")
        testImplementation(group = "com.google.guava", name = "guava-testlib", version = guavaVersion)

        testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = junitVersion)
    }

    tasks {
        test {
            useJUnitPlatform()
        }
    }
}