plugins {
	java
	groovy
	id("me.champeau.jmh")
}

fun bundles(name: String) = the<VersionCatalogsExtension>().named("libs").findBundle(name).get()

repositories {
	mavenCentral()
	maven {
		url = uri("https://maven.pkg.github.com/kkorolyov/flub")
		credentials {
			username = System.getenv("GITHUB_ACTOR")
			password = System.getenv("GITHUB_TOKEN")
		}
	}
	maven {
		url = uri("https://central.sonatype.com/repository/maven-snapshots/")
		content {
			includeGroup("org.spockframework")
		}
	}
}

dependencyLocking {
	lockAllConfigurations()
}

dependencies {
	implementation(bundles("stdlib"))
	testImplementation(bundles("test"))
	testRuntimeOnly(bundles("testRuntime"))
}

tasks.test {
	useJUnitPlatform()
}

tasks.jmhJar {
	notCompatibleWithConfigurationCache("serializes project")
}
