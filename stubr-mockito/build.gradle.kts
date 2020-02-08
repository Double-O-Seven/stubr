val equalizerVersion: String by rootProject.extra
val guavaVersion: String by rootProject.extra
val mockitoVersion: String by rootProject.extra

dependencies {
    api(project(":stubr-core"))

    implementation(group = "ch.leadrian.equalizer", name = "equalizer-core", version = equalizerVersion)
    implementation(group = "com.google.guava", name = "guava", version = guavaVersion)
    implementation(group = "org.mockito", name = "mockito-core", version = mockitoVersion)

    testImplementation(testFixtures(project(":stubr-core")))
}