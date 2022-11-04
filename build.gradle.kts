plugins {
	id("java")
    id("idea") // Needed for scala support in Intellij strangely
	id("wiki-build-logic")

	id("com.github.node-gradle.node")
}

group = "org.quiltmc"
version = "0.1-SNAPSHOT"

allprojects {
	apply(plugin = "java")
}

node {
	download.set(true)
}

tasks.generateWiki {
	dependsOn(tasks.npmInstall)
}
