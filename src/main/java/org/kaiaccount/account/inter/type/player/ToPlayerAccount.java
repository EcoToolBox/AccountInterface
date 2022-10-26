package org.kaiaccount.account.inter.type.player;

import org.jetbrains.annotations.NotNull;

public interface ToPlayerAccount {

	@NotNull
	PlayerAccount<?> toPlayerAccount(@NotNull PlayerAccountBuilder builder);
}
