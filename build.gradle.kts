import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.collections.listOf

val jUnitVersion = "5.4.2"
val kluentVersion = "1.51"
val spekVersion = "2.0.5"
val arrow_version = "0.10.4"


plugins {
	id("org.springframework.boot") version "2.2.2.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
	jacoco
}

repositories {
	mavenCentral()
	jcenter()
}

group = "com.seon"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	implementation("io.arrow-kt:arrow-fx:$arrow_version")
	implementation("io.arrow-kt:arrow-optics:$arrow_version")
	implementation("io.arrow-kt:arrow-syntax:$arrow_version")
	testImplementation("com.github.tomakehurst:wiremock:2.25.1")
	testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock:2.2.1.RELEASE")
}

jacoco {
	toolVersion = "0.8.4"
}

tasks.jacocoTestReport {
	reports {
		xml.isEnabled = true
		csv.isEnabled = true
		html.isEnabled = true
	}
}

tasks.getByName<Test>("test") {
	extensions.configure(JacocoTaskExtension::class) {
		isEnabled = true
		excludes = listOf("**/MoviesApplication.kt", "**/RestConfig.kt")
		excludeClassLoaders = listOf()
		isIncludeNoLocationClasses = false
	}
}

tasks.withType<Test> {
	finalizedBy(tasks.jacocoTestReport)
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
