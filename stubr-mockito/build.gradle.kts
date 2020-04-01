plugins {
    `java-library`
    `java-test-fixtures`
    jacoco
    `maven-publish`
}

dependencies {
    api(project(":stubr-core"))
    api(group = "org.mockito", name = "mockito-core")

    implementation(group = "ch.leadrian.equalizer", name = "equalizer-core")
    implementation(group = "com.google.guava", name = "guava")

    testImplementation(testFixtures(project(":stubr-core")))
}