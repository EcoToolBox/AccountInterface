package org.kaiaccount.account.inter.type.bank;

import org.jetbrains.annotations.CheckReturnValue;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.kaiaccount.account.inter.type.Account;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public interface BankAccount<Self extends BankAccount<Self>> extends Account {

    @NotNull
    @CheckReturnValue
    String getBankAccountName();

    @NotNull
    @UnmodifiableView
    @CheckReturnValue
    Map<UUID, Collection<BankPermission>> getAccounts();

    @NotNull
    @UnmodifiableView
    @CheckReturnValue
    default Collection<BankPermission> getAccountPermissions(@NotNull OfflinePlayer player) {
        return this.getAccountPermissions(player.getUniqueId());
    }

    @UnmodifiableView
    @NotNull
    @CheckReturnValue
    default Collection<BankPermission> getAccountPermissions(@NotNull UUID playerId) {
        return this.getAccounts()
                .entrySet()
                .parallelStream()
                .filter(entry -> entry.getKey().equals(playerId))
                .findAny()
                .map(Map.Entry::getValue)
                .orElse(Collections.emptyList());
    }


}
