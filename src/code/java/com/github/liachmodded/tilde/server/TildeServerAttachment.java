/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.server;

import com.github.liachmodded.tilde.Tilde;
import com.github.liachmodded.tilde.config.TildeConfig;
import net.minecraft.server.MinecraftServer;

public final class TildeServerAttachment {

  private final Tilde tilde;
  private final MinecraftServer server;
  private final TildeServerSettings settings;

  public TildeServerAttachment(Tilde tilde, MinecraftServer server) {
    this.tilde = tilde;
    this.server = server;
    this.settings = new TildeServerSettings(new TildeConfig(server.getFile("config").toPath(), "tilde.properties"));
  }

  void init() {
    String token = settings.discordToken.get();
    if (token.isEmpty()) {

    }
  }

  // server destructs!
  public void destruct() {
    settings.save();
  }

}
