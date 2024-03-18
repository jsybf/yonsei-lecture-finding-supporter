plugins {
    kotlin("jvm")
    kotlin("plugin.jpa") version "1.9.22"
    kotlin("plugin.allopen") version "1.9.22"
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")

}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}