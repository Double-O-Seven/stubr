/*
 * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import groovy.lang.Closure
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    jacoco
    `maven-publish`
    signing
    kotlin("jvm") apply false
    id("com.palantir.git-version") version "0.12.3"
    id("com.github.ben-manes.versions") version "0.28.0"
}

val gitVersion: Closure<String> by extra

val codacyCoverageReport: Configuration by configurations.creating

allprojects {
    group = "ch.leadrian.stubr"

    version = gitVersion()

    repositories {
        jcenter()
    }
}

dependencies {
    codacyCoverageReport(group = "com.codacy", name = "codacy-coverage-reporter", version = "7.1.0")
}

tasks {
    jacocoTestReport {
        val projectsWithTests = subprojects.filter { it.pluginManager.hasPlugin("jacoco") }
        projectsWithTests.forEach { dependsOn(it.tasks.test) }
        val testExecFiles = projectsWithTests.map { file("${it.buildDir}/jacoco/test.exec") }.filter { it.exists() }
        executionData.setFrom(testExecFiles)
        additionalSourceDirs.setFrom(projectsWithTests.map { it.sourceSets.main.get().allSource.sourceDirectories })
        sourceDirectories.setFrom(projectsWithTests.map { it.sourceSets.main.get().allSource.sourceDirectories })
        classDirectories.setFrom(projectsWithTests.map { it.sourceSets.main.get().output })
        reports {
            xml.isEnabled = true
        }
    }

    dependencyUpdates {
        checkConstraints = true
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

subprojects {

    tasks {

        withType(KotlinCompile::class) {
            sourceCompatibility = "1.8"
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = listOf("-Xjvm-default=compatibility")
            }
        }
    }

    pluginManager.withPlugin("java-library") {
        java {
            withSourcesJar()
            withJavadocJar()
        }

        dependencies {
            api(platform(project(":stubr-dependencies")))

            testImplementation(group = "org.assertj", name = "assertj-core")
            testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api")
            testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-params")
            testImplementation(group = "org.mockito", name = "mockito-core")
            testImplementation(group = "com.google.guava", name = "guava-testlib")

            testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine")
        }

        tasks.test {
            useJUnitPlatform()
        }
    }

    pluginManager.withPlugin("jacoco") {
        tasks {
            jacocoTestReport {
                dependsOn(test)
                reports {
                    xml.isEnabled = true
                }
            }
        }
    }

    pluginManager.withPlugin("maven-publish") {
        apply(plugin = "signing")

        val mavenJava by publishing.publications.creating(MavenPublication::class) {
            components.findByName("java")?.let { from(it) }
            components.findByName("javaPlatform")?.let { from(it) }
            pom {
                name.set("Stubr (${this@subprojects.name})")
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
                        else                                    -> releasesRepoUrl
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
}