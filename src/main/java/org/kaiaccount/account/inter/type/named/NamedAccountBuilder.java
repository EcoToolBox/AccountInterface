package org.kaiaccount.account.inter.type.named;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.inter.currency.Currency;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NamedAccountBuilder {

    private final Map<Currency<?>, BigDecimal> initialBalance = new ConcurrentHashMap<>();
    private String name;

    @CheckReturnValue
    public NamedAccount build() {
        return AccountInterface.getManager().toNamedAccount(this);
    }

    public String getAccountName() {
        return this.name;
    }

    public void setAccountName(@NotNull @Nls String name) {
        this.name = name;
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
