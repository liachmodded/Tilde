/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.config;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A property in the config of tilde.
 * @param <T>
 */
public final class Property<T> implements Supplier<T> {

  private final TildeConfig owner;
  private final String key;
  private T value;
  private final Function<T, String> saver;

  Property(TildeConfig owner, String key, T value, Function<T, String> saver) {
    this.owner = owner;
    this.key = key;
    this.value = value;
    this.saver = saver;
  }

  public void set(T value) {
    this.value = value;
    owner.properties.put(key, saver.apply(value));
  }

  @Override
  public T get() {
    return this.value;
  }
}
