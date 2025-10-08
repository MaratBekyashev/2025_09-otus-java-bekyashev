// Корневой
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
//import name.remal.gradle_plugins.sonarlint.SonarLintExtension
//import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
//import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    java
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

allprojects {
    group = "com.otus"
    version = "1.0.0"

    repositories {
        gradlePluginPortal()  // Обязательно добавьте это!
        mavenCentral()
        google()
    }

    //val testcontainersBom: String by project
    //val protobufBom: String by project
    val guava: String by project
    //val jmh: String by project
    //val asm: String by project
    //val glassfishJson: String by project
    //val errorProneAnnotations: String by project
    //val j2objcAnnotations: String by project
    //val redisson: String by project

    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            /*imports {
                mavenBom(BOM_COORDINATES)
                //mavenBom("org.testcontainers:testcontainers-bom:$testcontainersBom")
                //   mavenBom("com.google.protobuf:protobuf-bom:$protobufBom")
            }*/
            dependency("com.google.guava:guava:$guava")
            //dependency("org.openjdk.jmh:jmh-core:$jmh")
            //dependency("org.openjdk.jmh:jmh-generator-annprocess:$jmh")
            //dependency("org.ow2.asm:asm-commons:$asm")
            //dependency("org.glassfish:jakarta.json:$glassfishJson")
            //dependency("com.google.errorprone:error_prone_annotations:$errorProneAnnotations")
            //dependency("com.google.j2objc:j2objc-annotations:$j2objcAnnotations")
            //dependency("org.redisson:redisson:$redisson")
        }
    }

    /*configurations.all {
        resolutionStrategy {
            failOnVersionConflict()

            //force("javax.servlet:servlet-api:2.5")
            force("commons-logging:commons-logging:1.1.1")
            force("commons-lang:commons-lang:2.5")
            //force("org.codehaus.jackson:jackson-core-asl:1.8.8")
            //force("org.codehaus.jackson:jackson-mapper-asl:1.8.8")
            force("commons-io:commons-io:2.18.0")
        }
    }*/

}