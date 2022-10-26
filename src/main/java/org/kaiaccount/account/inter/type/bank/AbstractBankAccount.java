package org.kaiaccount.account.inter.type.bank;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.type.AccountType;
import org.kaiaccount.account.inter.type.IsolatedAccount;

import java.math.BigDecimal;
import java.util.Map;

public abstract class AbstractBankAccount<Self extends AbstractBankAccount<Self>>
		implements BankAccount<Self>, AccountType<Self> {

	private final @NotNull String bankAccountName;
	private final @NotNull IsolatedAccount account;

	public AbstractBankAccount(@NotNull String bankAccountName, @NotNull Map<Currency<?>, BigDecimal> currencies) {
		account = new IsolatedAccount(currencies);
		this.bankAccountName = bankAccountName;
	}

	@NotNull
	@Override
	public String getBankAccountName() {
		return this.bankAccountName;
	}

	@Override
	public IsolatedAccount getIsolated() {
		return this.account;
	}
}
