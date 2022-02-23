/*
 * Copyright (C) 2021 Adrian-Philipp Leuenberger
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

plugins {
    `code-coverage-report`
    id("com.github.ben-manes.versions")
    id("com.palantir.git-version")
}

val gitVersion: Closure<String> by extra

version = gitVersion()

allprojects {
    group = "ch.leadrian.stubr"

    version = rootProject.version

    repositories {
        mavenCentral()
        maven { setUrl("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
    }
}

tasks {
    dependencyUpdates {
        checkConstraints = true
    }
}
