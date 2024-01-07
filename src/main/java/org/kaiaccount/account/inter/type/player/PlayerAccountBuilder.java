package org.kaiaccount.account.inter.type.player;

import org.jetbrains.annotations.CheckReturnValue;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.inter.currency.Currency;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerAccountBuilder {

    private final Map<Currency<?>, BigDecimal> initialBalance = new ConcurrentHashMap<>();
    private OfflinePlayer player;

    @CheckReturnValue
    @NotNull
    public PlayerAccount<?> build() {
        return AccountInterface.getManager().toPlayerAccount(this);
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public PlayerAccountBuilder setPlayer(OfflinePlayer player) {
        this.player = player;
        return this;
    }

    public Map<Currency<?>, BigDecimal> getInitialBalance() {
        return initialBalance;
    }

    public PlayerAccountBuilder setInitialBalance(Map<Currency<?>, BigDecimal> map) {
        this.initialBalance.clear();
        this.initialBalance.putAll(map);
        return this;
    }
}
