package engine.utils;

import java.io.*;
import java.util.*;

public class FileUtils {

	private FileUtils() {

	}

	public static String loadAsString(String fileLocation) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
			String buffer = "";
			while ((buffer = reader.readLine()) != null) {
				result.append(buffer + '\n');
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	// Load the whole text into Arraylist of String
	public static ArrayList<String> loadAsStringArray(String fileLocation) {
		ArrayList<String> returnList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))) {
			String line;
			while ((line = br.readLine()) != null) {
				returnList.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnList;
	}

	// Only load the text under the hashtag
	public static ArrayList<String> loadAsStringArray(String fileLocation, String tag) {
		ArrayList<String> returnList = new ArrayList<>();

		boolean startRecording = false;
		try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))) {
			String line;
			while ((line = br.readLine()) != null) {
				// stop recording after it have reach the end
				if (line == "end" && startRecording) {
					break;
				}
				// record after it arrive the line with # (e.g. # MapSize)
				if (startRecording) {
					returnList.add(line);
				}
				// check when it arrive the line
				if (line.contains("#") && line.contains(tag)) {
					startRecording = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnList;
	}
}
