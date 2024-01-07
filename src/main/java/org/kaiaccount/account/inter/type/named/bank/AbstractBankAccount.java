package org.kaiaccount.account.inter.type.named.bank;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.type.named.AbstractNamedAccountLike;

import java.math.BigDecimal;
import java.util.Map;

public abstract class AbstractBankAccount extends AbstractNamedAccountLike implements BankAccount {
    public AbstractBankAccount(@NotNull String bankAccountName, @NotNull Map<Currency<?>, BigDecimal> currencies) {
        super(bankAccountName, currencies);
    }
}
