package org.kaiaccount.account.inter.type.named;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.utils.builder.Builder;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NamedAccountBuilder implements Builder<NamedAccount, NamedAccountBuilder> {

    private final Map<Currency<?>, BigDecimal> initialBalance = new ConcurrentHashMap<>();
    private String name;

    @CheckReturnValue
    @Override
    public NamedAccount build() {
        return AccountInterface.getManager().toNamedAccount(this);
    }

    @Override
    public NamedAccountBuilder from(NamedAccountBuilder builder) {
        this.setInitialBalance(builder.getInitialBalance());
        this.setAccountName(builder.getAccountName());
        return this;
    }

    public String getAccountName() {
        return this.name;
    }

    public NamedAccountBuilder setAccountName(@NotNull @Nls String name) {
        this.name = name;
        return this;
    }

    public Map<Currency<?>, BigDecimal> getInitialBalance() {
        return this.initialBalance;
    }

    public NamedAccountBuilder setInitialBalance(Map<Currency<?>, BigDecimal> map) {
        this.initialBalance.clear();
        this.initialBalance.putAll(map);
        return this;
    }
}
