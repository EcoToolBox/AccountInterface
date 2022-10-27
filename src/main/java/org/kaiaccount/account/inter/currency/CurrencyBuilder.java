package org.kaiaccount.account.inter.currency;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.AccountInterface;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CurrencyBuilder {

	private String singleDisplay;
	private String shortDisplay;
	private String symbol;
	private String name;
	private BigDecimal worth;
	private boolean isDefault;
	private Plugin plugin;
	private String multiDisplay;

	public Currency<?> build() {
		return AccountInterface.getManager().toCurrency(this);
	}

	public BigDecimal getWorth() {
		return worth;
	}

	public CurrencyBuilder setWorth(@Nullable BigDecimal worth) {
		this.worth = worth;
		return this;
	}

	public CurrencyBuilder setWorth(@Nullable BigInteger worth) {
		if (worth == null) {
			this.worth = null;
			return this;
		}
		this.worth = new BigDecimal(worth);
		return this;
	}

	public CurrencyBuilder setWorth(@Nullable Double worth) {
		if (worth == null) {
			this.worth = null;
			return this;
		}
		this.worth = BigDecimal.valueOf(worth);
		return this;
	}

	public String getDisplayNameSingle() {
		return singleDisplay;
	}

	public CurrencyBuilder setDisplayNameSingle(@Nullable String singleDisplay) {
		this.singleDisplay = singleDisplay;
		return this;
	}

	public String getDisplayNameShort() {
		return shortDisplay;
	}

	public CurrencyBuilder setDisplayNameShort(@Nullable String shortDisplay) {
		this.shortDisplay = shortDisplay;
		return this;
	}

	public String getSymbol() {
		return symbol;
	}

	public CurrencyBuilder setSymbol(@NotNull String symbol) {
		this.symbol = symbol;
		return this;
	}

	public String getName() {
		return name;
	}

	public CurrencyBuilder setName(@NotNull String name) {
		this.name = name;
		return this;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public CurrencyBuilder setDefault(boolean aDefault) {
		isDefault = aDefault;
		return this;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public CurrencyBuilder setPlugin(Plugin plugin) {
		this.plugin = plugin;
		return this;
	}

	public String getDisplayNameMultiple() {
		return multiDisplay;
	}

	public CurrencyBuilder setDisplayNameMultiple(@Nullable String multiDisplay) {
		this.multiDisplay = multiDisplay;
		return this;
	}
}
