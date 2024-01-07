package org.kaiaccount;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;


/**
 * This is the main class for AccountInterface. This will hold the manager for all your needs, but that is only what
 * you should be accessing this for
 */
public class AccountInterface {

    private static AccountInterface instance;
    private @Nullable AccountInterfaceManager manager;
    private @Nullable Plugin host;

    public AccountInterface() {
        instance = this;
    }

    /**
     * Checks if a currency plugin has taken ownership and provided a {@link AccountInterfaceManager} to the Bukkit
     * services
     *
     * @return true if the manager can be found
     */
    public static synchronized boolean isReady() {
        if (getInstance().manager != null) {
            return true;
        }
        Optional<AccountInterfaceManager> opManager = getInstance().getManagerFromService();
        if (opManager.isEmpty()) {
            return false;
        }
        getInstance().manager = opManager.get();
        return true;
    }

    /**
     * Gets the manager (if ready)
     *
     * @return The manager for all things AccountInterface
     */
    public static @NotNull AccountInterfaceManager getManager() {
        if (!isReady()) {
            throw new RuntimeException("Currency plugin has not registered itself yet");
        }
        return Objects.requireNonNull(getInstance().manager);
    }

    /**
     * Gets AccountInterface Bukkit plugin
     *
     * @return this plugin
     */
    @NotNull
    public static AccountInterface getInstance() {
        if (instance == null) {
            instance = new AccountInterface();
        }
        return instance;
    }

    public @NotNull Plugin getHostPlugin() {
        if (this.host == null) {
            this.host = this.getPluginFromService().orElseThrow(() -> new RuntimeException("No Economy plugin has been registered yet"));
        }
        return this.host;
    }

    @NotNull
    private Optional<AccountInterfaceManager> getManagerFromService() {
        return getFromService(RegisteredServiceProvider::getProvider);
    }

    @NotNull
    private Optional<Plugin> getPluginFromService() {
        return getFromService(RegisteredServiceProvider::getPlugin);
    }

    @NotNull
    private <T> Optional<T> getFromService(@NotNull Function<RegisteredServiceProvider<AccountInterfaceManager>, T> function) {
        RegisteredServiceProvider<AccountInterfaceManager> reg =
                Bukkit.getServicesManager().getRegistration(AccountInterfaceManager.class);
        if (reg == null) {
            return Optional.empty();
        }
        return Optional.of(function.apply(reg));
    }


}
