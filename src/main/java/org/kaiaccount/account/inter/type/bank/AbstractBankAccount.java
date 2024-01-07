package org.kaiaccount.account.inter.type.bank;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.transfer.IsolatedTransaction;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;
import org.kaiaccount.account.inter.type.AccountType;
import org.kaiaccount.account.inter.type.IsolatedAccount;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class AbstractBankAccount<Self extends AbstractBankAccount<Self>>
        implements BankAccount<Self>, AccountType {

    private final @NotNull String bankAccountName;
    private final @NotNull IsolatedAccount account;

    public AbstractBankAccount(@NotNull String bankAccountName, @NotNull Map<Currency<?>, BigDecimal> currencies) {
        account = new IsolatedAccount(currencies);
        this.bankAccountName = bankAccountName;
    }

    @NotNull
    @Override
    @CheckReturnValue
    public String getBankAccountName() {
        return this.bankAccountName;
    }

    @Override
    @CheckReturnValue
    public @NotNull IsolatedAccount getIsolated() {
        return this.account;
    }

    @NotNull
    @CheckReturnValue
    @Override
    public CompletableFuture<TransactionResult> multipleTransaction(
            @NotNull Function<IsolatedAccount, CompletableFuture<? extends TransactionResult>>... transactions) {
        CompletableFuture<TransactionResult> ret = new CompletableFuture<>();
        new IsolatedTransaction(map -> {
            IsolatedAccount isolated = map.get(this);
            Stream<CompletableFuture<? extends TransactionResult>> stream = Arrays
                    .stream(transactions)
                    .parallel()
                    .map(f -> f.apply(isolated));
            return stream.toList();
        }, this)
                .start()
                .thenAccept(ret::complete);
        return ret;
    }
}
