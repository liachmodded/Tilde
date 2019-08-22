/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.mixin;

import com.github.liachmodded.tilde.Tilde;
import com.github.liachmodded.tilde.server.TildeMinecraftServer;
import com.github.liachmodded.tilde.server.TildeServerAttachment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.util.NonBlockingThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftServer.class)
public abstract class ServerMixin extends NonBlockingThreadExecutor<ServerTask> implements TildeMinecraftServer {

  private TildeServerAttachment attachment;

  protected ServerMixin(String string_1) {
    super(string_1);
  }

  @Override
  public void createAttachment(Tilde tilde) {
    attachment = new TildeServerAttachment(tilde, (MinecraftServer) (Object) this);
  }

  @Override
  public TildeServerAttachment getAttachment() {
    return attachment;
  }
}
