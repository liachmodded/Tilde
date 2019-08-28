/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.server;

import com.github.liachmodded.tilde.config.Property;
import com.github.liachmodded.tilde.config.TildeConfig;

public final class TildeServerSettings {

  private static final String COMMENT = "Tilde's server settings; restart world/server to apply changes!";
  public final Property<String> discordToken;

  private final TildeConfig config;

  TildeServerSettings(TildeConfig config) {
    this.config = config;

    discordToken = config.stringProperty("discord-token", "");

    save();
  }

  public void save() {
    config.save(COMMENT);
  }

}
