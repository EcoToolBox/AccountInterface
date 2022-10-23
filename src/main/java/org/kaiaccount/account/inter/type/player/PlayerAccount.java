package org.kaiaccount.account.inter.type.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.Account;
import org.kaiaccount.account.inter.Currency;
import org.kaiaccount.account.inter.event.TransactionEvent;
import org.kaiaccount.account.inter.io.Serializer;
import org.kaiaccount.account.inter.io.Serializers;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.TransactionBuilder;
import org.kaiaccount.account.inter.transfer.TransactionType;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.type.bank.PlayerBankAccount;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedTransferQueue;

public class PlayerAccount implements Account<PlayerAccount> {

	private final @NotNull OfflinePlayer player;

	private final Map<Currency, BigDecimal> currencies = new ConcurrentHashMap<>();
	private final Collection<PlayerBankAccount> banks = new LinkedTransferQueue<>();

	public PlayerAccount(@NotNull OfflinePlayer player) {
		this(player, Collections.emptyMap());
	}

	public PlayerAccount(@NotNull OfflinePlayer player, @NotNull Map<Currency, BigDecimal> map) {
		this.player = player;
		this.currencies.putAll(map);
	}

	public @NotNull Collection<PlayerBankAccount> getBanks() {
		//load others

		return Collections.unmodifiableCollection(this.banks);
	}

	public @NotNull PlayerBankAccount createBankAccount() {
		PlayerBankAccount account = new PlayerBankAccount(this, Collections.emptyMap());
		this.banks.add(account);
		return account;
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

        public @NotNull Transaction withdrawSynced(@NotNull Payment payment) throws Exception {
                Transaction transaction = new TransactionBuilder().setAccount(this)
					.setPayment(payment)
					.setType(TransactionType.WITHDRAW)
					.build();
                TransactionEvent event = new TransactionEvent(transaction);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			throw new Exception(event.getCancelledReason);
		}

		BigDecimal current = this.getBalance(payment.getCurrency());
		BigDecimal newValue = current.subtract(transaction.getNewPaymentAmount());
		if (this.currencies.containsKey(payment.getCurrency())) {
			this.currencies.replace(payment.getCurrency(), newValue);
			return transaction;
		}
		this.currencies.put(payment.getCurrency(), newValue);
                return transaction;
        }

        //This should be TransactionResult
	@NotNull
	@Override
	public CompletableFuture<Transaction> withdraw(@NotNull Payment payment) {
		CompletableFuture<Transaction> result = new CompletableFuture<>();
		new Thread(() -> {
                        try{
                            result.accept(this.withdrawSynced(payment));
                        }catch(Exception e){
                            result.accept(null);
                        }
		
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
		return Serializers.PLAYER_ACCOUNT;
	}

	public static File getFile(@NotNull UUID uuid) {
		return new File("plugins/account/accounts/players/" + uuid + "player.yml");
	}
}
