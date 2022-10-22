package org.kaiaccount.account.inter.type.bank;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.Currency;
import org.kaiaccount.account.inter.io.Serializer;
import org.kaiaccount.account.inter.type.player.PlayerAccount;

import java.math.BigDecimal;
import java.util.Map;

public class PlayerBankAccount extends AbstractBankAccount<PlayerBankAccount> {

	private final @NotNull PlayerAccount account;

	public PlayerBankAccount(@NotNull PlayerAccount account, @NotNull Map<Currency, BigDecimal> values) {
		super(values);
		this.account = account;
	}

	public @NotNull PlayerAccount getAccount() {
		return this.account;
	}

	@Override
	public Serializer<PlayerBankAccount> getSerializer() {
		return null;
	}
}
