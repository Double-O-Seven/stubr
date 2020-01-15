plugins {
    jacoco
    `java-test-fixtures`
}

val guavaVersion: String by rootProject.extra

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
}