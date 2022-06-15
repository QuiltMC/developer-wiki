plugins {
	id("java")
	id("wiki-build-logic")
	id("org.quiltmc.loom") version "0.12.+" apply false
}

group = "org.quiltmc"
version = "0.1-SNAPSHOT"

allprojects {
	apply(plugin = "java")
}
