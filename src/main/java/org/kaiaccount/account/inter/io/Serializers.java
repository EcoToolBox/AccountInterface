package org.kaiaccount.account.inter.io;

public final class Serializers {

	public final PlayerAccountSerializer PLAYER_ACCOUNT = new PlayerAccountSerializer();

	private Serializers() {
		throw new RuntimeException("Don't do that");
	}
}
