/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.mixin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.NonBlockingThreadExecutor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class ServerMixin extends NonBlockingThreadExecutor<ServerTask> {

    @Shadow
    public abstract CommandManager getCommandManager();

    public ServerMixin(String string_1) {
        super(string_1);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void onInit(CallbackInfo ci) {
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
        getCommandManager().getDispatcher().register(cmd);

        LiteralArgumentBuilder<ServerCommandSource> cmd2 = LiteralArgumentBuilder
                .<ServerCommandSource>literal("zlookup")
                .redirect(zombieLookup);

        getCommandManager().getDispatcher().register(cmd2);
    }
}
