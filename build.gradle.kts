import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.transformers.*

plugins {
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	id("com.github.johnrengelman.shadow") version "7.1.2"
	id("org.graalvm.buildtools.native") version "0.9.20"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
}

group = "com.festusyuma"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2022.0.2"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.cloud:spring-cloud-function-web")
	implementation("org.springframework.cloud:spring-cloud-function-adapter-aws")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks {
	jar {
		manifest {
			attributes(
				"Main-Class" to "com.festusyuma.function.functions"
			)
		}
	}
	shadowJar {
		archiveClassifier.set("aws")
		dependencies {
			exclude(
				dependency("org.springframework.cloud:spring-cloud-function-web")
			)
		}
		mergeServiceFiles()
		append("META-INF/spring.handlers")
		append("META-INF/spring.schemas")
		append("META-INF/spring.tooling")
		transform(PropertiesFileTransformer::class.java) {
			val paths = listOf("META-INF/spring.factories")
			val mergeStrategy = "append"
		}
	}
	assemble {
		dependsOn("shadowJar")
	}
}
