plugins {
    `java-platform`
    `maven-publish`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform("com.google.guava:guava-bom:28.2-jre"))
    api(platform("org.junit:junit-bom:5.6.0"))

    constraints {
        api("ch.leadrian.equalizer:equalizer-core:1.2.0")
        api("org.assertj:assertj-core:3.13.2")
        api("org.mockito:mockito-core:3.2.4")
        api("org.spekframework.spek2:spek-dsl-jvm:2.0.9")

        runtime("org.spekframework.spek2:spek-runner-junit5:2.0.9")
    }
}