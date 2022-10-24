package org.kaiaccount;

import org.jetbrains.annotations.NotNull;

public final class AccountInterface {

	private static AccountInterfaceManager global;

	private AccountInterface() {
		throw new RuntimeException("Should not run");
	}

	public static void setGlobal(@NotNull AccountInterfaceManager aiGlobal) {
		if (!isReady()) {
			throw new RuntimeException("Currency plugin already registered");
		}
		global = aiGlobal;
	}

	public static boolean isReady() {
		return global != null;
	}

	public static @NotNull AccountInterfaceManager getGlobal() {
		if (global == null) {
			throw new RuntimeException("Currency plugin has not registered itself yet");
		}
		return global;
	}


}
