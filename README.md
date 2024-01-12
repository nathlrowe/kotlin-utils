# kotlin-utils

A collection of simple Kotlin utilities that I found useful in my day-to-day programming.

## Installation

1. Clone the current version of this repository somewhere on your local machine.
2. Open the project and run the Gradle task `kotlin-utils > lib > Tasks > publishing > publishToMavenLocal`.
3. Add the library to your project as a dependency from `mavenLocal`:

```kotlin
repositories {
   mavenLocal()
}

dependencies {
   implementation("io.github.nathlrowe.kotlin-utils:kotlin-utils:$kotlin_utils_version")
}
```
