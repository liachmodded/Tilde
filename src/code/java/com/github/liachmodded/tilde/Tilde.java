/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

/**
 * A mod that cares about permissions, I guess?
 */
public class Tilde implements ModInitializer {

    /**
     * The mod's identifier.
     */
    public static final String ID = "tilde";

    /**
     * Convenience method to create a namespaced identifier for an object from Tilde mod.
     *
     * @param name the name
     * @return the created ID
     */
    public static Identifier name(String name) {
        return new Identifier(ID, name);
    }

    @Override
    public void onInitialize() {

    }
}
