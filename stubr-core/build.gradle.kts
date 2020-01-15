plugins {
    jacoco
    `java-test-fixtures`
}

val assertJVersion: String by rootProject.extra
val guavaVersion: String by rootProject.extra
val junitVersion: String by rootProject.extra

tasks {
    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.isEnabled = true
        }
    }
}

jacoco {
    toolVersion = "0.8.4"
}

dependencies {
    implementation(group = "ch.leadrian.equalizer", name = "equalizer-core", version = "1.1.0")
    implementation(group = "com.google.guava", name = "guava", version = guavaVersion)

    testFixturesImplementation(group = "org.assertj", name = "assertj-core", version = assertJVersion)
    testFixturesImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitVersion)
}