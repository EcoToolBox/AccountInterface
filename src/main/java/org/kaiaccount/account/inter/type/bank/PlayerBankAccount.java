package org.kaiaccount.account.inter.type.bank;

import org.bukkit.entity.AnimalTamer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.io.Serializer;
import org.kaiaccount.account.inter.type.player.PlayerAccount;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerBankAccount extends AbstractBankAccount<PlayerBankAccount> {

	private final @NotNull PlayerAccount account;
	private final @NotNull Map<UUID, Collection<BankPermission>> bankPermissions = new ConcurrentHashMap<>();

	public PlayerBankAccount(@NotNull PlayerAccount account, @NotNull String name,
			@NotNull Map<Currency, BigDecimal> values) {
		super(name, values);
		this.account = account;
	}

	public @NotNull PlayerAccount getAccountHolder() {
		return this.account;
	}

	@Deprecated
	public void addAccount(@NotNull AnimalTamer uuid) {
		throw new RuntimeException("Cannot add an account without permissions");
	}

	public void addAccount(@NotNull AnimalTamer uuid, @NotNull BankPermission... permissions) {
		this.addAccount(uuid.getUniqueId(), permissions);
	}

	public void addAccount(@NotNull AnimalTamer player, Collection<BankPermission> permissions) {
		this.addAccount(player.getUniqueId(), permissions);
	}

	@Deprecated
	public void addAccount(@NotNull UUID uuid) {
		throw new RuntimeException("Cannot add an account without permissions");
	}

	public void addAccount(@NotNull UUID uuid, @NotNull BankPermission... permissions) {
		this.addAccount(uuid, Arrays.asList(permissions));
	}

	public void addAccount(@NotNull UUID uuid, Collection<BankPermission> permissions) {
		if (permissions.isEmpty()) {
			throw new RuntimeException("Cannot add an account without permissions");
		}
		this.bankPermissions.put(uuid, permissions);
	}

	public void removeAccount(@NotNull AnimalTamer player) {
		this.removeAccount(player.getUniqueId());
	}

	public void removeAccount(@NotNull UUID uuid) {
		this.bankPermissions.remove(uuid);
	}

	@Override
	public Serializer<PlayerBankAccount> getSerializer() {
		throw new RuntimeException("Not implemented yet");
	}

	@NotNull
	@Override
	public Map<UUID, Collection<BankPermission>> getAccounts() {
		Map<UUID, Collection<BankPermission>> map = new HashMap<>(this.bankPermissions);
		map.put(this.getAccountHolder().getPlayer().getUniqueId(), Arrays.asList(BankPermission.values()));
		return Collections.unmodifiableMap(map);
	}
}
