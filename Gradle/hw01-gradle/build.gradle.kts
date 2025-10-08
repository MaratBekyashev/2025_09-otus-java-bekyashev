plugins {
    // java
    // application
    // id ("com.github.johnrengelman.shadow")
}


dependencies {
    implementation ("com.google.guava:guava")
}

/*application {
    mainClass.set("ru.otus.Main")
}
*/
tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("gradleHelloWorld")
        archiveVersion.set("0.1")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "ru.otus.App"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}

