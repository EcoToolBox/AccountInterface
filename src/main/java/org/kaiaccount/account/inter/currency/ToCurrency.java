package org.kaiaccount.account.inter.currency;

import org.jetbrains.annotations.NotNull;

public interface ToCurrency {

    @NotNull Currency<?> toCurrency(@NotNull CurrencyBuilder builder) throws Exception;
}
