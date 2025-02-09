// Cian Dicker-Hughes
// G00415413@atu.ie
package ie.atu.sw;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.io.*;

/**
 * 
 * @author Cian Dicker-Hughes
 * @version 1.0
 * @since 1.8
 *
 * The Google1000 class manages the loading and processing of Google 1000 word embeddings. 
 * It extends the AbstractEmbedding class and interacts with the WordEmbedding class to ensure 
 * embeddings from WordEmbedding are used wherever available, otherwise uses default embeddings.
 * 
 * Features:
 * - Load Google 1000 embeddings from a file into a ConcurrentHashMap.
 * - Ensure that embeddings from the WordEmbedding class are prioritized when available.
 * - Handle fallback to a default Google 1000 embedding file if the specified file fails to load.
 * 
 * Key Methods:
 * - {@link #load(WordEmbedding)} - Loads embeddings and integrates them with WordEmbedding if available.
 * - {@link #containsWord(String)} - Checks if a word exists in the Google 1000 embeddings.
 * - {@link #getWordEmbedding(String)} - Retrieves the embedding for a specific word.
 * - {@link #getEmbeddings()} - Returns the map of Google 1000 embeddings.
 * - {@link #whichDictionaryFile()} - Returns the currently used Google 1000 file path.
 * 
 */

public class Google1000 extends AbstractEmbedding {
	private ConcurrentMap<String, double[]> google1000 = new ConcurrentHashMap<>();
	private String dictionaryFile = "";

	public Google1000() {
		this.dictionaryFile = "google-1000.txt"; // default file path
	}

	// change the file path to a new file
	public void setDictionaryFile(String filePath) {
		this.dictionaryFile = filePath;
	}

	// load the file to the map
	public void load(WordEmbedding wordEmbedding) {
		google1000.clear();
		System.out.println("Loading google-1000 from file: " + dictionaryFile);
		try (var br = new BufferedReader(new InputStreamReader(new FileInputStream(dictionaryFile)))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				String[] parts = line.split(",\\s*");
				String word = parts[0];
				double[] embedding = Arrays.stream(parts, 1, parts.length).mapToDouble(Double::parseDouble).toArray();

				// Check if the word exists in WordEmbedding
				if (wordEmbedding.containsWord(word)) {
					double[] wordEmbeddingValue = wordEmbedding.getEmbedding(word);
					google1000.put(word, wordEmbeddingValue); // Use embedding from WordEmbedding
				} else {
					google1000.put(word, embedding); // Use default embedding
				}
			}
		} catch (IOException e) {
			System.err.println("[ERROR] Encountered a problem reading the file: " + e.getMessage());
			if (dictionaryFile.equals("google-1000.txt")) {
				System.err.println("google-1000.txt file not found.");
			} else {
				System.out.println("Reverting to default file: google-1000.txt");
				dictionaryFile = "google-1000.txt";
				load(wordEmbedding); // Reload using default file
			}
		}
	}

	@Override
	public int getSize() {
		return google1000.size();
	}

	@Override
	public boolean containsWord(String word) {
		return google1000.containsKey(word);
	}

	public ConcurrentMap<String, double[]> getEmbeddings() {
		return this.google1000;
	}

	// get which file is being used
	public String whichDictionaryFile() {
		return dictionaryFile;
	}

	// get size of map
	public int getSizeEmbedding() {
		return google1000.size();
	}

	@Override
	public double[] getWordEmbedding(String word) {
		// TODO Auto-generated method stub
		return google1000.get(word);
	}

	@Override
	public double[] getEmbedding(String word) {
		// TODO Auto-generated method stub
		return google1000.get(word);
	}

}
