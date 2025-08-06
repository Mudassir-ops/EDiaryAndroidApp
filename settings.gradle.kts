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
//        maven {
//            url = uri("https://maven.pkg.github.com/Mudassir-ops/firebase-native-commons")
//            credentials {
//                username = "Mudassir-ops"
//                password = "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"
//            }
//        }
        //  mavenLocal()
    }
}

rootProject.name = "NewEasyDairy"
include(":app")
include(":firebase-core")
