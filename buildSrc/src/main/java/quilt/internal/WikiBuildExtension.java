package quilt.internal;

import org.gradle.api.provider.Property;

public abstract class WikiBuildExtension {
	public abstract Property<Boolean> getMojmapTranslatable();

	public WikiBuildExtension() {
		getMojmapTranslatable().convention(true);
	}
}
