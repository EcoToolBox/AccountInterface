package org.kaiaccount.account.inter.impl.currency;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.AbstractCurrency;
import org.kaiaccount.account.inter.currency.CurrencyBuilder;
import org.kaiaccount.account.inter.io.Serializer;

import java.io.File;

public class FakeCurrency extends AbstractCurrency<FakeCurrency> {
	public FakeCurrency(@NotNull CurrencyBuilder builder) {
		super(builder);
	}

	@Override
	public Serializer<FakeCurrency> getSerializer() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public @NotNull File getFile() {
		throw new RuntimeException("Not implemented");
	}
}
