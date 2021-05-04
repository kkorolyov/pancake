plugins {
	idea
	java
}

group = "dev.kkorolyov.pancake"

repositories {
	mavenCentral()
	maven {
		url = uri("https://maven.pkg.github.com/kkorolyov/flopple")
		credentials {
			username = System.getenv("GITHUB_ACTOR")
			password = System.getenv("GITHUB_TOKEN")
		}
	}
}

dependencies {
	// stdlib
	val floppleVersion: String by project
	implementation("dev.kkorolyov:flopple:$floppleVersion")

	// observability
	val slf4jVersion: String by project
	implementation("org.slf4j:slf4j-api:$slf4jVersion")
}
dependencyLocking {
	lockAllConfigurations()
}

java {
	sourceCompatibility = JavaVersion.VERSION_14
	targetCompatibility = JavaVersion.VERSION_14

	withSourcesJar()
	withJavadocJar()
}
