/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.permission.model;

import com.google.common.graph.Graph;

/**
 * A system of permissions. It can be bound to a server, an organization/faction,
 * a residence, a world, etc.
 *
 * @param <S> subject type
 * @param <K> key type
 */
public interface PermissionSystem<S, K> {

  /**
   * Gets the inheritance graph of subjects.
   *
   * <p>The graph goes from the least powerful subject to the most powerful subject,
   * e.g. player -> moderator -> owner.
   *
   * @return the subject inheritance graph
   */
  Graph<S> getSubjectRelations();

  /**
   * Gets the inheritance graph of keys.
   *
   * <p>The graph goes from the most powerful key to the least powerful key,
   * e.g. all commands -> admin commands -> op commands -> general commands
   *
   * @return the key inheritance graph
   */
  Graph<K> getKeyRelations();

  /**
   * Gets the exact definition of the permission at this subject-key combination.
   *
   * @param subject the subject
   * @param key the key
   * @return the exact definition
   */
  PermissionValue getExact(S subject, K key);

  /**
   * Gets the recursive definition of the permission of this subject-key combination.
   *
   * <p>This search will recurse along the relations.
   *
   * @param subject the subject
   * @param key the key
   * @return the exact definition
   */
  PermissionValue get(S subject, K key);

  /**
   * Convenience method to get the recursive definition of a permission.
   *
   * @param subject the subject
   * @param key the key
   * @return the boolean definition
   */
  default boolean evaluate(S subject, K key) {
    return get(subject, key).evaluate();
  }

}
