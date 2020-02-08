import groovy.lang.Closure
import org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME

plugins {
    `java-library`
    jacoco
    `maven-publish`
    signing
    id("com.palantir.git-version") version "0.12.0-rc2"
}

val assertJVersion by extra { "3.12.1" }
val equalizerVersion by extra { "1.1.0" }
val guavaVersion by extra { "28.2-jre" }
val junitVersion by extra { "5.5.1" }
val mockitoVersion by extra { "3.2.4" }

val gitVersion: Closure<String> by extra

val codacyCoverageReport: Configuration by configurations.creating

repositories {
    jcenter()
}

dependencies {
    codacyCoverageReport(group = "com.codacy", name = "codacy-coverage-reporter", version = "6.2.0")
}

tasks {
    jacocoTestReport {
        subprojects.forEach { dependsOn(it.tasks.test) }
        executionData.setFrom(subprojects.map { file("${it.buildDir}/jacoco/test.exec") })
        additionalSourceDirs.setFrom(subprojects.map { it.sourceSets[MAIN_SOURCE_SET_NAME].allSource.sourceDirectories })
        sourceDirectories.setFrom(subprojects.map { it.sourceSets[MAIN_SOURCE_SET_NAME].allSource.sourceDirectories })
        classDirectories.setFrom(subprojects.map { it.sourceSets[MAIN_SOURCE_SET_NAME].output })
        reports {
            xml.isEnabled = true
        }
    }
}

task<JavaExec>("codacyCoverageReport") {
    dependsOn(tasks.jacocoTestReport)
    main = "com.codacy.CodacyCoverageReporter"
    classpath = codacyCoverageReport
    args(
            "report",
            "-l",
            "Java",
            "-r",
            "$buildDir/reports/jacoco/test/jacocoTestReport.xml"
    )
}

allprojects {
    group = "ch.leadrian.stubr"

    version = gitVersion()
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "jacoco")

    repositories {
        jcenter()
    }

    dependencies {
        testImplementation(group = "org.assertj", name = "assertj-core", version = assertJVersion)
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitVersion)
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-params", version = junitVersion)
        testImplementation(group = "org.mockito", name = "mockito-core", version = mockitoVersion)
        testImplementation(group = "com.google.guava", name = "guava-testlib", version = guavaVersion)

        testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = junitVersion)
    }

    tasks {
        test {
            useJUnitPlatform()
        }

        jacocoTestReport {
            dependsOn(test)
            reports {
                xml.isEnabled = true
            }
        }
    }
}

configure(subprojects - project(":stubr-integration-test")) {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    java {
        withSourcesJar()
        withJavadocJar()
    }

    val mavenJava by publishing.publications.creating(MavenPublication::class) {
        from(components["java"])
        pom {
            name.set("Stubr (${this@configure.name})")
            description.set("Library for instantiating stub objects in unit tests")
            url.set("https://github.com/Double-O-Seven/stubr")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("Double-O-Seven")
                    name.set("Adrian-Philipp Leuenberger")
                    email.set("thewishwithin@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:git://github.com/Double-O-Seven/stubr.git")
                developerConnection.set("scm:git:ssh://github.com/Double-O-Seven/stubr.git")
                url.set("https://github.com/Double-O-Seven/stubr")
            }
        }
    }

    publishing {
        repositories {
            maven {
                val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
                val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                url = when {
                    version.toString().endsWith("SNAPSHOT") -> snapshotsRepoUrl
                    else -> releasesRepoUrl
                }
                credentials {
                    val ossrhUsername: String? by extra
                    val ossrhPassword: String? by extra
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }
    }

    signing {
        sign(mavenJava)
    }
}