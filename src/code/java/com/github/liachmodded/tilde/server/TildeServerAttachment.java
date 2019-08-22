/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.server;

import com.github.liachmodded.tilde.Tilde;
import com.github.liachmodded.tilde.config.TildeConfig;
import java.util.concurrent.CompletableFuture;
import net.minecraft.server.MinecraftServer;
import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.listener.server.role.UserRoleAddListener;

public final class TildeServerAttachment {

  private final Tilde tilde;
  private final MinecraftServer server;
  private final TildeConfig config;

  public TildeServerAttachment(Tilde tilde, MinecraftServer server) {
    this.tilde = tilde;
    this.server = server;
    this.config = new TildeConfig(server.getFile("config").toPath(), "tilde.properties");
  }

  public TildeConfig getConfig() {
    return config;
  }

  void init() {
    TildeConfig.PropertyAccessor<String> tokenAccessor = config.stringAccessor("discord-token", "");

    String token = tokenAccessor.get();
    if (token.isEmpty()) {

    }

    setDiscordApi(new DiscordApiBuilder()
        .setAccountType(AccountType.BOT)
        .setToken("")
        .addListener(api -> (UserRoleAddListener) event -> {

        })
        .login()
    );
  }

  // server destructs!
  public void destruct() {
    config.save("Tilde server config - UTF8 encoded");
  }

  void setDiscordApi(CompletableFuture<DiscordApi> api) {}
//
//  CompletableFuture<DiscordApi> getApi();

}
