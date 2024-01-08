package org.kaiaccount.account.inter.type.named;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;

import java.math.BigDecimal;
import java.util.Map;

public class AbstractNamedAccount extends AbstractNamedAccountLike implements NamedAccount {
    public AbstractNamedAccount(@NotNull String accountName, @NotNull Map<Currency<?>, BigDecimal> currencies) {
        super(accountName, currencies);
    }

    @Override
    public int hashCode() {
        return this.getAccountName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NamedAccount named)) {
            return false;
        }
        return named.getAccountName().equalsIgnoreCase(this.getAccountName());
    }
}
