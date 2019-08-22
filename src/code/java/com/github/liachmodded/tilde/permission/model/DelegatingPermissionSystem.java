/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.permission.model;

import com.google.common.graph.Graph;
import java.util.List;

public class DelegatingPermissionSystem<S, K> extends AbstractPermissionSystem<S, K> implements PermissionSystem<S, K> {

  protected final List<PermissionSystem<S, K>> delegates;

  protected DelegatingPermissionSystem(Graph<S> subjectRelations, Graph<K> keyRelations, List<PermissionSystem<S, K>> delegates) {
    super(subjectRelations, keyRelations);
    this.delegates = delegates;
  }

  @Override
  public PermissionValue getExact(S subject, K key) {
    for (PermissionSystem<S, K> each : delegates) {
      PermissionValue var = each.getExact(subject, key);
      if (var != PermissionValue.NONE) {
        return var;
      }
    }
    return PermissionValue.NONE;
  }

  @Override
  protected PermissionValue getExact(PermissionIndex<S, K> index) {
    return getExact(index.getSubject(), index.getKey());
  }
}
