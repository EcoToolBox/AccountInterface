package org.kaiaccount.account.inter.type;

import org.jetbrains.annotations.CheckReturnValue;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.event.TransactionCompletedEvent;
import org.kaiaccount.account.inter.event.TransactionEvent;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.TransactionBuilder;
import org.kaiaccount.account.inter.transfer.TransactionType;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.payment.PaymentBuilder;
import org.kaiaccount.account.inter.transfer.result.SingleTransactionResult;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;
import org.kaiaccount.account.inter.transfer.result.failed.FailedTransactionResult;
import org.kaiaccount.account.inter.transfer.result.failed.MultipleFailedTransactionResult;
import org.kaiaccount.account.inter.transfer.result.failed.SingleFailedTransactionResult;
import org.kaiaccount.account.inter.transfer.result.successful.MultipleSuccessfulTransactionResult;
import org.kaiaccount.account.inter.transfer.result.successful.SingleSuccessfulTransactionResult;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;
import java.util.stream.Stream;

public class IsolatedAccount implements AccountSynced {

    private final Map<Currency<?>, BigDecimal> money = new HashMap<>();
    private final LinkedBlockingDeque<UUID> tickets = new LinkedBlockingDeque<>();

    public IsolatedAccount() {
        this(Collections.emptyMap());
    }

    public IsolatedAccount(@NotNull Map<Currency<?>, BigDecimal> money) {
        this.money.putAll(money);
    }

    public @NotNull UUID awaitTask() {
        return awaitTask(false);
    }

    @SuppressWarnings({"LoopConditionNotUpdatedInsideLoop", "StatementWithEmptyBody"})
    @NotNull
    public UUID awaitTask(boolean priority) {
        UUID uuid = UUID.randomUUID();
        if (priority) {
            tickets.offerLast(uuid);
        } else {
            tickets.offerFirst(uuid);
        }
        while (tickets.peekLast() != null && !tickets.peekLast().equals(uuid)) {
        }
        return uuid;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void completeTask(@NotNull UUID uuid) {
        if (tickets.isEmpty()) {
            return;
        }
        tickets.remove(uuid);
    }

    @NotNull
    @Override
    @CheckReturnValue
    public BigDecimal getBalance(@NotNull Currency<?> currency) {
        return this.money.getOrDefault(currency, BigDecimal.ZERO);
    }

    @NotNull
    @Override
    @UnmodifiableView
    @CheckReturnValue
    public Map<Currency<?>, BigDecimal> getBalances() {
        return Collections.unmodifiableMap(this.money);
    }

    @NotNull
    @CheckReturnValue
    @Override
    public SingleTransactionResult withdrawSynced(@NotNull Payment payment) {
        return this.withdrawSynced(payment, this);
    }

    @CheckReturnValue
    public @NotNull SingleTransactionResult withdrawSynced(@NotNull Payment payment, @NotNull Account account) {
        Transaction transaction = new TransactionBuilder().setAccount(account)
                .setPayment(payment)
                .setType(TransactionType.WITHDRAW)
                .build();
        TransactionEvent event = new TransactionEvent(transaction);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            SingleFailedTransactionResult result = new SingleFailedTransactionResult(
                    event.getCancelledReason().orElseThrow(() -> new RuntimeException("No reason specified")),
                    transaction);
            Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
            return result;
        }

        BigDecimal current = this.getBalance(payment.getCurrency());
        BigDecimal newValue = current.subtract(transaction.getNewPaymentAmount());
        if (newValue.compareTo(BigDecimal.ZERO) < 0) {
            SingleFailedTransactionResult result = new SingleFailedTransactionResult(
                    "Account does not have " + transaction.getNewPaymentAmount(), transaction);
            Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
            return result;
        }

        UUID ticket = awaitTask(payment.isPriority());
        if (this.money.containsKey(payment.getCurrency())) {
            this.money.replace(payment.getCurrency(), newValue);
            completeTask(ticket);
            SingleSuccessfulTransactionResult result = new SingleSuccessfulTransactionResult(transaction);
            Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
            completeTask(ticket);
            return result;
        }
        this.money.put(payment.getCurrency(), newValue);
        SingleSuccessfulTransactionResult result = new SingleSuccessfulTransactionResult(transaction);
        Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
        return result;
    }


    @NotNull
    @Override
    @Async.Execute
    @CheckReturnValue
    public CompletableFuture<SingleTransactionResult> withdraw(@NotNull Payment payment) {
        return this.withdraw(payment, this);
    }

