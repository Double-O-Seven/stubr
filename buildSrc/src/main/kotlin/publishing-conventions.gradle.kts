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
    `maven-publish`
    signing
}

val mavenJava by publishing.publications.creating(MavenPublication::class) {
    components.findByName("java")?.let { from(it) }
    components.findByName("javaPlatform")?.let { from(it) }
    versionMapping {
        allVariants {
            fromResolutionResult()
        }
    }
    pom {
        name.set("Stubr (${project.name})")
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
                else -> releasesRepoUrl
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
