pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Task Manager"
include(":app")

include(":core")
include(":core:designsystem")
include(":core:database")

include(":feature")
include(":feature:notes")
include(":feature:settings")
include(":feature:tasks")
include(":core:data")
include(":core:domain")
include(":core:model")
include(":core:datastore")
include(":core:storage")
include(":core:network")
include(":core:speech")
