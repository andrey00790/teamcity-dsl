package buildTypes

import jetbrains.buildServer.configs.kotlin.v2018_1.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_1.DslContext
import jetbrains.buildServer.configs.kotlin.v2018_1.ParametrizedWithType
import jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_1.triggers.vcs
import kotlin.script.dependencies.Environment

class GradleTest(val tasks: String,
init: GradleTestContext.() -> Unit = {}
):BuildType({

    name = "MyGradleTest"
    id(name)

    params {
        param("env.deploy.environment", "test")
    }


    val context = GradleTestContext()
    context.init()
    context.apply(this.params)

    vcs {
        root(DslContext.settingsRoot)
    }


    steps {
        script {
            scriptContent = """
                #!/usr/bin/env bash
                set -e
                echo "test %deploy.environment%"
            """.trimIndent()
        }
        gradle {
            name
            this.tasks = tasks
        }

    }

    triggers {
        vcs {
        }
    }

})

class GradleTestContext {
    var envVariables: Map<String,String>? = null

    fun apply(params: ParametrizedWithType) {
        envVariables?.let { it.forEach { (key,value) ->  params.param("env.$key",value)} }
    }
}
