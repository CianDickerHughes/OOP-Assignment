// Cian Dicker-Hughes
// G00415413@atu.ie
package ie.atu.sw;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 
 * @author Cian Dicker-Hughes
 * @version 1.0
 * @since 1.8
 * 
 * The ProcessFile class processes text files by simplifying and replacing words
 * based on Google 1000 and custom word embeddings. It uses concurrent processing 
 * for efficiency and outputs the modified text to a file.
 * 
 * Features:
 * - Updates words using similar embeddings.
 * - Supports dynamic file paths for output.
 * - Utilizes virtual threads for parallel execution.
 * 
 * Key Methods:
 * - `setDictionaryFile(String)`: Sets the output file path.
 * - `execute(...)`: Performs word replacement and writes output.
 * - `whichDictionaryFile()`: Returns the current output file path.
 *  
 */


public class ProcessFile {
	private Set<String> words = new ConcurrentSkipListSet<>();
	private Map<String, String> updateWordMap = new ConcurrentHashMap<>();
	private List<String> originalText = new ArrayList<>();
	private ConcurrentMap<String, double[]> google = new ConcurrentHashMap<>();
	private ConcurrentMap<String, double[]> embeddings = new ConcurrentHashMap<>();
	private String dictionaryFile = "";

	public ProcessFile() {
		this.dictionaryFile = "out.txt"; // default file path
	}

	// change the file path to a new file
	public void setDictionaryFile(String filePath) {
		this.dictionaryFile = filePath;
	}

	public void execute(SimplifyingText simplifyingText, Google1000 google1000, WordEmbedding wordEmbedding) {
		originalText = simplifyingText.getOriginalText(); // Get words from ArrayList
		words = simplifyingText.getWords(); // Get words from ConcurrentSkipListSet
		google = google1000.getEmbeddings(); // Get Google 1000 embeddings
		embeddings = wordEmbedding.getEmbeddings(); // Get embeddings

		// Create a virtual thread ExecutorService
		try (ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory())) {

			// List to hold Future tasks for parallel processing
			List<Future<?>> tasks = new ArrayList<>();

			// Update words concurrently
			for (String word : words) {
				tasks.add(executor.submit(() -> {
					if (google.containsKey(word)) {
						// No action needed as word is already in Google 1000 set
					} else {
						if (embeddings.containsKey(word)) {
							List<String> similarWords = wordEmbedding.findTopNSimilarWords(word);
							System.out.println(similarWords + " " + word);
							if (!similarWords.isEmpty()) {
								String similarWord = similarWords.get(0); // Get the top similar word
								updateWordMap.put(word, similarWord);
							}
						}
					}
				}));
			}

			// Wait for all tasks to complete
			for (Future<?> task : tasks) {
				try {
					task.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}

			// Print to console Original Text
			System.out.println("Original Text");
			for (String line : originalText) {
				System.out.print(line + " ");
			}
			System.out.println("");

			// Update the originalText with the new modified words
			for (int i = 0; i < originalText.size(); i++) {
				String word = originalText.get(i);
				if (updateWordMap.containsKey(word)) {
					originalText.set(i, updateWordMap.get(word)); // Replace the word with the updated version
				}
			}

			// Print to console Modified Text
			System.out.println("Modified Text");
			for (String line : originalText) {
				System.out.print(line + " ");
			}
			System.out.println("");

			// Write Modified Text to File
			try (FileWriter writer = new FileWriter(dictionaryFile)) {
				for (String line : originalText) {
					writer.write(line + " ");
				}
				System.out.println("Output to: " + dictionaryFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // The executor will be automatically closed here
	}

	// get which file is being used
	public String whichDictionaryFile() {
		return dictionaryFile;
	}
}
