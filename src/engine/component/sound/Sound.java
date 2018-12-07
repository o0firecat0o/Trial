package engine.component.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.WaveData;

public class Sound {
	private final static Map<String, Integer> NAME_ID_DICTIONARY = new HashMap<String, Integer>();

	public static int createSound(String path, String name) {
		if (NAME_ID_DICTIONARY.containsKey(name)) {
			return NAME_ID_DICTIONARY.get(name);
		} else {
			int sound = load(path);
			NAME_ID_DICTIONARY.put(name, sound);
			return sound;
		}
	}

	public static int getSound(String name) {
		try {
			return NAME_ID_DICTIONARY.get(name);
		} catch (Exception e) {
			System.out.println("Sound: " + name + " does not exist");
			System.exit(0);
			return -1;
		}
	}

	private static int load(String path) {
		int buffer = AL10.alGenBuffers();
		try {
			WaveData waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(path)));
			AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
		} catch (FileNotFoundException e) {
			System.out.println(path + " not found in folder. @Sound, function load(String Path)");
			e.printStackTrace();
		}
		return buffer;
	}

	public static void cleanUp() {
		for (Map.Entry<String, Integer> entry : NAME_ID_DICTIONARY.entrySet()) {
			int ID = entry.getValue();
			AL10.alDeleteBuffers(ID);
		}
	}

	public static void LoadDefault(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				LoadDefault(fileEntry);
			} else {
				Sound.createSound(fileEntry.getPath(),
						fileEntry.getName().substring(0, fileEntry.getName().lastIndexOf('.')));
			}
		}
	}
}
