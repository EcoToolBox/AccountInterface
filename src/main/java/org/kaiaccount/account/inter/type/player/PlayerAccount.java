package org.kaiaccount.account.inter.type.player;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.io.Serializer;
import org.kaiaccount.account.inter.io.Serializers;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;
import org.kaiaccount.account.inter.type.AccountType;
import org.kaiaccount.account.inter.type.IsolatedAccount;
import org.kaiaccount.account.inter.type.bank.player.PlayerBankAccount;
import org.kaiaccount.account.inter.type.bank.player.PlayerBankAccountBuilder;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.LinkedTransferQueue;

public class PlayerAccount implements AccountType<PlayerAccount> {

	private final @NotNull OfflinePlayer player;

	private final IsolatedAccount account;
	private final Collection<PlayerBankAccount<?>> banks = new LinkedTransferQueue<>();

	public PlayerAccount(@NotNull OfflinePlayer player) {
		this(player, Collections.emptyMap());
	}

	public PlayerAccount(@NotNull OfflinePlayer player, @NotNull Map<Currency, BigDecimal> map) {
		this.player = player;
		this.account = new IsolatedAccount(map);
	}

	public @NotNull Collection<PlayerBankAccount<?>> getBanks() {
		//load others
		return Collections.unmodifiableCollection(this.banks);
	}

	public @NotNull PlayerBankAccount<?> createBankAccount(@NotNull String name) {
		PlayerBankAccount<?> account = new PlayerBankAccountBuilder().setName(name).setAccount(this).build();
		this.banks.add(account);
		return account;
	}

	public @NotNull OfflinePlayer getPlayer() {
		return this.player;
	}

	@Override
	public IsolatedAccount getIsolated() {
		return this.account;
	}

	public @NotNull TransactionResult withdrawSynced(@NotNull Payment payment) {
		return this.account.withdrawSynced(payment, this);
	}

	public @NotNull TransactionResult depositSynced(@NotNull Payment payment) {
		return this.account.depositSynced(payment, this);
	}

	public @NotNull TransactionResult setSynced(@NotNull Payment payment) {
		return this.account.setSynced(payment, this);
	}

	@Override
	public Serializer<PlayerAccount> getSerializer() {
		return Serializers.PLAYER_ACCOUNT;
	}

	public static File getFile(@NotNull UUID uuid) {
		return new File("plugins/account/accounts/players/" + uuid + "player.yml");
	}
}
