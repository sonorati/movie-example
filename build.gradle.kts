import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.2.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	jacoco
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
}

apply(plugin = "jacoco")

jacoco {
	toolVersion = "0.8.1"
}

group = "com.seon"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
	jcenter()
}

val arrow_version = "0.10.4"
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

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}


tasks.create<JacocoReport>("codeCoverageReport"){
	executionData(fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec"))
	classDirectories.from(fileTree(project.rootDir.absolutePath).include("**/build/classes/kotlin/test/**"))

	reports {
		xml.isEnabled = true
		html.isEnabled = true
		html.destination = file("$buildDir/reports/coverage")
		csv.isEnabled = false
	}
}

tasks.named("codeCoverageReport") {
	dependsOn(tasks.test)
}