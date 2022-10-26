package org.kaiaccount.account.inter.vault;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class VaultPluginLoaderWrapper implements PluginLoader {
	@NotNull
	@Override
	public Plugin loadPlugin(@NotNull File file) throws UnknownDependencyException {
		throw new RuntimeException("Should not run -> use VaultEmulationUtils.loadVault()");
	}

	@NotNull
	@Override
	public PluginDescriptionFile getPluginDescription(@NotNull File file) {
		throw new RuntimeException("Should not run");
	}

	@NotNull
	@Override
	public Pattern[] getPluginFileFilters() {
		throw new RuntimeException("Should not run");
	}

	@NotNull
	@Override
	public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(@NotNull Listener listener,
			@NotNull Plugin plugin) {
		throw new RuntimeException("Should not run");
	}

	@Override
	public void enablePlugin(@NotNull Plugin plugin) {

	}

	@Override
	public void disablePlugin(@NotNull Plugin plugin) {

	}
}
