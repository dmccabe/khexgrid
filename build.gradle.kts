plugins {
    base
    java
    kotlin("jvm") version "1.8.10"

    `java-library`
}

val kotlinVersion = "1.8.10"
val gdxVersion = "1.9.6"
val ktxVersion = "1.9.9-b1"
val kotestVersion = "5.6.2"

group = "com.darkgravity"
version = "0.1-SNAPSHOT"

sourceSets["main"].java.srcDirs("src/")
sourceSets["test"].java.srcDirs("test/")

tasks.withType<Test> {
    useJUnitPlatform()
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    implementation("io.github.libktx:ktx-math:$ktxVersion")

    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("com.natpryce:hamkrest:1.4.2.0")
    testRuntimeOnly("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
}
