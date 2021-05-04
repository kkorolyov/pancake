plugins {
	groovy
}

repositories {
	mavenCentral()
}
dependencies {
	val spockVersion: String by project
	val byteBuddyVersion: String by project

	testImplementation("org.spockframework:spock-core:$spockVersion")
	testImplementation("net.bytebuddy:byte-buddy:$byteBuddyVersion")
}

tasks.test {
	useJUnitPlatform()
}
