plugins {
    `java-platform`
    `maven-publish`
}

dependencies {
    constraints {
        api(project(":stubr-core"))
        api(project(":stubr-junit"))
        api(project(":stubr-kotlin"))
        api(project(":stubr-mockito"))
    }
}