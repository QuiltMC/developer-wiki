plugins {
	id("java")
	id("wiki-build-logic")
	id("org.quiltmc.loom") version "0.12.+" apply false

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
