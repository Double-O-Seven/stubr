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

plugins {
    `java-library`
    `java-test-fixtures`
    jacoco
    `maven-publish`
    kotlin("jvm")
    id("org.jetbrains.dokka") version "0.10.1"
}

dependencies {
    api(project(":stubr-core"))
    api(kotlin("reflect"))
    api(kotlin("stdlib-jdk8"))
    api(group = "io.mockk", name = "mockk")

    implementation(group = "ch.leadrian.equalizer", name = "equalizer-core")

    testImplementation(testFixtures(project(":stubr-core")))
    testImplementation(group = "org.spekframework.spek2", name = "spek-dsl-jvm")

    testRuntimeOnly(group = "org.spekframework.spek2", name = "spek-runner-junit5")
}

tasks {

    test {
        useJUnitPlatform {
            includeEngines("spek2", "junit-jupiter")
        }
    }

    javadocJar {
        from(dokka)
    }
}