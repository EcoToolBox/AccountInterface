package org.kaiaccount.account.inter.impl.currency;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.currency.CurrencyBuilder;
import org.kaiaccount.account.inter.currency.ToCurrency;

public class ToFakeCurrency implements ToCurrency {
	@Override
	public Currency toCurrency(@NotNull CurrencyBuilder builder) throws Exception {
		return new FakeCurrency(builder);
	}
}
