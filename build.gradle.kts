import org.gradle.api.tasks.SourceSetContainer
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    jacoco
    alias(libs.plugins.spotless)
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
}

// Resolving the issue of not being able to reference the version catalog in allprojects and subprojects scopes
val versionCatalog = libs

allprojects {
    group = "io.zhc1"

    plugins.apply(
        versionCatalog.plugins.java
            .get()
            .pluginId,
    )
    plugins.apply(
        versionCatalog.plugins.spotless
            .get()
            .pluginId,
    )
    plugins.apply("jacoco")

    repositories {
        mavenCentral()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(versionCatalog.versions.java.get()))
        }
    }

    jacoco {
        toolVersion = "0.8.11"
    }

    spotless {
        java {
            palantirJavaFormat().formatJavadoc(true)

            formatAnnotations()
            removeUnusedImports()
            trimTrailingWhitespace()
            importOrder("java", "jakarta", "org", "com", "net", "io", "lombok", "io.zhc1")
        }

        kotlin {
            ktlint()
            trimTrailingWhitespace()
        }

        kotlinGradle {
            ktlint()
            trimTrailingWhitespace()
        }
    }
}

subprojects {
    plugins.apply(
        versionCatalog.plugins.spring.boot
            .get()
            .pluginId,
    )
    plugins.apply(
        versionCatalog.plugins.spring.dependency.management
            .get()
            .pluginId,
    )

    configurations {
        all { exclude(group = "junit", module = "junit") }
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    dependencies {
        implementation(platform(versionCatalog.spring.boot.bom))

        compileOnly(versionCatalog.lombok)
        annotationProcessor(versionCatalog.lombok)

        implementation(versionCatalog.spring.boot.starter)
        testImplementation(versionCatalog.spring.boot.starter.test)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)
    }

    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }

    tasks.getByName<BootJar>("bootJar") {
        enabled = false
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }
}

tasks.register<JacocoReport>("jacocoRootReport") {
    dependsOn(subprojects.map { it.tasks.withType<Test>() })

    executionData(subprojects.flatMap {
        it.tasks.withType<Test>().map { task ->
            task.extensions.getByType<JacocoTaskExtension>().destinationFile
        }
    })

    subprojects.forEach { subproject ->
        val sourceSets = subproject.extensions.getByType<SourceSetContainer>()
        sourceDirectories.from(sourceSets.getByName("main").allSource.srcDirs)
        classDirectories.from(sourceSets.getByName("main").output)
    }

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/aggregate"))
    }
}
