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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Webdav Manager"
include(":app")
include(":feature:serverlist")
include(":feature:serverconfig")
include(":feature:filemanager")
include(":core:ui")
include(":core:database")
include(":core:data")
include(":core:storage:webdav")
include(":core:storage:android")
