/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde;

import com.github.liachmodded.tilde.server.TildeMinecraftServer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A mod that cares about permissions, I guess?
 */
public final class Tilde implements ModInitializer {

  /**
   * The mod's identifier.
   */
  public static final String ID = "tilde";

  public static final Logger LOGGER = LogManager.getLogger(ID);

  /**
   * Convenience method to create a namespaced identifier for an object from Tilde mod.
   *
   * @param name the name
   * @return the created ID
   */
  public static Identifier name(String name) {
    return new Identifier(ID, name);
  }

  @SuppressWarnings("unchecked")
  public static <E extends Throwable> void rethrow(Throwable ex) throws E {
    throw (E) ex;
  }

  @Override
  public void onInitialize() {
    CommandRegistry.INSTANCE.register(false, this::registerCommands);

    //    ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener();

    ServerStartCallback.EVENT.register(this::onServerStart);
    ServerStopCallback.EVENT.register(this::onServerStop);
  }

  private void onServerStart(MinecraftServer server) {
    ((TildeMinecraftServer) server).createAttachment(this);
  }

  private void onServerStop(MinecraftServer server) {
    ((TildeMinecraftServer) server).getAttachment().destruct();
  }

  private void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
    LiteralCommandNode<ServerCommandSource> zombieLookup;
    LiteralArgumentBuilder<ServerCommandSource> cmd = LiteralArgumentBuilder
        .<ServerCommandSource>literal("zombie")
        .executes(ctx -> {
          Vec3d vec = ctx.getSource().getPosition();
          BlockPos pos = new BlockPos(vec);

          ctx.getSource().sendFeedback(new LiteralText(pos.toString()), false);
          return 1;
        })
        .then(zombieLookup = LiteralArgumentBuilder
            .<ServerCommandSource>literal("lookup")
            .requires(src -> src.hasPermissionLevel(2))
            .executes(ctx -> {
              System.out.println("I am a subcommand executed!");
              return 1;
            }).build()
        );
    dispatcher.register(cmd);

    LiteralArgumentBuilder<ServerCommandSource> cmd2 = LiteralArgumentBuilder
        .<ServerCommandSource>literal("zlookup")
        .redirect(zombieLookup);

    dispatcher.register(cmd2);
  }
}
