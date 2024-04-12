package org.kaiaccount.account.inter.type.player;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

public interface ToPlayerAccount {

    @NotNull
    @CheckReturnValue
    PlayerAccount toPlayerAccount(@NotNull PlayerAccountBuilder builder);
}
