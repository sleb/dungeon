import com.scorpipede.dungeon.BuildVersions.alexaSdk
import com.scorpipede.dungeon.BuildVersions.slf4j
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    kotlin("jvm") version "1.3.70"
    `java-library`
    distribution
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

    runtimeOnly("org.slf4j:slf4j-simple:$slf4j")

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
}

distributions {
    main {
        contents {
            from(tasks.compileKotlin)
            from(tasks.processResources)
            into("lib") {
                from(configurations.runtimeClasspath)
            }
        }
    }
}
