package buildTypes

import jetbrains.buildServer.configs.kotlin.v2018_1.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_1.DslContext
import jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2018_1.triggers.vcs

class MyTestBuild(btName:String) : BuildType({

    name = "Test enviroment $btName"
    id("${id}_${btName}")

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            name
            tasks = "clean test"
        }

    }

    triggers {
        vcs {
        }
    }
})
