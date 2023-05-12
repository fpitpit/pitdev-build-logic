package fr.pitdev.plugins

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.rules.JacocoLimit
import org.gradle.testing.jacoco.tasks.rules.JacocoViolationRule
import org.gradle.testing.jacoco.tasks.rules.JacocoViolationRulesContainer
import java.util.Locale

class JacocoReportsPlugin : Plugin<Project> {
    private val Project.libs: VersionCatalog
        get() = extensions.findByType(VersionCatalogsExtension::class)?.named("libs")
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

    override fun apply(project: Project) {
        with(project) {
            plugins.run {
                apply("jacoco")
            }
            val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
            tasks.withType(Test::class.java) {
                configure<JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*")
                }
            }
            androidComponents.onVariants { variant ->
                jacocoOnVariant(variant)
            }
            dependencies {
                "implementation"(libs.findLibrary("jacoco").get())
            }
        }
    }


    private fun Project.jacocoOnVariant(variant: Variant) {
            val buildType = variant.buildType ?: ""
            val flavorName = variant.flavorName.takeIf { it?.isNotEmpty() == true } ?: ""
            val sourceName: String
            val sourcePath: String

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
                testTaskName = testTaskName,
                sourceName = sourceName,
                sourcePath = sourcePath,
                flavorName = flavorName,
                buildTypeName = buildType
            )
    }

    private fun Project.registerCodeCoverageTask(
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
            executionData.setFrom(file("${project.buildDir}/outputs/unit_test_code_coverage/${sourceName}UnitTest/${testTaskName}.exec"))

            reports {
                xml.required.set(true)
                html.required.set(true)
            }

        }
    }
}
