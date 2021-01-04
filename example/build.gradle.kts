plugins {
    id("kotlin")
    id("application")
}

application {
    mainClass.set("me.szymanski.kotlinexec.example.Main")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.21")
    implementation("me.szymanski.kotlinexec:kotlin-exec-lib:1.01")
}
