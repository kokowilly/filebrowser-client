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
  }
}

rootProject.name = "File Browser"

include(":app")
include(":core:foundation")
include(":core:log")
include(":core:navigation")
include(":example:theme")
include(":feature:connection")
include(":lib:network")
include(":test:common")
include(":feature:browse")
