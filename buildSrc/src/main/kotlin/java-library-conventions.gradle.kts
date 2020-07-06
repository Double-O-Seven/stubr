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
    `java-test-fixtures` apply false
}

val internal: Configuration by configurations.creating {
    isVisible = false
    isCanBeConsumed = false
    isCanBeResolved = false
}

configurations {
    compileClasspath.get().extendsFrom(internal)
    runtimeClasspath.get().extendsFrom(internal)
    testCompileClasspath.get().extendsFrom(internal)
    testRuntimeClasspath.get().extendsFrom(internal)
}

pluginManager.withPlugin("java-test-fixtures") {
    val javaComponent = components["java"] as AdhocComponentWithVariants
    configurations {
        javaComponent.withVariantsFromConfiguration(testFixturesApiElements.get()) { skip() }
        javaComponent.withVariantsFromConfiguration(testFixturesRuntimeElements.get()) { skip() }
        testFixturesCompileClasspath.get().extendsFrom(internal)
        testFixturesRuntimeClasspath.get().extendsFrom(internal)
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

val dependenciesProject: Project by rootProject.extra

dependencies {
    internal(platform(dependenciesProject))

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