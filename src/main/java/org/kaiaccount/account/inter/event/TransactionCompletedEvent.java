package org.kaiaccount.account.inter.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;

public class TransactionCompletedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final @NotNull TransactionResult transaction;

	public TransactionCompletedEvent(@NotNull TransactionResult transaction) {
		super(true);
		this.transaction = transaction;
	}

	public @NotNull TransactionResult getTransaction() {
		return this.transaction;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
