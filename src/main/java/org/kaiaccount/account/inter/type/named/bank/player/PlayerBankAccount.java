package org.kaiaccount.account.inter.type.named.bank.player;

import org.bukkit.entity.AnimalTamer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.type.named.bank.BankAccount;
import org.kaiaccount.account.inter.type.named.bank.BankPermission;
import org.kaiaccount.account.inter.type.player.PlayerAccount;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public interface PlayerBankAccount extends BankAccount {

    @NotNull
    PlayerAccount<?> getAccountHolder();

    @Deprecated
    default void addAccount(@NotNull AnimalTamer ignored) {
        throw new RuntimeException("Cannot add an account without permissions");
    }

    default void addAccount(@NotNull AnimalTamer uuid, @NotNull BankPermission... permissions) {
        this.addAccount(uuid.getUniqueId(), permissions);
    }

    default void addAccount(@NotNull AnimalTamer player, Collection<BankPermission> permissions) {
        this.addAccount(player.getUniqueId(), permissions);
    }

    @Deprecated
    default void addAccount(@NotNull UUID ignored) {
        throw new RuntimeException("Cannot add an account without permissions");
    }

    default void addAccount(@NotNull UUID uuid, @NotNull BankPermission... permissions) {
        this.addAccount(uuid, Arrays.asList(permissions));
    }

    void addAccount(@NotNull UUID uuid, Collection<BankPermission> permissions);

    default void removeAccount(@NotNull AnimalTamer player) {
        this.removeAccount(player.getUniqueId());
    }

    void removeAccount(@NotNull UUID uuid);


}
