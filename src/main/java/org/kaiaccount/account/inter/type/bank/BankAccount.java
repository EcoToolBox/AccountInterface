package org.kaiaccount.account.inter.type.bank;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.type.Account;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public interface BankAccount<Self extends BankAccount<Self>> extends Account<Self> {

	@NotNull
	String getBankAccountName();

	@NotNull
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
