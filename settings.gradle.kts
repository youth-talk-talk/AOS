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
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}
rootProject.name = "YongProject"
include(":app")
include(
    ":core",
    ":core:data",
    ":core:dataApi",
    ":core:designsystem",
    ":core:model",
    ":core:domain",
    ":core:navigation",
    ":core:exception",
    ":core:datastore",
)
include(
    ":feature",
    ":feature:main",
    ":feature:home",
    ":feature:community",
    ":feature:mypage",
    ":feature:login",
    ":feature:policydetail",
    ":feature:specpolicy",
)
