/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.mixin.client;

import com.github.liachmodded.tilde.MidiSequenceLoader;
import com.github.liachmodded.tilde.Tilde;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.util.NonBlockingThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class ClientMixin extends NonBlockingThreadExecutor<Runnable> {

    @Shadow private ReloadableResourceManager resourceManager;

    public ClientMixin(String string_1) {
        super(string_1);
    }

    @Inject(method = "init()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashScreen;method_18819(Lnet/minecraft/client/MinecraftClient;)V"))
    public void onInitMethod(CallbackInfo ci) {
        resourceManager.registerListener(new MidiSequenceLoader(Tilde.getInstance().getMusicCallback()));
    }
}
