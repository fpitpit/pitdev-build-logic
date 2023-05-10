package fr.pitdev.plugins

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.ListProperty
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.rules.JacocoLimit
import org.gradle.testing.jacoco.tasks.rules.JacocoViolationRule
import org.gradle.testing.jacoco.tasks.rules.JacocoViolationRulesContainer
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

    override fun apply(project: Project) {
        val extension: JacocoReportsPluginExtension =
            project.extensions.create(
                name = "limitsRules",
            )
        with(project) {
            plugins.run {
                apply("jacoco")
            }
            extension.includes.convention(emptyList())
            extension.limitRules.convention(emptyList())
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
            executionData.setFrom(file("${project.buildDir}/outputs/unit_test_code_coverage/${sourceName}UnitTest/${testTaskName}.exec"))

            reports {
                xml.required.set(true)
                html.required.set(true)
            }

        }

        jacocoCoverageVerification(
            extension = extension,
            sourceName = sourceName,
            sourcePath = sourcePath,
            testTaskName = testTaskName,
            buildTypeName = buildTypeName,
            flavorName = flavorName
        )

    }

    private fun Project.jacocoCoverageVerification(
        extension: JacocoReportsPluginExtension,
        sourceName: String,
        sourcePath: String,
        flavorName: String,
        buildTypeName: String,
        testTaskName: String
    ) {
        tasks.register<JacocoCoverageVerification>("${testTaskName}CoverageVerification") {
            group = "verification"
            dependsOn("${testTaskName}Coverage")
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

            executionData.setFrom(
                files("${project.buildDir}/outputs/unit_test_code_coverage/${sourceName}UnitTest/${testTaskName}.exec")
            )
            val limitRules = extension.limitRules.getOrElse(emptyList())
            val includePackages = extension.includes.getOrElse(emptyList())
            logger.quiet("limits for $sourceName = ${limitRules}, includesPackage : $includePackages")
            violationRules {
                rule {
                    isEnabled = true
                    element = "PACKAGE"
                    includes = includePackages
                    excludes = listOf("**/ui/theme")
                    limitRules.forEach { limit ->
                        limit {
                            counter = limit.type.name
                            value = "COVEREDRATIO"
                            takeIf { limit.maximum > 0 }?.let {
                                maximum = limit.maximum.toBigDecimal()
                            }
                            takeIf { limit.minimum > 0 }?.let {
                                minimum = limit.minimum.toBigDecimal()
                            }
                        }
                    }
                }
            }

        }
    }
}

interface JacocoReportsPluginExtension {
    val limitRules: ListProperty<LimitRule>
    val includes: ListProperty<String>
    val excludes: ListProperty<String>
}

data class LimitRule(val type: LimitType, val minimum: Double = 0.0, val maximum: Double = 0.0) {
    init {
        require(minimum >= 0) {
            "$minimum must be >= 0"
        }
        require(maximum >= 0) {
            "$maximum must be >= 0"
        }
        require(maximum <= 1) {
            "$maximum must be between 0.0 and 1.O"
        }
    }
}

enum class LimitType {
    INSTRUCTION,
    BRANCH,
    LINE,
    COMPLEXITY,
    METHOD,
    CLASS
}