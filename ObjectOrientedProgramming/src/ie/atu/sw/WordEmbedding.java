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
 * The WordEmbedding class handles loading, storing, and managing word embeddings. 
 * 	It uses a ConcurrentHashMap to store word vectors and provides methods to find 
 * similar words based on their vector similarity using dot product.
 *
 * Features:
 * - Load word embeddings from a file into a ConcurrentHashMap.
 * - Retrieve word embeddings and check for the presence of a word.
 * - Find the top N similar words to a given word using vector similarity.
 * - Handle fallback to a default embedding file if the specified file fails to load.
 *
 * Key Methods:
 * - {@link #load()} - Loads word embeddings from a file into memory.
 * - {@link #findTopNSimilarWords(String)} - Finds similar words based on vector similarity.
 * - {@link #containsWord(String)} - Checks if a word exists in the embeddings.
 * - {@link #getWordEmbedding(String)} - Retrieves the embedding for a specific word.
 * - {@link #getEmbeddings()} - Returns the entire map of embeddings.
 * - {@link #getSize()} - Returns the size of the embeddings map.
 *
 */

public class WordEmbedding extends AbstractEmbedding {
	private ConcurrentMap<String, double[]> embeddings = new ConcurrentHashMap<>();
	private String dictionaryFile = "";

	public WordEmbedding() {
		this.dictionaryFile = "embeddings.txt"; // default file path
	}

	// change the file path to a new file
	public void setDictionaryFile(String filePath) {
		this.dictionaryFile = filePath;
	}

	/**
	 *
	 * load() function that loads in the file and store the contents to a
	 * ConcurrentHashMap. If it fail to load it will default to
	 * "word-embeddings.txt". If it can't find "word-embeddings.txt" it will
	 * continue the program.
	 * 
	 */

	// load the file to the map
	public void load() {
		embeddings.clear();
		System.out.println("Loading embeddings from file: " + dictionaryFile);
		try (var br = new BufferedReader(new InputStreamReader(new FileInputStream(dictionaryFile)))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				String[] parts = line.split(",\\s*");
				String word = parts[0];
				double[] embedding = Arrays.stream(parts, 1, parts.length).mapToDouble(Double::parseDouble).toArray();
				embeddings.put(word, embedding);
			}
		} catch (IOException e) {
			System.err.println("[ERROR] Encountered a problem reading the file: " + e.getMessage());
			if (dictionaryFile.equals("word-embeddings.txt")) {
				System.err.println("word-embeddings.txt file not found.");
			} else {
				System.out.println("Reverting to default file: word-embeddings.txt");
				dictionaryFile = "word-embeddings.txt";
				load(); // Reload using default file
			}
		}
	}

	/**
	 * 
	 * findTopNSimilarWords() function Compare the Similarity of Words by using
	 * Word-Embedding. It uses the Dot Product for the Comparing Vectors for
	 * Similarity.
	 * 
	 */

	// calculates the dot product between two vectors
	private double dotProduct(double[] vectorA, double[] vectorB) {
		double dotProduct = 0.0;
		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
		}
		return dotProduct;
	}

	// finds the top N similar words to the given word based on their embedding
	public List<String> findTopNSimilarWords(String word) {
		int n = 1;
		double[] targetEmbedding = embeddings.get(word);
		if (targetEmbedding == null) {
			System.out.println("Word not found in embeddings.");
			return Collections.emptyList();
		}

		// use a priority queue to keep track of top N similar words
		PriorityQueue<Map.Entry<String, Double>> pq = new PriorityQueue<>(Map.Entry.comparingByValue());

		for (Map.Entry<String, double[]> entry : embeddings.entrySet()) {
			if (!entry.getKey().equals(word)) {
				double similarity = dotProduct(targetEmbedding, entry.getValue());
				pq.offer(new AbstractMap.SimpleEntry<>(entry.getKey(), similarity));
				if (pq.size() > n) {
					pq.poll();
				}
			}
		}

		// extract words from the priority queue
		List<String> topNWords = new ArrayList<>();
		while (!pq.isEmpty()) {
			topNWords.add(0, pq.poll().getKey());
		}

		return topNWords;
	}

	// is the word in the map/array
	@Override
	public boolean containsWord(String word) {
		return embeddings.containsKey(word);
	}

	// get the word Numbers
	@Override
	public double[] getWordEmbedding(String word) {
		return embeddings.get(word);
	}

	// get which file is being used
	@Override
	public String whichDictionaryFile() {
		return dictionaryFile;
	}

	// return the full map/array
	@Override
	public ConcurrentMap<String, double[]> getEmbeddings() {
		return this.embeddings;
	}

	// get size of map
	@Override
	public int getSize() {
		return embeddings.size();
	}

	@Override
	public double[] getEmbedding(String word) {
		return embeddings.get(word);
	}
}