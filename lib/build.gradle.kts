plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.commons.math)

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    // Set Java version
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    // Include library sources and javadoc in publications
    withSourcesJar()
    withJavadocJar()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.compileKotlin {
    // Enable Kotlin context receivers
    compilerOptions.freeCompilerArgs.add("-Xcontext-receivers")
}

publishing {
    publications {
        // Maven publication configuration
        create<MavenPublication>("mavenJava") {
            groupId = "io.github.nathlrowe.kotlin-utils"
            artifactId = "kotlin-utils"
            version = "0.1.0"

            from(components["java"])
        }
    }
}
