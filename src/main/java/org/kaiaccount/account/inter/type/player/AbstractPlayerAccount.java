package org.kaiaccount.account.inter.type.player;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.type.AccountType;
import org.kaiaccount.account.inter.type.IsolatedAccount;
import org.kaiaccount.account.inter.type.bank.player.PlayerBankAccount;
import org.kaiaccount.account.inter.type.bank.player.PlayerBankAccountBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.LinkedTransferQueue;

public abstract class AbstractPlayerAccount<Self extends AbstractPlayerAccount<Self>>
		implements PlayerAccount<Self>, AccountType<Self> {

	private final @NotNull OfflinePlayer player;

	private final IsolatedAccount account;
	private final Collection<PlayerBankAccount<?>> banks = new LinkedTransferQueue<>();

	public AbstractPlayerAccount(PlayerAccountBuilder builder) {
		this.player = builder.getPlayer();
		this.account = new IsolatedAccount(builder.getInitialBalance());
		if (this.player == null) {
			throw new IllegalArgumentException("Player is missing from builder");
		}
	}

	@Override
	public @NotNull Collection<PlayerBankAccount<?>> getBanks() {
		//load others
		return Collections.unmodifiableCollection(this.banks);
	}

	@Override
	public @NotNull PlayerBankAccount<?> createBankAccount(@NotNull String name) {
		PlayerBankAccount<?> account = new PlayerBankAccountBuilder().setName(name).setAccount(this).build();
		this.banks.add(account);
		return account;
	}

	@Override
	public void registerBank(@NotNull PlayerBankAccount<?> account) {
		if (!account.getAccountHolder().equals(this)) {
			throw new IllegalArgumentException(
					"provided PlayerBankAccount cannot be registered. The AccountHolder must be this player");
		}
		this.banks.add(account);
	}

	@Override
	public @NotNull OfflinePlayer getPlayer() {
		return this.player;
	}

	@Override
	public IsolatedAccount getIsolated() {
		return this.account;
	}
}
