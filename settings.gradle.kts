rootProject.name = "wiki-prototype"

fun addProjects(root: String, gradlePath: String) {
	rootProject.projectDir.toPath().resolve(root).toFile().listFiles()?.forEach {
		if (it.isDirectory && File(it, "build.gradle").exists()) {
			include("$gradlePath:${it.name}")
			val project = project("$gradlePath:${it.name}")
			project.projectDir = it;

			addProjects(it.path, "$gradlePath:${it.name}");
		}
	}
}

addProjects("wiki/versions", ":versions")
addProjects("wiki/libraries", ":libraries")

pluginManagement {
	repositories {
		maven {
			name = "Quilt"
			url = uri("https://maven.quiltmc.org/repository/release")
		}
		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
		gradlePluginPortal()
	}
}
