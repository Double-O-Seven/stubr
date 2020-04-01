plugins {
    `java-library`
    `java-test-fixtures`
    jacoco
    `maven-publish`
}

dependencies {
    implementation(group = "ch.leadrian.equalizer", name = "equalizer-core")
    implementation(group = "com.google.guava", name = "guava")

    testFixturesApi(group = "org.junit.jupiter", name = "junit-jupiter-api")

    testFixturesImplementation(group = "org.assertj", name = "assertj-core")
}