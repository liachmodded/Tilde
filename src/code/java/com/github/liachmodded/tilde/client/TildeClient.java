/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.client;

import com.github.liachmodded.tilde.Tilde;
import com.github.liachmodded.tilde.client.midi.MidiSequenceLoader;
import com.github.liachmodded.tilde.config.TildeConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.resource.ResourceType;

public final class TildeClient implements ClientModInitializer {

  private ModContainer mod;
  private TildeConfig config;
  private TildeClientSettings settings;

  @Override
  public void onInitializeClient() {
    FabricLoader loader = FabricLoader.getInstance();
    this.mod = loader.getModContainer(Tilde.ID).orElseThrow(() -> new IllegalStateException("Tilde is absent!"));
    this.config = new TildeConfig(loader.getConfigDirectory().toPath().resolve("tilde-client.properties"));
    this.settings = new TildeClientSettings(config);

    if (settings.midiLoader.get()) {
      ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new MidiSequenceLoader());
    }
  }
}
