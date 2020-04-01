plugins {
    `java-library`
}

dependencies {
    testImplementation(project(":stubr-junit"))
    testImplementation(project(":stubr-core"))
}