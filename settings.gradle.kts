plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "yonsei-lecture-finding-supporter"
include("scraping-batch")
include("entity-module")
include("lecture-content")
