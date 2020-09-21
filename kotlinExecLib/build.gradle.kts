plugins {
    id("java-library")
    id("kotlin")
    id("maven-publish")
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("default") {
            groupId = "me.szymanski.kotlinexec"
            artifactId = "kotlin-exec-lib"
            version = "1.0"

            from(components["java"])

            pom {
                name.set("kotlin-exec-lib")
                description.set("Code to run command line system commands from Kotlin")
                url.set("https://github.com/stasheq/kotlin_cmd_exec")
                licenses {
                    license {
                        name.set("The Unlicense")
                        url.set("https://unlicense.org/")
                    }
                }
                developers {
                    developer {
                        id.set("stasheq")
                        name.set("Pawel Szymanski")
                        email.set("stasheq@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/stasheq/kotlin_cmd_exec")
                }
            }
        }
    }
    repositories {
        maven {
            url = uri("$buildDir/repository")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
}
