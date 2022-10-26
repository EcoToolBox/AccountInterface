package org.kaiaccount.account.inter.type;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.event.TransactionCompletedEvent;
import org.kaiaccount.account.inter.event.TransactionEvent;
import org.kaiaccount.account.inter.io.Serializer;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.TransactionBuilder;
import org.kaiaccount.account.inter.transfer.TransactionType;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.result.FailedTransactionResult;
import org.kaiaccount.account.inter.transfer.result.SuccessfulTransactionResult;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;
import java.util.function.IntFunction;

public class IsolatedAccount implements Account<IsolatedAccount> {

	private final Map<Currency, BigDecimal> money = new HashMap<>();
	private final LinkedBlockingDeque<UUID> tickets = new LinkedBlockingDeque<>();

	public IsolatedAccount() {
		this(Collections.emptyMap());
	}

	public IsolatedAccount(Map<Currency, BigDecimal> money) {
		this.money.putAll(money);
	}

	@SuppressWarnings({"LoopConditionNotUpdatedInsideLoop", "StatementWithEmptyBody"})
	private UUID awaitTask() {
		UUID uuid = UUID.randomUUID();
		tickets.offerFirst(uuid);
		while (tickets.peekLast() != null && !tickets.peekLast().equals(uuid)) {
		}
		return uuid;
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private void completeTask(UUID uuid) {
		if (tickets.isEmpty()) {
			return;
		}
		tickets.remove(uuid);
	}

	@NotNull
	@Override
	public BigDecimal getBalance(@NotNull Currency currency) {
		return this.money.getOrDefault(currency, BigDecimal.ZERO);
	}

	@NotNull
	@Override
	public Map<Currency, BigDecimal> getBalances() {
		return Collections.unmodifiableMap(this.money);
	}

	public @NotNull TransactionResult withdrawSynced(@NotNull Payment payment, @NotNull Account<?> account) {
		Transaction transaction = new TransactionBuilder().setAccount(account)
				.setPayment(payment)
				.setType(TransactionType.WITHDRAW)
				.build();
		TransactionEvent event = new TransactionEvent(transaction);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			FailedTransactionResult result = new FailedTransactionResult(transaction,
					event.getCancelledReason().orElseThrow(() -> new RuntimeException("No reason specified")));
			Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
			return result;
		}

		BigDecimal current = this.getBalance(payment.getCurrency());
		BigDecimal newValue = current.subtract(transaction.getNewPaymentAmount());
		if (newValue.compareTo(BigDecimal.ZERO) < 0) {
			FailedTransactionResult result = new FailedTransactionResult(transaction,
					"Account does not have " + transaction.getNewPaymentAmount());
			Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
			return result;
		}

		UUID ticket = awaitTask();
		if (this.money.containsKey(payment.getCurrency())) {
			this.money.replace(payment.getCurrency(), newValue);
			completeTask(ticket);
			SuccessfulTransactionResult result = new SuccessfulTransactionResult(transaction);
			Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
			completeTask(ticket);
			return result;
		}
		this.money.put(payment.getCurrency(), newValue);
		SuccessfulTransactionResult result = new SuccessfulTransactionResult(transaction);
		Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
		return result;
	}


	@NotNull
	@Override
	public CompletableFuture<TransactionResult> withdraw(@NotNull Payment payment) {
		return this.withdraw(payment, this);
	}

	@NotNull
	public CompletableFuture<TransactionResult> withdraw(@NotNull Payment payment, @NotNull Account<?> account) {
		CompletableFuture<TransactionResult> result = new CompletableFuture<>();
		new Thread(() -> result.complete(this.withdrawSynced(payment, account))).start();
		return result;
	}

	public @NotNull TransactionResult depositSynced(@NotNull Payment payment, @NotNull Account<?> account) {
		Transaction transaction = new TransactionBuilder().setAccount(account)
				.setPayment(payment)
				.setType(TransactionType.DEPOSIT)
				.build();
		TransactionEvent event = new TransactionEvent(transaction);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			FailedTransactionResult result = new FailedTransactionResult(transaction,
					event.getCancelledReason().orElseThrow(() -> new RuntimeException("No reason specified")));
			Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
			return result;
		}

