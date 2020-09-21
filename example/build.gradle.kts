plugins {
    id("kotlin")
    id("application")
}

application {
    mainClassName = "me.szymanski.kotlinexec.example.Main"
}

dependencies {
    implementation(project(":kotlinExecLib"))
}
