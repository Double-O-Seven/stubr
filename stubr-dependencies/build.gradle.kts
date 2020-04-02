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
    `java-platform`
    `maven-publish`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform("com.google.guava:guava-bom:28.2-jre"))
    api(platform("org.junit:junit-bom:5.6.1"))

    constraints {
        api("ch.leadrian.equalizer:equalizer-core:1.2.0")
        api("org.assertj:assertj-core:3.15.0")
        api("org.mockito:mockito-core:3.3.3")
        api("org.spekframework.spek2:spek-dsl-jvm:2.0.10")

        runtime("org.spekframework.spek2:spek-runner-junit5:2.0.10")
    }
}