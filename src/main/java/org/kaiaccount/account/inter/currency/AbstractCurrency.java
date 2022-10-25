package org.kaiaccount.account.inter.currency;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Optional;

public abstract class AbstractCurrency implements Currency {

	private final @Nullable String singleDisplay;
	private final @Nullable String multiDisplay;
	private @Nullable BigDecimal worth;
	private final @NotNull String shortDisplay;
	private final @NotNull String symbol;
	private final @NotNull String name;
	private boolean isDefault;
	private final @NotNull Plugin plugin;

	public AbstractCurrency(@NotNull CurrencyBuilder builder) {
		this.plugin = builder.getPlugin();
		this.isDefault = builder.isDefault();
		this.name = builder.getName();
		this.multiDisplay = builder.getDisplayNameMultiple();
		this.singleDisplay = builder.getDisplayNameSingle();
		this.shortDisplay = builder.getDisplayNameShort();
		this.symbol = builder.getSymbol();
		this.worth = builder.getWorth();
		if (this.plugin == null) {
			throw new RuntimeException("No plugin found");
		}
		if (this.name == null) {
			throw new RuntimeException("No name found");
		}
		if (this.symbol == null) {
			throw new RuntimeException("no symbol found");
		}
	}

	@Override
	public Optional<BigDecimal> getWorth() {
		return Optional.ofNullable(this.worth);
	}

	@Override
	public void setWorth(@NotNull BigDecimal worth) {
		this.worth = worth;
	}

	@Override
	public @NotNull String getDisplayNameSingle() {
		if (this.singleDisplay == null) {
			return this.name;
		}
		return this.singleDisplay;
	}

	@Override
	public @NotNull String getDisplayNameMultiple() {
		if (this.multiDisplay == null) {
			return this.getDisplayNameSingle();
		}
		return this.multiDisplay;
	}

	@NotNull
	@Override
	public String getDisplayNameShort() {
		return this.shortDisplay;
	}

	@Override
	public @NotNull String getSymbol() {
		return this.symbol;
	}

	@Override
	public @NotNull String getKeyName() {
		return this.name;
	}

	@Override
	public @NotNull Plugin getPlugin() {
		return this.plugin;
	}

	@Override
	public boolean isDefault() {
		return this.isDefault;
	}

	@Override
	public void setDefault(boolean check) {
		this.isDefault = check;
	}
}
