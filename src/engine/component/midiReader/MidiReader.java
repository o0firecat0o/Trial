package engine.component.midiReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MidiReader {

	public static final int NOTE_ON = 0x90;
	public static final int NOTE_OFF = 0x80;
	public static final int SET_TEMPO = 0x51;
	public static final String[] NOTE_NAMES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

	public final ArrayList<Key> Keys = new ArrayList<>();
	public final ArrayList<Integer> MinNotesOfTrack = new ArrayList<>();
	public final ArrayList<Integer> MaxNotesOfTrack = new ArrayList<>();

	public class Key {
		public long time;
		public int notes;
		public int notesName;
		public float volume;
		public int length;
		public int track;
	}

	public int bpm;
	private int tpb;
	private float ratio;

	public void Read(String path) {
		// read midi file here
		try {
			Sequence sequence = MidiSystem.getSequence(new File(path));
			tpb = sequence.getResolution();
			for (int s = 0; s < sequence.getTracks().length; s++) {
				Track track = sequence.getTracks()[s];
				System.out.println("[MIDI files]: Track " + s + ": size = " + track.size());

				int min = 50000;
				int max = -50000;

				for (int i = 0; i < track.size(); i++) {
					MidiEvent event = track.get(i);
					MidiMessage message = event.getMessage();
					if (message instanceof ShortMessage) {
						ShortMessage sm = (ShortMessage) message;
						if (sm.getCommand() == NOTE_ON) {
							Key key = new Key();
							key.time = (long) (event.getTick() * ratio);
							key.notes = sm.getData1();
							key.notesName = key.notes % 12;
							key.volume = sm.getData2();
							key.length = sm.getLength();
							key.track = s;
							Keys.add(key);

							// compute min max
							if (sm.getData1() < min) {
								min = key.notes;
							}
							if (sm.getData1() > max) {
								max = key.notes;
							}
						}
					} else if (message instanceof MetaMessage) {
						MetaMessage metaMessage = (MetaMessage) message;
						if (metaMessage.getType() == SET_TEMPO) {
							byte[] data = metaMessage.getData();
							int tempo = (data[0] & 0xff) << 16 | (data[1] & 0xff) << 8 | (data[2] & 0xff);
							bpm = 60000000 / tempo;
							System.out.println("The calulated bpm is: " + bpm);
							ratio = (60000f / (bpm * tpb));
						}
					}
				}
				// end reading a track
				// save the min max of this track
				MinNotesOfTrack.add(min);
				MaxNotesOfTrack.add(max);
				System.out.println("[MIDI files]: Track " + s + ": min= " + min + ", max = " + max);
			}

		} catch (InvalidMidiDataException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		// sort the arraylist
		java.util.Collections.sort(Keys, new Comparator<Key>() {
			@Override
			public int compare(Key a, Key b) {
				if (a.time > b.time) {
					return 1;
				} else if (a.time < b.time) {
					return -1;
				} else {
					return 0;
				}
			}
		});
	}
}
