package fr.pitdev.plugins

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.NodeChild
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.rules.JacocoViolationRule
import java.io.File
import java.util.Locale

class JacocoReportsPlugin : Plugin<Project> {
    private val Project.libs: VersionCatalog
        get() = extensions.findByType(VersionCatalogsExtension::class)?.named("projectLibs")
            ?: error("Cannot find version catalog libs: $name")

    private val Project.jacoco: JacocoPluginExtension
        get() = extensions.findByName("jacoco") as? JacocoPluginExtension
            ?: error("Not a Jacoco module: $name")

    private val excludedFiles = mutableSetOf(
        "**/R.class",
        "**/R$*.class",
        "**/*\$ViewInjector*.*",
        "**/*\$ViewBinder*.*",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Factory*",
        "**/*_MembersInjector*",
        "**/*Module*",
        "**/*Component*",
        "**android**",
        "**/BR.class"
    )

    private val limitRules: List<LimitRule> = listOf(
        LimitRule(LIMIT_TYPE.INSTRUCTION, 0.0),
        LimitRule(LIMIT_TYPE.BRANCH, 0.0),
        LimitRule(LIMIT_TYPE.LINE, 0.0),
        LimitRule(LIMIT_TYPE.COMPLEXITY, 0.0),
        LimitRule(LIMIT_TYPE.METHOD, 0.0),
        LimitRule(LIMIT_TYPE.CLASS, 0.0)
    )


    private val includesPackage: List<String> = emptyList()

    override fun apply(project: Project) {
        val extension: JacocoReportsPluginExtension =
            project.extensions.create<JacocoReportsPluginExtension>(
                name = "limitsRules",
            )
        with(project) {
            plugins.run {
                apply("jacoco")
            }
            extension.includes.convention(includesPackage)
            extension.limitRules.convention(limitRules)
            jacocoOnVariant(extension)
            dependencies {
                "implementation"(libs.findLibrary("jacoco").get())
            }
        }
    }


    private fun Project.jacocoOnVariant(extension: JacocoReportsPluginExtension) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            val buildType = variant.buildType ?: ""
            val flavorName = variant.flavorName.takeIf { it?.isNotEmpty() == true } ?: ""
            var sourceName: String
            var sourcePath: String

            if (flavorName.isEmpty()) {
                sourceName = buildType
                sourcePath = buildType
            } else {
                sourceName = "${flavorName}${
                    buildType.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ENGLISH
                        ) else it.toString()
                    }
                }"
                sourcePath = "${flavorName}/${buildType}"
            }

            val testTaskName = "test${
                sourceName.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ENGLISH
                    ) else it.toString()
                }
            }UnitTest"

            registerCodeCoverageTask(
                extension = extension,
                testTaskName = testTaskName,
                sourceName = sourceName,
                sourcePath = sourcePath,
                flavorName = flavorName,
                buildTypeName = buildType
            )

        }


    }

    private fun Project.registerCodeCoverageTask(
        extension: JacocoReportsPluginExtension,
        testTaskName: String,
        sourceName: String,
        sourcePath: String,
        flavorName: String,
        buildTypeName: String
    ) {
        tasks.register<JacocoReport>("${testTaskName}Coverage") {
            dependsOn(testTaskName)
            group = "Reporting"
            description =
                "Generate Jacoco coverage reports on the ${
                    sourceName.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ENGLISH
                        ) else it.toString()
                    }
                } build."

            val javaDirectories = fileTree(
                "${project.buildDir}/intermediates/javac/${sourcePath}/classes"
            ) { exclude(excludedFiles) }

            val kotlinDirectories = fileTree(
                "${project.buildDir}/tmp/kotlin-classes/${sourcePath}"
            ) { exclude(excludedFiles) }

            val coverageSrcDirectories = listOf(
                "src/main/java",
                "src/$flavorName/java",
                "src/$buildTypeName/java"
            )

            classDirectories.setFrom(files(javaDirectories, kotlinDirectories))
            additionalClassDirs.setFrom(files(coverageSrcDirectories))
            sourceDirectories.setFrom(files(coverageSrcDirectories))
            getExecutionData().setFrom(
                files("${project.buildDir}/outputs/unit_test_code_coverage/${sourceName}UnitTest/${testTaskName}.exec")
            )

            executionData.setFrom(file("${project.buildDir}/outputs/unit_test_code_coverage/${sourceName}UnitTest/${testTaskName}.exec"))

            reports {
                xml.required.set(true)
                html.required.set(true)
            }

        }

        jacocoCoverageVerification(extension, sourceName, testTaskName)

    }

    private fun Project.jacocoCoverageVerification(
        extension: JacocoReportsPluginExtension,
        sourceName: String,
        testTaskName: String
    ) {
        tasks.register<JacocoCoverageVerification>("${testTaskName}CoverageVerification") {
            group = "verification"
            dependsOn("${testTaskName}Coverage")
            getExecutionData().setFrom(
                files("${project.buildDir}/outputs/unit_test_code_coverage/${sourceName}UnitTest/${testTaskName}.exec")
            )
            val limitRules = extension.limitRules.getOrElse(emptyList())
            val includePackages = extension.includes.getOrElse(emptyList())
            logger.quiet("limits for ${sourceName} = ${limitRules}, includesPackage : ${includePackages}")
            violationRules {
                rule {
                    isEnabled = true
                    element = "CLASS"
                    includes = includePackages

                    limitRules.forEach { limit ->

                        limit {
                            counter = limit.type.name
                            value = "COVEREDRATIO"
                            minimum = limit.limit.toBigDecimal()
                        }
                    }
                }
            }
            logger.quiet("violation rules = ${violationRules}")
        }
    }
}

interface JacocoReportsPluginExtension {
    val limitRules: ListProperty<LimitRule>
    val includes: ListProperty<String>
}

data class LimitRule(val type: LIMIT_TYPE, val limit: Double)
enum class LIMIT_TYPE {
    INSTRUCTION,
    BRANCH,
    LINE,
    COMPLEXITY,
    METHOD,
    CLASS
}
