plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    `maven-publish`
    alias(libs.plugins.serialization)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.datetime)

    implementation(libs.serialization.json)

    implementation(libs.commons.cli)
    implementation(libs.commons.compress)
    implementation(libs.commons.csv)
    implementation(libs.commons.dbcp)
    implementation(libs.commons.math)
    implementation(libs.commons.net)

    implementation(libs.mysql)

    implementation(libs.jakarta.mail)
    implementation(libs.jakarta.activation)

    implementation(libs.log4j.api)
    implementation(libs.log4j.core)

    // TODO upgrade to itext 8
    implementation(libs.itext)

    implementation(libs.poi.core)
    implementation(libs.poi.ooxml)

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    // Set Java version
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
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
