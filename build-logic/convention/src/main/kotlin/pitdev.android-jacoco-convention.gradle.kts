import fr.pitdev.jacoco.configureJacoco


plugins {
    id("jacoco")
    id("io.github.gmazzo.test.aggregation.coverage")
}


project.configureJacoco()