    @NotNull
    @Async.Execute
    @CheckReturnValue
    public CompletableFuture<SingleTransactionResult> withdraw(@NotNull Payment payment, @NotNull Account account) {
        CompletableFuture<SingleTransactionResult> result = new CompletableFuture<>();
        new Thread(() -> result.complete(this.withdrawSynced(payment, account))).start();
        return result;
    }

    @NotNull
    @Override
    @CheckReturnValue
    public SingleTransactionResult depositSynced(@NotNull Payment payment) {
        return this.depositSynced(payment, this);
    }

    @CheckReturnValue
    public @NotNull SingleTransactionResult depositSynced(@NotNull Payment payment, @NotNull Account account) {
        Transaction transaction = new TransactionBuilder().setAccount(account)
                .setPayment(payment)
                .setType(TransactionType.DEPOSIT)
                .build();
        TransactionEvent event = new TransactionEvent(transaction);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            SingleFailedTransactionResult result = new SingleFailedTransactionResult(
                    event.getCancelledReason().orElseThrow(() -> new RuntimeException("No reason specified")),
                    transaction);
            Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
            return result;
        }

        BigDecimal current = this.getBalance(payment.getCurrency());
        BigDecimal newValue = current.add(transaction.getNewPaymentAmount());
        UUID ticket = awaitTask(payment.isPriority());
        if (this.money.containsKey(payment.getCurrency())) {
            this.money.replace(payment.getCurrency(), newValue);
            completeTask(ticket);
            SingleSuccessfulTransactionResult result = new SingleSuccessfulTransactionResult(transaction);
            Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
            return result;
        }
        this.money.put(payment.getCurrency(), newValue);
        completeTask(ticket);
        SingleSuccessfulTransactionResult result = new SingleSuccessfulTransactionResult(transaction);
        Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
        return result;
    }

    @NotNull
    @Override
    @Async.Execute
    @CheckReturnValue
    public CompletableFuture<SingleTransactionResult> deposit(@NotNull Payment payment) {
        return this.deposit(payment, this);
    }

    @NotNull
    @Async.Execute
    @CheckReturnValue
    public CompletableFuture<SingleTransactionResult> deposit(@NotNull Payment payment, @NotNull Account account) {
        CompletableFuture<SingleTransactionResult> result = new CompletableFuture<>();
        new Thread(() -> result.complete(this.depositSynced(payment, account))).start();
        return result;
    }

    @NotNull
    @Override
    @CheckReturnValue
    public SingleTransactionResult setSynced(@NotNull Payment payment) {
        return this.setSynced(payment, this);
    }

    @NotNull
    @Override
    @CheckReturnValue
    public SingleTransactionResult refundSynced(@NotNull Transaction payment) {
        return this.refundSynced(payment, this);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void forceSetSynced(@NotNull Payment payment) {
        this.forceSetSynced(payment, this);
    }

    @NotNull
    @Async.Execute
    @CheckReturnValue
    public CompletableFuture<TransactionResult> multipleTransaction(
            @NotNull Function<IsolatedAccount, CompletableFuture<? extends TransactionResult>>... transactions) {
        IsolatedAccount copy = this.copy();
        CompletableFuture<? extends TransactionResult>[] array =
                Stream.of(transactions).parallel().map(fun -> fun.apply(copy)).toArray(
                        CompletableFuture[]::new);
        CompletableFuture<TransactionResult> future = new CompletableFuture<>();
        CompletableFuture.allOf(array).thenAccept(ret -> {
            Collection<Transaction> results = new LinkedList<>();
            for (CompletableFuture<? extends TransactionResult> com : array) {
                try {
                    TransactionResult transaction = com.get();
                    results.addAll(transaction.getTransactions());
                    if (transaction instanceof FailedTransactionResult fail) {
                        future.complete(new MultipleFailedTransactionResult(fail.getReason(), results));
                        return;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    future.complete(
                            new MultipleFailedTransactionResult("Technical reason: " + e.getMessage(), results));
                    return;
                }
            }
            future.complete(new MultipleSuccessfulTransactionResult(results));
        });


        return future;
    }

    @NotNull
    @CheckReturnValue
    public SingleSuccessfulTransactionResult forceSetSynced(@NotNull Payment payment, @NotNull Account account) {
        Transaction transaction =
                new TransactionBuilder().setAccount(account).setPayment(payment).setType(TransactionType.SET).build();
        UUID ticket = awaitTask(payment.isPriority());
        if (this.money.containsKey(payment.getCurrency())) {
            this.money.replace(payment.getCurrency(), payment.getAmount());
        } else {
            this.money.put(payment.getCurrency(), payment.getAmount());
        }
        this.completeTask(ticket);

        SingleSuccessfulTransactionResult result = new SingleSuccessfulTransactionResult(transaction);
        TransactionCompletedEvent event = new TransactionCompletedEvent(result);
        Bukkit.getPluginManager().callEvent(event);
        return result;
    }

    @NotNull
    @CheckReturnValue
    public SingleTransactionResult refundSynced(@NotNull Transaction payment, @NotNull Account account) {
        BigDecimal amount = this.getBalance(payment.getCurrency());
        switch (payment.getType()) {
            case WITHDRAW -> amount = amount.add(payment.getNewPaymentAmount());
            case SET -> amount = payment.getNewPaymentAmount();
            case DEPOSIT -> amount = amount.subtract(payment.getNewPaymentAmount());
            default -> throw new RuntimeException("Unknown transaction type of " + payment.getType().name());
        }
        return this.forceSetSynced(new PaymentBuilder().setCurrency(payment.getCurrency())
                .setAmount(amount)
                .setReason("refund")
                .build(payment.getPayment()
                        .getPlugin()), account);
    }

    @NotNull
    @CheckReturnValue
    public SingleTransactionResult setSynced(@NotNull Payment payment, @NotNull Account account) {
        Transaction transaction = new TransactionBuilder().setAccount(account)
                .setPayment(payment)
                .setType(TransactionType.SET)
                .build();
        TransactionEvent event = new TransactionEvent(transaction);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            SingleFailedTransactionResult result = new SingleFailedTransactionResult(
                    event.getCancelledReason().orElseThrow(() -> new RuntimeException("No reason specified")),
                    transaction);
            Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
            return result;
        }

        BigDecimal newValue = transaction.getNewPaymentAmount();
        UUID ticket = awaitTask(payment.isPriority());
        if (this.money.containsKey(payment.getCurrency())) {
            this.money.replace(payment.getCurrency(), newValue);
            completeTask(ticket);
            SingleSuccessfulTransactionResult result = new SingleSuccessfulTransactionResult(transaction);
            Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
            return result;
        }
        this.money.put(payment.getCurrency(), newValue);
        completeTask(ticket);
        SingleSuccessfulTransactionResult result = new SingleSuccessfulTransactionResult(transaction);
        Bukkit.getPluginManager().callEvent(new TransactionCompletedEvent(result));
        return result;
    }

    @NotNull
    @Override
    @Async.Execute
    @CheckReturnValue
    public CompletableFuture<SingleTransactionResult> set(@NotNull Payment payment) {
        return this.set(payment, this);
    }

    @NotNull
    @Override
    @Async.Execute
    @CheckReturnValue
    public CompletableFuture<SingleTransactionResult> refund(@NotNull Transaction payment) {
        return refund(payment, this);
    }

    @NotNull
    @Override
    @Async.Execute
    public CompletableFuture<Void> forceSet(@NotNull Payment payment) {
        return forceSet(payment, this);
    }

    @Async.Execute
    public @NotNull CompletableFuture<Void> forceSet(@NotNull Payment payment, Account account) {
        CompletableFuture<Void> result = new CompletableFuture<>();
        new Thread(() -> {
            this.forceSetSynced(payment);
            result.complete(null);
        }).start();
        return result;
    }

    @Async.Execute
    @CheckReturnValue
    public @NotNull CompletableFuture<SingleTransactionResult> refund(@NotNull Transaction payment,
                                                                      @NotNull Account account) {
        CompletableFuture<SingleTransactionResult> result = new CompletableFuture<>();
        new Thread(() -> result.complete(this.refundSynced(payment, account))).start();
        return result;
    }

    @NotNull
    @Async.Execute
    @CheckReturnValue
    public CompletableFuture<SingleTransactionResult> set(@NotNull Payment payment, @NotNull Account account) {
        CompletableFuture<SingleTransactionResult> result = new CompletableFuture<>();
        new Thread(() -> result.complete(this.setSynced(payment, account))).start();
        return result;
    }

    public @NotNull IsolatedAccount copy() {
        return new IsolatedAccount(new HashMap<>(this.money));
    }
}
