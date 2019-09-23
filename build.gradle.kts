import groovy.lang.Closure

plugins {
    id("com.palantir.git-version") version "0.12.0-rc2"
}

val gitVersion: Closure<String> by extra

allprojects {
    group = "ch.leadrian.stubr"

    version = gitVersion()
}

subprojects {
    apply(plugin = "java-library")

    repositories {
        mavenCentral()
    }
}