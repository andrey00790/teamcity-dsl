package buildTypes

import jetbrains.buildServer.configs.kotlin.v2018_1.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_1.DslContext
import jetbrains.buildServer.configs.kotlin.v2018_1.ParametrizedWithType
import jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_1.triggers.vcs

open class ConsoleTest(
        init: ConsoleTestContext.() -> Unit = {}
) : BuildType({
    name = "MyConsoleTest"
    id(name)


    params {
        param("deploy.environment", "test")
        param("tests.versionBuild","${'$'}VERSION")
    }


    val context = ConsoleTestContext()
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

                echo "%tests.versionBuild%"

                GRADLE_EXEC=${'$'}(shell which gradle')
                ${'$'}(GRADLE_EXEC) clean test

                echo "##teamcity[importData type='junit' path='${'$'}(MSBuildProjectDirectory)\test\test-results.xml']"

            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }

})

class ConsoleTestContext {
    var envVariables: Map<String,String>? = null

    fun apply(params: ParametrizedWithType) {
        envVariables?.let { it.forEach { (key,value) ->  params.param("env.$key",value)} }
    }
}
