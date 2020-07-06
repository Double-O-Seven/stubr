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

include(":dependencies")
include(":samples")
include(":stubr-bom")
include(":stubr-core")
include(":stubr-integration-test")
include(":stubr-junit")
include(":stubr-kotlin")
include(":stubr-mockito")
include(":stubr-mockk")

rootProject.name = "stubr"

pluginManagement {
    plugins {
        kotlin("jvm") version "1.3.72"
        id("com.github.ben-manes.versions") version "0.28.0"
        id("com.palantir.git-version") version "0.12.3"
    }
}