package org.kaiaccount.account.inter.type.named;

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

public abstract class AbstractNamedAccountLike
        implements NamedAccountLike, AccountType {

    private final @NotNull String accountName;
    private final @NotNull IsolatedAccount account;

    public AbstractNamedAccountLike(@NotNull String accountName, @NotNull Map<Currency<?>, BigDecimal> currencies) {
        account = new IsolatedAccount(currencies);
        this.accountName = accountName;
    }

    @NotNull
    @Override
    public String getAccountName() {
        return this.accountName;
    }

    @Override
    public IsolatedAccount getIsolated() {
        return this.account;
    }

    @NotNull
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
