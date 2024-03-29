/*
 * Copyright (C) 2022 Adrian-Philipp Leuenberger
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
    `kotlin-library-conventions`
}

subprojects {
    apply(plugin = "kotlin-library-conventions")

    dependencies {
        testImplementation(project(":stubr-javafaker"))
        testImplementation(project(":stubr-junit"))
        testImplementation(project(":stubr-kotlin"))
        testImplementation(project(":stubr-mockito"))
        testImplementation(project(":stubr-mockk"))
        testImplementation(project(":stubr-spek"))
    }

    tasks {
        compileKotlin {
            kotlinOptions {
                javaParameters = true
            }
        }
    }
}

allprojects {
    tasks {
        dokkaHtml {
            isEnabled = false
        }

        javadocJar {
            isEnabled = false
        }
    }
}