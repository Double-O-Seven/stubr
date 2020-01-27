include(":stubr-core")
include(":stubr-integration-test")
include(":stubr-junit")
include(":stubr-kotlin")
include(":stubr-mockito")

rootProject.name = "stubr"

pluginManagement {
    plugins {
        kotlin("jvm") version "1.3.61"
    }
}