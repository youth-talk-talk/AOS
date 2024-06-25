pluginManagement {
    includeBuild("build-logic")
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
rootProject.name = "YongProject"
include(":app")
include(
    ":core",
    ":core:data",
    ":core:data-api",
    "core:designsystem",
    "core:model",
)
include(
    ":feature",
    ":feature:main",
)
include(":core:domain")
include(":core:navigation")
include(":feature:home")
include(":feature:community")
include(":feature:mypage")
include(":feature:login")
