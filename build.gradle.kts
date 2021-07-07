plugins {
    base
    java
    kotlin("jvm") version "1.4.10"
}

val kotlinVersion = "1.4.10"
val gdxVersion = "1.9.6"
val ktxVersion = "1.9.9-b1"
val spekVersion = "2.0.14"

sourceSets["main"].java.srcDirs("src/")
sourceSets["test"].java.srcDirs("test/")

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines("spek2")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    implementation("io.github.libktx:ktx-math:$ktxVersion")

    testImplementation("com.natpryce:hamkrest:1.4.2.0")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
    testRuntimeOnly("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
}
