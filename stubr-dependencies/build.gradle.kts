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
    `java-platform`
    `maven-publish`
}

javaPlatform {
    allowDependencies()
}

object Versions {

    const val assertJ = "3.16.1"

    const val equalizer = "1.2.1"

    const val guava = "29.0-jre"

    const val junit = "5.6.2"

    const val mockito = "3.3.3"

    const val mockK = "1.10.0"

    const val spek = "2.0.11"

}

dependencies {
    api(platform("ch.leadrian.equalizer:equalizer-bom:${Versions.equalizer}"))
    api(platform("com.google.guava:guava-bom:${Versions.guava}"))
    api(platform("org.junit:junit-bom:${Versions.junit}"))

    constraints {
        api("io.mockk:mockk:${Versions.mockK}")
        api("org.assertj:assertj-core:${Versions.assertJ}")
        api("org.mockito:mockito-core:${Versions.mockito}")
        api("org.spekframework.spek2:spek-dsl-jvm:${Versions.spek}")

        runtime("org.spekframework.spek2:spek-runner-junit5:${Versions.spek}")
    }
}