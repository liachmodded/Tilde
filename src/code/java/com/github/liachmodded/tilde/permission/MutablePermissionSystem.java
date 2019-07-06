/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.permission;

/**
 * A {@link PermissionSystem permission system} that can be changed.
 *
 * @param <S> the subject type
 * @param <K> the key type
 */
public interface MutablePermissionSystem<S, K> extends PermissionSystem<S, K> {
    /**
     * Explicitly set the value of a permission entry.
     *
     * @param subject the subject
     * @param key the key
     * @param value the set value
     */
    void set(S subject, K key, PermissionValue value);

    /**
     * Update the value of a permission entry.
     *
     * @param subject the subject
     * @param key the key
     * @param operator the updater
     */
    void update(S subject, K key, PermissionUpdater operator);

    /**
     * A handy interface to update permission values.
     */
    interface PermissionUpdater {
        /**
         * Update a permission value.
         *
         * @param old the previous value
         * @param exact whether this value is explicitly defined for this subject-key combination
         * @return the updated permission
         */
        PermissionValue update(PermissionValue old, boolean exact);
    }
}
