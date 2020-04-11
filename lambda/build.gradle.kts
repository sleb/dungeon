import com.scorpipede.dungeon.BuildVersions.alexaSdk
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.70"
    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform(kotlin("bom")))

    // Use the Kotlin JDK 8 standard library.
    implementation(kotlin("stdlib-jdk8"))

    // Alexa SDK
    implementation("com.amazon.alexa:ask-sdk:$alexaSdk")
    implementation("com.amazon.alexa:ask-sdk-core:$alexaSdk")
    implementation("com.amazon.alexa:ask-sdk-lambda-support:$alexaSdk")

    implementation("com.amazonaws:aws-lambda-java-log4j2:1.1.0")

    // Use the Kotlin test library.
    testImplementation(kotlin("test"))

    // Use the Kotlin JUnit integration.
    testImplementation(kotlin("test-junit"))
}

tasks {
    test {
        testLogging {
            events = setOf(PASSED, SKIPPED, FAILED)
        }
    }

    withType<KotlinCompile>().configureEach{
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

tasks.register<Zip>("buildZip") {
    from(tasks.compileKotlin)
    from(tasks.processResources)
    into("lib") {
        from(configurations.runtimeClasspath)
    }
}
