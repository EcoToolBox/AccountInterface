package org.kaiaccount.account.inter.io;

import org.jetbrains.annotations.CheckReturnValue;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public interface Serializable<Self extends Serializable<Self>> {

    @CheckReturnValue
    @NotNull Serializer<Self> getSerializer();

    @CheckReturnValue
    @NotNull File getFile();

    default void save(@NotNull YamlConfiguration configuration) {
        this.getSerializer().serialize(configuration, (Self) this);
    }

    default void save() throws IOException {
        File file = this.getFile();
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        save(configuration);
        configuration.save(file);
    }

    @CheckReturnValue
    default boolean delete() {
        return this.getFile().delete();
    }
}
