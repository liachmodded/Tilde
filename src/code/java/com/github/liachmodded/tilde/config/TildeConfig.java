/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.config;

import com.github.liachmodded.tilde.Tilde;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.function.Function;
import net.minecraft.server.dedicated.AbstractPropertiesHandler;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class TildeConfig extends AbstractPropertiesHandler<TildeConfig> {

  private final Properties properties;
  private final Path file;

  public TildeConfig(Path directory, String fileName) {
    this(directory.resolve(fileName));
  }

  public TildeConfig(Path file) {
    this(load(file), file);
  }

  private TildeConfig(Properties properties, Path file) {
    super(properties);
    this.properties = properties;
    this.file = file;
  }

  public static Properties load(Path path) {
    Properties properties = new Properties();

    // The vanilla
    try (Reader reader = Files.newBufferedReader(path)) {
      properties.load(reader);
    } catch (IOException ex) {
      Tilde.LOGGER.error("Failed to load properties from file {}!", path, ex);
    }

    return properties;
  }

  public TildeConfig copyTo(Path file) {
    Properties copiedProperties = new Properties();
    copiedProperties.putAll(getProperties());
    return new TildeConfig(copiedProperties, file);
  }

  public void save() {
    save(file, "Config in UTF-8 encoding");
  }

  public void save(@Nullable String comment) {
    save(file, comment);
  }

  @Override
  public void store(final Path file) {
    save(file, null);
  }

  // Exposures

  private void save(final Path file, @Nullable String comment) {
    try {
      Files.createDirectories(file.getParent());
      try (Writer writer = Files.newBufferedWriter(file)) {
        this.properties.store(writer, comment);
      }
    } catch (IOException ex) {
      Tilde.LOGGER.warn("Cannot save config at {}!", file, ex);
    }
  }

  @Override
  public <V> PropertyAccessor<V> accessor(final String key, final Function<String, V> reader, final Function<V, String> saver, final V value) {
    return super.accessor(key, reader, saver, value);
  }

  @Override
  public <V> PropertyAccessor<V> accessor(final String key, final Function<String, V> reader, final V value) {
    return super.accessor(key, reader, value);
  }

  @Override
  public PropertyAccessor<Integer> intAccessor(final String key, final int value) {
    return super.intAccessor(key, value);
  }

  @Override
  public PropertyAccessor<Boolean> booleanAccessor(final String key, final boolean value) {
    return super.booleanAccessor(key, value);
  }

  // Internal

  public PropertyAccessor<String> stringAccessor(final String key, final String value) {
    return accessor(key, Function.identity(), Function.identity(), value);
  }

  @Override
  protected Properties getProperties() {
    return properties;
  }

  @Override // this is actually modifying the config from the new properties
  protected TildeConfig create(final Properties properties) {
    return this;
  }

}
