val equalizerVersion: String by rootProject.extra
val guavaVersion: String by rootProject.extra
val junitVersion: String by rootProject.extra

dependencies {
    api(project(":stubr-core"))
    api(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitVersion)

    implementation(group = "ch.leadrian.equalizer", name = "equalizer-core", version = equalizerVersion)
    implementation(group = "com.google.guava", name = "guava", version = guavaVersion)

    testImplementation(testFixtures(project(":stubr-core")))
}