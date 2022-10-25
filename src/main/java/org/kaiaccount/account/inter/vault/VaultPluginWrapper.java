package org.kaiaccount.account.inter.vault;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public class VaultPluginWrapper implements Plugin {
	@NotNull
	@Override
	public File getDataFolder() {
		throw new RuntimeException("Should not run");
	}

	@NotNull
	@Override
	public PluginDescriptionFile getDescription() {
		return new PluginDescriptionFile("Vault", "1.7.3", "org.kaiaccount.account.inter.vault.VaultPluginWrapper");
	}

	@NotNull
	@Override
	public FileConfiguration getConfig() {
		throw new RuntimeException("Should not run");
	}

	@Nullable
	@Override
	public InputStream getResource(@NotNull String s) {
		throw new RuntimeException("Should not run");
	}

	@Override
	public void saveConfig() {

	}

	@Override
	public void saveDefaultConfig() {

	}

	@Override
	public void saveResource(@NotNull String s, boolean b) {

	}

	@Override
	public void reloadConfig() {

	}

	@NotNull
	@Override
	public VaultPluginLoaderWrapper getPluginLoader() {
		return new VaultPluginLoaderWrapper();
	}

	@NotNull
	@Override
	public Server getServer() {
		throw new RuntimeException("Should not run");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onLoad() {

	}

	@Override
	public void onEnable() {

	}

	@Override
	public boolean isNaggable() {
		return false;
	}

	@Override
	public void setNaggable(boolean b) {
		throw new RuntimeException("Should not run");
	}

	@Nullable
	@Override
	public ChunkGenerator getDefaultWorldGenerator(@NotNull String s, @Nullable String s1) {
		throw new RuntimeException("Should not run");
	}

	@Nullable
	@Override
	public BiomeProvider getDefaultBiomeProvider(@NotNull String s, @Nullable String s1) {
		throw new RuntimeException("Should not run");
	}

	@NotNull
	@Override
	public Logger getLogger() {
		throw new RuntimeException("Should not run");
	}

	@NotNull
	@Override
	public String getName() {
		return "Vault";
	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
			@NotNull String[] strings) {
		throw new RuntimeException("Should not run");
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
			@NotNull String s,
			@NotNull String[] strings) {
		throw new RuntimeException("Should not run");
	}
}
