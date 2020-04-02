/*
 *
 *  * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `java-test-fixtures`
    jacoco
    `maven-publish`
    kotlin("jvm")
    id("org.jetbrains.dokka") version "0.10.1"
}

repositories {
    jcenter()
    maven {
        setUrl("https://dl.bintray.com/spekframework/spek")
    }
}

dependencies {
    api(project(":stubr-core"))

    compileOnly(kotlin("reflect"))
    compileOnly(kotlin("stdlib-jdk8"))

    testImplementation(testFixtures(project(":stubr-core")))
    testImplementation(kotlin("reflect"))
    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation(group = "org.spekframework.spek2", name = "spek-dsl-jvm")

    testRuntimeOnly(group = "org.spekframework.spek2", name = "spek-runner-junit5")
}

tasks {
    withType(KotlinCompile::class) {
        sourceCompatibility = "1.8"
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=compatibility")
        }
    }

    test {
        useJUnitPlatform {
            includeEngines("spek2", "junit-jupiter")
        }
    }

    javadocJar {
        from(dokka)
    }
}
