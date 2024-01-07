package org.kaiaccount.account.inter.type.named;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;

import java.math.BigDecimal;
import java.util.Map;

public class AbstractNamedAccount extends AbstractNamedAccountLike {
    public AbstractNamedAccount(@NotNull String accountName, @NotNull Map<Currency<?>, BigDecimal> currencies) {
        super(accountName, currencies);
    }
}
