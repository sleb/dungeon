import Versions.CDK
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    kotlin("jvm") version "1.3.70"
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))
    implementation("software.amazon.awscdk:core:$CDK")
    implementation("software.amazon.awscdk:lambda:$CDK")
    implementation("software.amazon.awscdk:codebuild:$CDK")
    implementation("software.amazon.awscdk:codecommit:$CDK")
    implementation("software.amazon.awscdk:codepipeline:$CDK")
    implementation("software.amazon.awscdk:codepipeline-actions:$CDK")
    implementation("com.squareup.moshi:moshi-kotlin:1.9+")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

application {
    mainClassName = "com.scorpipede.dungeon.infra.AppKt"
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

