/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.resource;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

public interface EasyResourceLoader<T> extends IdentifiableResourceReloadListener {

  @Override
  default CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler,
      Executor preparer, Executor applier) {
    return prepare(manager, prepareProfiler, preparer).thenCompose(synchronizer::whenPrepared)
        .thenAcceptAsync(data -> apply(data, manager, applyProfiler), applier);
  }

  /**
   * Prepares the data. Can be done with multithreading.
   *
   * @param manager  the resource manager
   * @param profiler the preparation profiler
   * @param executor the preparation executor
   * @return the completable future for preparation
   */
  CompletableFuture<T> prepare(ResourceManager manager, Profiler profiler, Executor executor);

  /**
   * Applies the prepared data.
   *
   * @param data     the data
   * @param manager  the manager
   * @param profiler the profiler
   */
  void apply(T data, ResourceManager manager, Profiler profiler);

  /**
   * Gets the identifier of this resource load listener.
   *
   * @return the id
   */
  Identifier getFabricId();

  /**
   * Returns the dependencies for sorting. only applies in the apply stage!
   *
   * @return The identifiers of listeners this listener expects to have been
   * executed before itself.
   */
  default Collection<Identifier> getFabricDependencies() {
    return Collections.emptyList();
  }
}
