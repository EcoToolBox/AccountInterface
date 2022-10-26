package org.kaiaccount.account.inter.type.bank.player;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.type.bank.BankPermission;
import org.kaiaccount.account.inter.type.player.PlayerAccount;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerBankAccountBuilder {

	private PlayerAccount<?> account;
	private String name;
	private final Map<UUID, Collection<BankPermission>> bankPermissions = new ConcurrentHashMap<>();
	private final Map<Currency<?>, BigDecimal> initialBalance = new ConcurrentHashMap<>();

	public PlayerBankAccount<?> build() {
		return AccountInterface.getGlobal().toBankAccount(this);
	}

	public Map<Currency<?>, BigDecimal> getInitialBalance() {
		return this.initialBalance;
	}

	public PlayerBankAccountBuilder setInitialBalance(Map<Currency<?>, BigDecimal> map) {
		this.initialBalance.clear();
		this.initialBalance.putAll(map);
		return this;
	}

	public PlayerAccount<?> getAccount() {
		return this.account;
	}

	public PlayerBankAccountBuilder setAccount(PlayerAccount<?> account) {
		this.account = account;
		return this;
	}

	public String getName() {
		return this.name;
	}

	public PlayerBankAccountBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public Map<UUID, Collection<BankPermission>> getAccountHolders() {
		return this.bankPermissions;
	}

	public PlayerBankAccountBuilder setAccountHolders(Map<UUID, Collection<BankPermission>> map) {
		this.bankPermissions.clear();
		this.bankPermissions.putAll(map);
		return this;
	}

	public PlayerBankAccountBuilder addAccountHolders(@NotNull UUID uuid, Collection<BankPermission> permissions) {
		this.bankPermissions.put(uuid, permissions);
		return this;
	}
}
