package org.kaiaccount.account.inter.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class VaultEmulationUtils {

    private VaultEmulationUtils() {
        throw new RuntimeException("Dont do that");
    }

    public static @NotNull Plugin getReason() {
        String[] ignore = new String[]{"org.kaiaccount", "net.milkbowl.vault"};
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement trace : stackTrace) {
            String fullName = trace.getClass().getPackageName();
            if (Stream.of(ignore).anyMatch(fullName::startsWith)) {
                continue;
            }
            Optional<Plugin> opPlugin = Stream.of(plugins).map(plugin -> {
                String main = plugin.getDescription().getMain();
                int matched = IntStream
                        .range(0, Math.min(main.length(), fullName.length()))
                        .filter(index -> main.charAt(index) != fullName.charAt(index))
                        .findFirst()
                        .orElse(0);
                return Map.entry(plugin, matched);
            }).max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
            if (opPlugin.isPresent()) {
                return opPlugin.get();
            }
        }
        return Objects.requireNonNull(AccountInterface.getManager().getVaultPlugin());
    }


    public static @NotNull KaiEco loadService(@NotNull Plugin plugin) {
        KaiEco eco = new KaiEco(() -> AccountInterface.getManager().getDefaultCurrency(), 3);
        loadService(eco, plugin);
        return eco;
    }

    public static void loadService(@NotNull Economy eco, @NotNull Plugin plugin) {
        Bukkit.getServicesManager().register(Economy.class, eco, plugin, ServicePriority.Normal);
    }
}
