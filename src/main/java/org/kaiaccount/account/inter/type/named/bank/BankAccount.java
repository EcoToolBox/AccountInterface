package org.kaiaccount.account.inter.type.named.bank;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.kaiaccount.account.inter.type.named.NamedAccountLike;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public interface BankAccount extends NamedAccountLike {

    @NotNull
    @Nls
    @Deprecated(forRemoval = true)
    default String getBankAccountName() {
        return this.getAccountName();
    }

    @NotNull
    @UnmodifiableView
    Map<UUID, Collection<BankPermission>> getAccounts();



    default Collection<BankPermission> getAccountPermissions(@NotNull OfflinePlayer player) {
        return this.getAccountPermissions(player.getUniqueId());
    }

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
