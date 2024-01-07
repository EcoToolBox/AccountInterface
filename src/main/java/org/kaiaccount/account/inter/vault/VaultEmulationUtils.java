package org.kaiaccount.account.inter.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;

public final class VaultEmulationUtils {

    private VaultEmulationUtils() {
        throw new RuntimeException("Dont do that");
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
