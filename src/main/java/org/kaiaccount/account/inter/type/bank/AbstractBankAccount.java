package org.kaiaccount.account.inter.type.bank;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.event.TransactionEvent;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.TransactionBuilder;
import org.kaiaccount.account.inter.transfer.TransactionType;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.result.FailedTransactionResult;
import org.kaiaccount.account.inter.transfer.result.SuccessfulTransactionResult;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBankAccount<Self> implements BankAccount<Self> {

	private final Map<Currency, BigDecimal> currencies = new ConcurrentHashMap<>();
	private final @NotNull String bankAccountName;

	public AbstractBankAccount(@NotNull String bankAccountName, @NotNull Map<Currency, BigDecimal> currencies) {
		this.currencies.putAll(currencies);
		this.bankAccountName = bankAccountName;
	}

	@NotNull
	@Override
	public String getBankAccountName() {
		return this.bankAccountName;
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

	private @NotNull TransactionResult withdrawSynced(@NotNull Payment payment) {
		Transaction transaction = new TransactionBuilder().setAccount(this)
				.setPayment(payment)
				.setType(TransactionType.WITHDRAW)
				.build();
		TransactionEvent event = new TransactionEvent(transaction);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return new FailedTransactionResult(transaction,
					event.getCancelledReason().orElseThrow(() -> new RuntimeException("Cannot get reason")));
		}

		BigDecimal current = this.getBalance(payment.getCurrency());
		BigDecimal newValue = current.subtract(transaction.getNewPaymentAmount());
		if (this.currencies.containsKey(payment.getCurrency())) {
			this.currencies.replace(payment.getCurrency(), newValue);
			return new SuccessfulTransactionResult(transaction);
		}
		this.currencies.put(payment.getCurrency(), newValue);
		return new SuccessfulTransactionResult(transaction);
	}

	@NotNull
	@Override
	public CompletableFuture<TransactionResult> withdraw(@NotNull Payment payment) {
		CompletableFuture<TransactionResult> result = new CompletableFuture<>();
		new Thread(() -> result.complete(this.withdrawSynced(payment))).start();
		return result;
	}

	private @NotNull TransactionResult depositSynced(@NotNull Payment payment) {
		Transaction transaction = new TransactionBuilder().setAccount(this)
				.setPayment(payment)
				.setType(TransactionType.DEPOSIT)
				.build();
		TransactionEvent event = new TransactionEvent(transaction);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return new FailedTransactionResult(transaction,
					event.getCancelledReason().orElseThrow(() -> new RuntimeException("No reason specified")));
		}

		BigDecimal current = this.getBalance(payment.getCurrency());
		BigDecimal newValue = current.add(transaction.getNewPaymentAmount());
		if (this.currencies.containsKey(payment.getCurrency())) {
			this.currencies.replace(payment.getCurrency(), newValue);
			return new SuccessfulTransactionResult(transaction);
		}
		this.currencies.put(payment.getCurrency(), newValue);
		return new SuccessfulTransactionResult(transaction);
	}

	@NotNull
	@Override
	public CompletableFuture<TransactionResult> deposit(@NotNull Payment payment) {
		CompletableFuture<TransactionResult> result = new CompletableFuture<>();
		new Thread(() -> result.complete(this.depositSynced(payment))).start();
		return result;
	}
}
