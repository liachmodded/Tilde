/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.permission;

/**
 * A subject-key pair for easy perm storage.
 *
 * @param <S> the subject type
 * @param <K> the key type
 */
public final class PermissionIndex<S, K> {

    private final S subject;
    private final K key;

    /**
     * Create a subject-key pair.
     *
     * @param subject the subject
     * @param key the key
     */
    public PermissionIndex(S subject, K key) {
        this.subject = subject;
        this.key = key;
    }

    /**
     * Gets the key.
     */
    public K getKey() {
        return key;
    }

    /**
     * Gets the subject.
     */
    public S getSubject() {
        return subject;
    }

    @Override
    public int hashCode() {
        return (key.hashCode() * 31) ^ subject.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof PermissionIndex))
            return false;

        PermissionIndex that = (PermissionIndex) obj;
        return key.equals(that.key) && subject.equals(that.subject);
    }
}
