package org.kaiaccount.account.inter.impl.currency;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.AbstractCurrency;
import org.kaiaccount.account.inter.currency.CurrencyBuilder;

public class FakeCurrency extends AbstractCurrency {
	public FakeCurrency(@NotNull CurrencyBuilder builder) {
		super(builder);
	}
}
