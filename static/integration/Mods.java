/**
 * An enum class containing id's for specific mods which can be referenced directly
 *
 * @author Originally by the Create Team (Forge). Modified by SleepyEvelyn for QuiltMC
 * @see <a href="https://github.com/Creators-of-Create/Create/blob/mc1.18/dev/src/main/java/com/simibubi/create/compat/Mods.java">Mods.java (1.18)</a>
 **/
public enum Mods {

    EXAMPLE_MOD,
    BOTANIA,
    CREATE;

    private final String id;

    Mods() {
	// Convert the name into a mod id by translating to lowercase and replacing "_" characters with "-"
	this.id = name().toLowerCase(Locale.ENGLISH).replace("_", "-");
    }

    Mods(String id) {
	// Used for when the readable mod name does not match the id
	this.id = id;
    }

    public String id() {
	return id;
    }

    public Optional<ModContainer> modContainer() {
	return QuiltLoader.getModContainer(id);
    }

    public boolean isLoaded() {
	return QuiltLoader.isModLoaded(id);
    }

    public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
	return isLoaded() ? Optional.of(toRun.get().get()) : Optional.empty();
    }

    public void executeIfInstalled(Supplier<Runnable> toExecute) {
	if(isLoaded())
		toExecute.get().run();
    }

    // Access a mods registry easily
    public <T> Optional<T> getRegisteredObject(Registry<T> registry, String path) {
	return registry.getOrEmpty(new Identifier(id, path));
    }
}
