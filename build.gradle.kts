import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
	idea

	id("io.spring.dependency-management") version "1.0.7.RELEASE"

	val kotlinVersion = "1.3.21"
	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion

	id("org.springframework.boot") version "2.1.3.RELEASE"
}

apply(plugin = "io.spring.dependency-management")

group = "com.example"
version = "0.0.1-SNAPSHOT"

configurations {
	compileOnly {
		extendsFrom(configurations["annotationProcessor"])
	}
}

repositories {
	mavenCentral()
}

configure<DependencyManagementExtension> {
	dependencies {
		dependency("org.junit.jupiter:junit-jupiter:5.4.1")
	}
}

dependencies {
	// Kotlin
	implementation(kotlin("stdlib-jdk8"))
	implementation(kotlin("reflect"))
	// Spring Boot
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "junit")
	}
	// Dev Tools
	runtimeOnly("org.springframework.boot:spring-boot-devtools")
	// Configuration Processor
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	// JUnit5
	testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinJvmCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = java.targetCompatibility.toString()
	}
}

idea {
	project {
		languageLevel = IdeaLanguageLevel(java.targetCompatibility)
	}
	module {
		inheritOutputDirs = false
		outputDir = the<JavaPluginConvention>().sourceSets["main"].java.outputDir
		testOutputDir = the<JavaPluginConvention>().sourceSets["test"].java.outputDir
	}
}

tasks.test {
	useJUnitPlatform()
}
