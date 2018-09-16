package buildTypes

import jetbrains.buildServer.configs.kotlin.v2018_1.BuildType

class MyProject_MasterTest(buildTypes: List<BuildType>) : BuildType({

    name = "MyProject_MasterTest"
    id(name)

    dependencies {
        buildTypes.forEach {
            snapshot(it, {

            })
        }
    }
})