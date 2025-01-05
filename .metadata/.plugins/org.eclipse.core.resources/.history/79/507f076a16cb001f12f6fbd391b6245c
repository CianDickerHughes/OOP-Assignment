// Cian Dicker-Hughes
// G00415413@atu.ie
package ie.atu.sw;

import java.util.Map;
import java.util.Scanner;

// This display all the Embeddings for Word Embeddings and Google1000
public class DisplayEmbeddings {
	public static void display(String dictionaryFile, Map<String, double[]> embeddings, Scanner input) {
		System.out.println("You are using " + dictionaryFile);
		System.out.println("Size of embeddings map: " + (embeddings != null ? embeddings.size() : 0));

		if (embeddings == null || embeddings.isEmpty()) {
			System.out.println("[INFO] The embeddings map is empty or not loaded.");
			return;
		}

		System.out.println("Do you want to display the file? (y or n)");
		char choiceFile = input.next().charAt(0);
		input.nextLine();

		if (choiceFile == 'y' || choiceFile == 'Y') {
			for (Map.Entry<String, double[]> entry : embeddings.entrySet()) {
				String word = entry.getKey();
				double[] embedding = entry.getValue();

				// Print the word and its embeddings
				System.out.print(word + ": [");
				for (int i = 0; i < embedding.length; i++) {
					System.out.print(embedding[i]);
					if (i < embedding.length - 1) {
						System.out.print(", "); // Add a comma only between elements
					}
				}
				System.out.println("]");
			}
			System.out.println("Size of embeddings map: " + embeddings.size()); // Print size again for clarity
		}
	}
}
