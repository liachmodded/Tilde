/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.permission.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Values for the permission system.
 */
public enum PermissionValue {
  /**
   * The value for a missing definition.
   */
  NONE(false),
  /**
   * The value for an allowance.
   */
  ALLOW(true),
  /**
   * The value for an explicit denial (has higher priority than {@link #ALLOW}).
   */
  DENY(false);

  private final boolean value;

  PermissionValue(boolean value) {
    this.value = value;
  }

  /**
   * Gets the boolean value of this permission value.
   */
  public boolean evaluate() {
    return value;
  }

  /**
   * Gets the merged results of two permission values.
   */
  public static PermissionValue merge(PermissionValue one, PermissionValue two) {
    return one.compareTo(two) > 0 ? one : two;
  }

  /**
   * Gets the merged results of multiple permission values.
   */
  public static PermissionValue merge(Collection<PermissionValue> definitions) {
    return definitions.isEmpty() ? NONE : Collections.max(definitions);
  }

  /**
   * Gets the merged results of multiple permission values.
   */
  public static PermissionValue merge(Stream<PermissionValue> definitions) {
    return definitions.max(Comparator.naturalOrder()).orElse(NONE);
  }
}