		BigDecimal current = this.getBalance(payment.getCurrency());
		BigDecimal newValue = current.add(transaction.getNewPaymentAmount());
		UUID ticket = awaitTask();
		if (this.money.containsKey(payment.getCurrency())) {
			this.money.replace(payment.getCurrency(), newValue);
			completeTask(ticket);
			SuccessfulTransactionResult result = new SuccessfulTransactionResult(transaction);
			Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
			return result;
		}
		this.money.put(payment.getCurrency(), newValue);
		completeTask(ticket);
		SuccessfulTransactionResult result = new SuccessfulTransactionResult(transaction);
		Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
		return result;
	}

	@NotNull
	@Override
	public CompletableFuture<TransactionResult> deposit(@NotNull Payment payment) {
		return this.deposit(payment, this);
	}

	@NotNull
	public CompletableFuture<TransactionResult> deposit(@NotNull Payment payment, Account<?> account) {
		CompletableFuture<TransactionResult> result = new CompletableFuture<>();
		new Thread(() -> result.complete(this.depositSynced(payment, account))).start();
		return result;
	}

	public TransactionResult setSynced(@NotNull Payment payment, @NotNull Account<?> account) {
		Transaction transaction = new TransactionBuilder().setAccount(account)
				.setPayment(payment)
				.setType(TransactionType.SET)
				.build();
		TransactionEvent event = new TransactionEvent(transaction);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			FailedTransactionResult result = new FailedTransactionResult(transaction,
					event.getCancelledReason().orElseThrow(() -> new RuntimeException("No reason specified")));
			Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
			return result;
		}

		BigDecimal newValue = transaction.getNewPaymentAmount();
		UUID ticket = awaitTask();
		if (this.money.containsKey(payment.getCurrency())) {
			this.money.replace(payment.getCurrency(), newValue);
			completeTask(ticket);
			SuccessfulTransactionResult result = new SuccessfulTransactionResult(transaction);
			Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
			return result;
		}
		this.money.put(payment.getCurrency(), newValue);
		completeTask(ticket);
		SuccessfulTransactionResult result = new SuccessfulTransactionResult(transaction);
		Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
		return result;
	}

	@NotNull
	@Override
	public CompletableFuture<TransactionResult> set(@NotNull Payment payment) {
		return this.set(payment, this);
	}

	@NotNull
	public CompletableFuture<TransactionResult> set(@NotNull Payment payment, @NotNull Account<?> account) {
		CompletableFuture<TransactionResult> result = new CompletableFuture<>();
		new Thread(() -> result.complete(this.setSynced(payment, account))).start();
		return result;
	}

	@NotNull
	@Override
	public CompletableFuture<String> multipleTransaction(
			@NotNull Function<IsolatedAccount, CompletableFuture<TransactionResult>>... transactions) {
		IsolatedAccount copy = this.copy();
		CompletableFuture<String> ret = new CompletableFuture<>();
		CompletableFuture<TransactionResult>[] list =
				Arrays.stream(transactions).parallel().map(function -> function.apply(copy)).toArray(
						(IntFunction<CompletableFuture<TransactionResult>[]>) CompletableFuture[]::new);
		CompletableFuture.allOf(list).thenAccept(v -> {
			String resetReason = null;
			for (CompletableFuture<TransactionResult> result : list) {
				try {
					TransactionResult transaction = result.get();
					if (transaction instanceof FailedTransactionResult failed) {
						resetReason = failed.getFailReason();
						break;
					}
				} catch (InterruptedException | ExecutionException e) {
					resetReason = e.getMessage();
					break;
				}
			}
			if (resetReason != null) {
				ret.complete(resetReason);
				return;
			}
			UUID ticket = awaitTask();

			this.money.clear();
			this.money.putAll(copy.money);
			ret.complete(null);
			completeTask(ticket);
		});
		return ret;
	}

	@Override
	public Serializer<IsolatedAccount> getSerializer() {
		throw new RuntimeException("Cannot save");
	}

	public IsolatedAccount copy() {
		return new IsolatedAccount(new HashMap<>(this.money));
	}
}
