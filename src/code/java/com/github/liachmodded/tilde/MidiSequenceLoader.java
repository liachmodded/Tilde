/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

public class MidiSequenceLoader implements ResourceReloadListener {

    private final List<Sequence> sequences;
    private final MusicCallback callback;

    public MidiSequenceLoader(MusicCallback callback) {
        this.sequences = new ArrayList<>();
        this.callback = callback;
    }

    @Override
    public CompletableFuture<Void> reload(ResourceReloadListener.Synchronizer synchronizer, ResourceManager manager, Profiler prepProfiler, Profiler applyProfiler, Executor preparer, Executor applier) {
        return CompletableFuture.supplyAsync(() -> manager.findResources("midi", st -> st.endsWith(".mid") || st.endsWith(".midi")), preparer)
                .thenComposeAsync((ids) -> {
                    @SuppressWarnings("unchecked") final CompletableFuture<Sequence>[] tasks = (CompletableFuture<Sequence>[]) new CompletableFuture<?>[ids.size()];
                    int i = 0;
                    for (Identifier id : ids) {
                        tasks[i] = CompletableFuture.supplyAsync(() -> {
                            try (Resource resource = manager.getResource(id)) {
                                return MidiSystem.getSequence(resource.getInputStream());
                            } catch (InvalidMidiDataException | IOException ex) {
                                Tilde.rethrow(ex);
                                return null;
                            }
                        }, preparer);
                        i++;
                    }
                    return CompletableFuture.allOf(tasks).thenApplyAsync((nil) -> {
                        final List<Sequence> sequences = new ArrayList<>(tasks.length);
                        for (CompletableFuture<Sequence> each : tasks) {
                            Sequence sequence = null;
                            try {
                                sequence = each.join();
                            } catch (CompletionException ignored) {
                            }
                            if (sequence != null)
                                sequences.add(sequence);
                        }

                        return sequences;
                    }, preparer);
                }, preparer)
                .thenCompose(synchronizer::whenPrepared)
                .thenAcceptAsync(sequences -> {
                    this.sequences.clear();
                    this.sequences.addAll(sequences);
                    ((ArrayList<?>) this.sequences).trimToSize();
                    this.callback.updateMusic(sequences);
                }, applier);
    }
}
