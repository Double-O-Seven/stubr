val equalizerVersion: String by rootProject.extra
val guavaVersion: String by rootProject.extra
val junitVersion: String by rootProject.extra

dependencies {
    api(project(":stubr-core"))

    implementation(group = "ch.leadrian.equalizer", name = "equalizer-core", version = equalizerVersion)
    implementation(group = "com.google.guava", name = "guava", version = guavaVersion)
    implementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitVersion)

    testImplementation(testFixtures(project(":stubr-core")))
}