package org.kaiaccount.account.inter.type.player;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;
import org.kaiaccount.account.inter.type.Account;
import org.kaiaccount.account.inter.type.bank.player.PlayerBankAccount;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public interface PlayerAccount<Self extends PlayerAccount<Self>> extends Account {

	@NotNull
	Collection<PlayerBankAccount<?>> getBanks();

	@NotNull
	PlayerBankAccount<?> createBankAccount(@NotNull String name);

	void registerBank(@NotNull PlayerBankAccount<?> account);

	@NotNull
	OfflinePlayer getPlayer();

	@NotNull
	CompletableFuture<TransactionResult> withdrawWithBanks(@NotNull Payment payment);

	@NotNull
	default Map<Currency<?>, BigDecimal> getBalancesWithBanks() {
		Map<Currency<?>, BigDecimal> playerBalance = new HashMap<>(this.getBalances());
		Map<Currency<?>, BigDecimal> bankBalance = this.getBanks()
				.parallelStream()
				.flatMap(bank -> bank.getBalances().entrySet().parallelStream())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, BigDecimal::add));
		playerBalance.putAll(bankBalance);
		return Collections.unmodifiableMap(playerBalance);
	}
}
