package org.kaiaccount.account.inter.transfer;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.payment.PaymentBuilder;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;
import org.kaiaccount.account.inter.transfer.result.failed.FailedTransactionResult;
import org.kaiaccount.account.inter.transfer.result.failed.MultipleFailedTransactionResult;
import org.kaiaccount.account.inter.transfer.result.successful.MultipleSuccessfulTransactionResult;
import org.kaiaccount.account.inter.type.Account;
import org.kaiaccount.account.inter.type.AccountType;
import org.kaiaccount.account.inter.type.IsolatedAccount;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IsolatedTransaction {

    private final Map<AccountType, IsolatedAccount> accounts;
    private final Function<Map<AccountType, IsolatedAccount>, Collection<CompletableFuture<?
            extends TransactionResult>>>
            function;

    public IsolatedTransaction(
            Function<Map<AccountType, IsolatedAccount>, Collection<CompletableFuture<? extends TransactionResult>>> complete,
            AccountType... accounts) {
        this(complete, Arrays.asList(accounts));
    }

    public IsolatedTransaction(
            Function<Map<AccountType, IsolatedAccount>, Collection<CompletableFuture<? extends TransactionResult>>> complete,
            Collection<AccountType> accounts) {
        this.accounts =
                accounts.parallelStream().collect(Collectors.toMap(acc -> acc, acc -> acc.getIsolated().copy()));
        this.function = complete;
    }

    @CheckReturnValue
    private @NotNull AccountType getTypeFrom(@NotNull Account isolated) {
        return this.accounts.entrySet()
                .parallelStream()
                .filter(entry -> entry.getValue().equals(isolated))
                .findAny()
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RuntimeException("Account doesnt have key"));
    }

    @CheckReturnValue
    public @NotNull CompletableFuture<TransactionResult> start() {
        CompletableFuture<TransactionResult> ret = new CompletableFuture<>();
        Collection<CompletableFuture<? extends TransactionResult>> toProcess = this.function.apply(this.accounts);
        CompletableFuture<Void> await = CompletableFuture.allOf(toProcess.toArray(CompletableFuture[]::new));
        await.thenAccept(v -> {
            try {
                List<Transaction> transactions = new LinkedList<>();
                for (CompletableFuture<? extends TransactionResult> futureResult : toProcess) {
                    TransactionResult result = futureResult.get();
                    transactions.addAll(result.getTransactions());
                    if (!(result instanceof FailedTransactionResult failed)) {
                        continue;
                    }
                    ret.complete(new MultipleFailedTransactionResult(failed.getReason(), transactions));
                    break;
                }

                transactions.parallelStream().forEach(transaction -> {
                    AccountType realAccount = getTypeFrom(transaction.getTarget());
                    BigDecimal amount = transaction.getTarget().getBalance(transaction.getCurrency());
                    Payment payment = new PaymentBuilder()
                            .setAmount(amount)
                            .setCurrency(transaction.getCurrency())
                            .setPriority(true)
                            .build(transaction.getPayment()
                                    .getPlugin());
                    realAccount.forceSet(payment);
                });
                ret.complete(new MultipleSuccessfulTransactionResult(transactions));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                ret.complete(new MultipleFailedTransactionResult("Technical reason: " + e.getMessage()));
            }
        });
        return ret;
    }
}
