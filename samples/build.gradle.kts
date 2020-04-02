plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":stubr-junit"))
    implementation(project(":stubr-mockito"))
}

tasks {
    compileKotlin {
        kotlinOptions {
            javaParameters = true
        }
    }
}