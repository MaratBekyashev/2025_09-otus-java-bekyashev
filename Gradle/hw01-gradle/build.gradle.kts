plugins {
     java
    // application
    id("com.github.johnrengelman.shadow")

}

dependencies {
    implementation ("com.google.guava:guava")
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set("gradleHelloWorld")
        archiveVersion.set("0.1")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "ru.otus.MainClass"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}

