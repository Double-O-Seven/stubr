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

plugins {
    `java-library`
    `java-test-fixtures`
    jacoco
    `maven-publish`
}

dependencies {
    api(project(":stubr-core"))
    api(group = "org.junit.jupiter", name = "junit-jupiter-api")

    implementation(group = "ch.leadrian.equalizer", name = "equalizer-core")
    implementation(group = "com.google.guava", name = "guava")

    testImplementation(testFixtures(project(":stubr-core")))
}