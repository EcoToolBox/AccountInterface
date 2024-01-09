package org.kaiaccount.account.inter.impl.bank;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.type.named.AbstractNamedAccountLike;
import org.kaiaccount.account.inter.type.named.bank.AbstractBankAccount;
import org.kaiaccount.account.inter.type.named.bank.BankPermission;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FakeBankAccount extends AbstractBankAccount {

    private final Map<UUID, Collection<BankPermission>> accounts = new HashMap<>();

    public FakeBankAccount(@NotNull String bankAccountName,
                           @NotNull Map<Currency<?>, BigDecimal> currencies) {
        super(bankAccountName, currencies);
    }

    public FakeBankAccount(@NotNull String bankAccountName) {
        this(bankAccountName, Map.of());
    }

    public FakeBankAccount(@NotNull String bankAccountName, @NotNull Currency<?> currency,
                           @NotNull BigDecimal amount) {
        this(bankAccountName, Map.of(currency, amount));
    }

    @NotNull
    @Override
    public Map<UUID, Collection<BankPermission>> getAccounts() {
        return this.accounts;
    }
}
