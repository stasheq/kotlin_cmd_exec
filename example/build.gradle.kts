plugins {
    id("kotlin")
    id("application")
}

application {
    mainClassName = "me.szymanski.kotlinexec.example.Main"
}

dependencies {
    implementation("me.szymanski.kotlinexec:kotlin-exec-lib:1.0")
}
