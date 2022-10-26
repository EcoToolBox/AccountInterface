package org.kaiaccount.account.inter.type.player;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.type.Account;
import org.kaiaccount.account.inter.type.bank.player.PlayerBankAccount;

import java.util.Collection;

public interface PlayerAccount<Self extends PlayerAccount<Self>> extends Account<Self> {

	@NotNull
	Collection<PlayerBankAccount<?>> getBanks();

	@NotNull
	PlayerBankAccount<?> createBankAccount(@NotNull String name);

	void registerBank(@NotNull PlayerBankAccount<?> account);

	@NotNull
	OfflinePlayer getPlayer();

}
