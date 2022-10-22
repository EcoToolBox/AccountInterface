package org.kaiaccount.account.inter;

import org.bukkit.plugin.Plugin;

public interface Currency {

	String getDisplayNameSingle();

	String getDisplayNameMultiple();

	String getKeyName();

	Plugin getPlugin();

}
