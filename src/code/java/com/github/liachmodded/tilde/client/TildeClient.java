/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.client;

import com.github.liachmodded.tilde.client.midi.MidiSequenceLoader;
import com.github.liachmodded.tilde.client.midi.MusicRunner;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public final class TildeClient implements ClientModInitializer {

  private final MusicRunner runner = new MusicRunner();

  @Override
  public void onInitializeClient() {
    new Thread(runner).start();
    ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new MidiSequenceLoader(runner));
  }
}
