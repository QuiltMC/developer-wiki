package quilt.internal;

import org.gradle.api.provider.Property;

public abstract class MappingTranslationExtension {
	public abstract Property<Boolean> getMojmapTranslatable();
	public abstract Property<String> getMinecraftVersion();
	public MappingTranslationExtension() {
		getMojmapTranslatable().convention(false);
		getMinecraftVersion().convention("unknown");
	}
}
