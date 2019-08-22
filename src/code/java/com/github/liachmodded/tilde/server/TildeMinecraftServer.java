/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.server;

import com.github.liachmodded.tilde.Tilde;

public interface TildeMinecraftServer {

  void createAttachment(Tilde tilde);

  TildeServerAttachment getAttachment();
}
