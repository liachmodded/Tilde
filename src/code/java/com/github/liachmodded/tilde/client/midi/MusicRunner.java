/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.tilde.client.midi;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class MusicRunner implements Runnable, MusicCallback {

  private final Random random = new Random(); // only used on off thread!
  private volatile boolean alive = true;
  private volatile boolean playing = true;
  private volatile boolean updatedMusic = false;
  private final AtomicReference<List<Sequence>> sequenceRef = new AtomicReference<>(Collections.emptyList());

  @Override
  public void updateMusic(List<Sequence> sequences) {
    sequenceRef.set(sequences);
    updatedMusic = true;
  }

  @Override
  public void setPlaying(boolean playing) {
    this.playing = playing;
  }

  void kill() {
    alive = false;
  }

  // off thread this is!
  @Override
  public void run() {
    try (Sequencer sequencer = MidiSystem.getSequencer()) {
      sequencer.open();

      while (alive) {
        final boolean playing = this.playing;
        if (playing) {
          final boolean running = sequencer.isRunning();
          if (!running || this.updatedMusic) {
            this.updatedMusic = false;
            final List<Sequence> sequences = sequenceRef.get();
            if (!sequences.isEmpty()) {
              sequencer.setSequence(sequences.get(random.nextInt(sequences.size())));
            } else {
              sequencer.setSequence((Sequence) null);
            }
          }

          if (!running && sequencer.getSequence() != null) {
            sequencer.start();
          }
        } else {
          if (sequencer.isRunning()) {
            sequencer.stop();
          }
        }
        // Wait till we close it!
      }
    } catch (InvalidMidiDataException | MidiUnavailableException ex) {
      throw new RuntimeException(ex);
    }
  }
}
