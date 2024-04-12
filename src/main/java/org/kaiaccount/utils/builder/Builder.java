package org.kaiaccount.utils.builder;

import org.jetbrains.annotations.CheckReturnValue;

public interface Builder<Build extends Buildable<Build, Self>, Self extends Builder<Build, Self>> {

    @CheckReturnValue
    Build build();

    Self from(Self builder);

    default Self from(Build built) {
        return from(built.toBuilder());
    }
}
