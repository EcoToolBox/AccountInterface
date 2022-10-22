package org.kaiaccount.account.inter.type.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.Account;
import org.kaiaccount.account.inter.Currency;
import org.kaiaccount.account.inter.event.TransactionEvent;
import org.kaiaccount.account.inter.io.Serializer;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.TransactionBuilder;
import org.kaiaccount.account.inter.transfer.TransactionType;
import org.kaiaccount.account.inter.transfer.payment.Payment;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerAccount implements Account<PlayerAccount> {

	private final @NotNull OfflinePlayer player;

	private final Map<Currency, BigDecimal> currencies = new ConcurrentHashMap<>();

	public PlayerAccount(@NotNull OfflinePlayer player, Map<Currency, BigDecimal> map) {
		this.player = player;
		this.currencies.putAll(map);
	}

	public @NotNull OfflinePlayer getPlayer() {
		return this.player;
	}

	@NotNull
	@Override
	public BigDecimal getBalance(@NotNull Currency currency) {
		return this.currencies.getOrDefault(currency, BigDecimal.ZERO);
	}

	@NotNull
	@Override
	public Map<Currency, BigDecimal> getBalances() {
		return Collections.unmodifiableMap(this.currencies);
	}

	@NotNull
	@Override
	public CompletableFuture<Transaction> withdraw(@NotNull Payment payment) {
		CompletableFuture<Transaction> result = new CompletableFuture<>();
		new Thread(() -> {

			Transaction transaction = new TransactionBuilder().setAccount(this)
					.setPayment(payment)
					.setType(TransactionType.WITHDRAW)
					.build();
			TransactionEvent event = new TransactionEvent(transaction);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				return;
			}

			BigDecimal current = this.getBalance(payment.getCurrency());
			BigDecimal newValue = current.subtract(transaction.getNewPaymentAmount());
			if (this.currencies.containsKey(payment.getCurrency())) {
				this.currencies.replace(payment.getCurrency(), newValue);
				return;
			}
			this.currencies.put(payment.getCurrency(), newValue);
		}).start();
		return result;
	}

	@NotNull
	@Override
	public CompletableFuture<Transaction> deposit(@NotNull Payment payment) {
		CompletableFuture<Transaction> result = new CompletableFuture<>();
		new Thread(() -> {

			Transaction transaction = new TransactionBuilder().setAccount(this)
					.setPayment(payment)
					.setType(TransactionType.DEPOSIT)
					.build();
			TransactionEvent event = new TransactionEvent(transaction);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				return;
			}

			BigDecimal current = this.getBalance(payment.getCurrency());
			BigDecimal newValue = current.add(transaction.getNewPaymentAmount());
			if (this.currencies.containsKey(payment.getCurrency())) {
				this.currencies.replace(payment.getCurrency(), newValue);
				return;
			}
			this.currencies.put(payment.getCurrency(), newValue);
		}).start();
		return result;
	}

	@Override
	public Serializer<PlayerAccount> getSerializer() {
		return null;
	}
}
