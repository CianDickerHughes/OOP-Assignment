// Cian Dicker-Hughes
// G00415413@atu.ie
package ie.atu.sw;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.io.*;

public class SimplifyingText {
	private Set<String> words = new ConcurrentSkipListSet<>();
	private List<String> originalText = new ArrayList<>();
	private String dictionaryFile = "";

	public SimplifyingText() {
		this.dictionaryFile = "Parse-Text.txt"; // default file path
	}

	// change the file path to a new file
	public void setDictionaryFile(String filePath) {
		this.dictionaryFile = filePath;
	}

	// load the file to the map
	public void load() {
		words.clear();
		originalText.clear();
		System.out.println("Loading Parse-Text from file: " + dictionaryFile);
		try (BufferedReader br = new BufferedReader(new FileReader(dictionaryFile))) {
			String line;
			while ((line = br.readLine()) != null) {

				// Split the line into words using a regular expression
				String[] lineWords = line.split("\\W+"); // Splits on non-word characters

				for (String word : lineWords) {
					if (!word.isEmpty()) { // Skip empty strings
						originalText.add(word); // Add to the text list
						words.add(word.toLowerCase()); // Add to the set (convert to lowercase)
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		}
	}

	// get size for words
	public int getSize() {
		return words.size();
	}

	// get which file is being used
	public String whichDictionaryFile() {
		return dictionaryFile;
	}

	// getter for words set
	public Set<String> getWords() {
		return words;
	}

	public List<String> getOriginalText() {
		return originalText;
	}
}