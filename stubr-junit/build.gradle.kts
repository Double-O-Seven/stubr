plugins {
    `java-library`
    `java-test-fixtures`
    jacoco
    `maven-publish`
}

dependencies {
    api(project(":stubr-core"))
    api(group = "org.junit.jupiter", name = "junit-jupiter-api")

    implementation(group = "ch.leadrian.equalizer", name = "equalizer-core")
    implementation(group = "com.google.guava", name = "guava")

    testImplementation(testFixtures(project(":stubr-core")))
}