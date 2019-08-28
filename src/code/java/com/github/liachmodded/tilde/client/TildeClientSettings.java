/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.client;

import com.github.liachmodded.tilde.config.Property;
import com.github.liachmodded.tilde.config.TildeConfig;

public final class TildeClientSettings {

  private static final String COMMENT = "Tilde's client settings; restart client to apply changes!";
  public final Property<Boolean> midiLoader;

  TildeClientSettings(TildeConfig config) {
    midiLoader = config.booleanProperty("midi-loader", true);

    config.save(COMMENT);
  }

}
