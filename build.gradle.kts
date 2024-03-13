plugins {
    kotlin("jvm") version "1.9.22" apply false
}


allprojects {
    group = "gitp"
    version = "1.0-SNAPSHOT"
}
subprojects {
    repositories {
        mavenCentral()
    }
}

//repositories {
//    mavenCentral()
//}
//
//dependencies {
//    testImplementation("org.jetbrains.kotlin:kotlin-test")
//}
//
//tasks.test {
//    useJUnitPlatform()
//}
//kotlin {
//    jvmToolchain(21)
//}