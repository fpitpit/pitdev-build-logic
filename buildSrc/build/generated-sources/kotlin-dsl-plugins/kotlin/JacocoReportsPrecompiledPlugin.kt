/**
 * Precompiled [jacoco-reports-precompiled.gradle.kts][Jacoco_reports_precompiled_gradle] script plugin.
 *
 * @see Jacoco_reports_precompiled_gradle
 */
public
class JacocoReportsPrecompiledPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Jacoco_reports_precompiled_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
