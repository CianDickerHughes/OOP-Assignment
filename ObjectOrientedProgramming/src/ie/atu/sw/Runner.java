// Cian Dicker-Hughes
// G00415413@atu.ie
package ie.atu.sw;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 
 * @author Cian Dicker-Hughes
 * @version 1.1
 * @since 1.8
 *
 * The Runner class is the main entry point for the program, managing user interactions, 
 * file operations, and text processing tasks. It integrates various components like 
 * WordEmbedding, Google1000, and SimplifyingText to perform word analysis and text 
 * simplification. The program runs in a loop, presenting a menu to the user until 
 * they choose to exit.
 * 
 * Features:
 * - Load and process embedding files and text data.
 * - Provide options for customization of input/output files.
 * - Analyze and report word similarities using embeddings.
 * - Interactive menu system for seamless user operation.
 * 
 * Key Methods:
 * - `main()`: Initializes components, displays menu, and handles user input.
 *
 */

public class Runner {

	public static void main(String[] args) throws Exception {
		Scanner input = new Scanner(System.in); // Create a Scanner object
		String filePath = "", words = "";
		int choice;

		WordEmbedding wordEmbedding = new WordEmbedding();
		wordEmbedding.load();

		Google1000 google1000 = new Google1000();
		google1000.load(wordEmbedding);

		SimplifyingText simplifying = new SimplifyingText();
		simplifying.load();

		ProcessFile processor = new ProcessFile();

		System.out.println(wordEmbedding.getSize() + " : " + google1000.getSize() + " : " + simplifying.getSize());

		// Loop to display menu and handle options until quit
		while (true) {
			// main menu
			choice = MainMeunPage.meun();

			// You may want to include a progress meter in you assignment!
			System.out.print(ConsoleColour.YELLOW); // Change the colour of the console text
			int size = 100; // The size of the meter. 100 equates to 100%
			for (int i = 0; i < size; i++) { // The loop equates to a sequence of processing steps
				MainMeunPage.printProgress(i + 1, size); // After each (some) steps, update the progress meter
				Thread.sleep(10); // Slows things down so the animation is visible
			}

			System.out.println(ConsoleColour.BLACK_BOLD_BRIGHT);

			// Handle menu options
			switch (choice) {
			case 1: // New Embedding File or Default Embedding File
				System.out.println("\nEnter New Embedding File (leave empty to keep default):");
				input.nextLine();
				filePath = input.nextLine().trim();
				if (!filePath.isEmpty()) {
					wordEmbedding.setDictionaryFile(filePath);
				} else {
					processor.setDictionaryFile("embeddings.txt"); // Reset to default file
				}
				try {
					wordEmbedding.load();
					System.out.println("Embedding loaded successfully.\n");
				} catch (Exception e) {
					System.out.println("Error loading embedding: " + e.getMessage());
				}
				break;
			case 2: // New Google File or Default Google File
				System.out.println("\nEnter Google File (leave empty to keep default):");
				input.nextLine();
				filePath = input.nextLine().trim();
				if (!filePath.isEmpty()) {
					google1000.setDictionaryFile(filePath);
				} else {
					processor.setDictionaryFile("google-1000.txt"); // Reset to default file
				}
				try {
					google1000.load(wordEmbedding);
					System.out.println("Google loaded successfully.\n");
				} catch (Exception e) {
					System.out.println("Error loading Google: " + e.getMessage());
				}
				break;
			case 3: // New Output File or Default Output File
				System.out.println("\nEnter Output File (leave empty to keep default):");
				filePath = input.nextLine().trim();
				if (!filePath.isEmpty()) {
					processor.setDictionaryFile(filePath);
				} else {
					processor.setDictionaryFile("out.txt"); // Reset to default file
				}
				try {
					System.out.println("output loaded successfully.\n");
				} catch (Exception e) {
					System.out.println("Error loading output: " + e.getMessage());
				}
				break;
			case 4: // Execute, Analyse and Report
				System.out.println("\nEnter Text File to Simplify (leave empty to keep default):");
				filePath = input.nextLine().trim();

				if (!filePath.isEmpty()) {
					simplifying.setDictionaryFile(filePath); // Set the new Simplify Text File path
					simplifying.load();
				}

				try {
					System.out.println(
							"Output file set successfully: " + (filePath.isEmpty() ? "Default file" : filePath));
				} catch (Exception e) {
					System.out.println("Error setting output file: " + e.getMessage());
				}

				// Create a new ProcessFile instance and execute
				try {
					processor.execute(simplifying, google1000, wordEmbedding);
					System.out.println("Processing completed successfully.");
				} catch (Exception e) {
					System.out.println("An error occurred during processing: " + e.getMessage());
					e.printStackTrace(); // Optional: Print stack trace for debugging
				}
				break;
			case 5: // Which Embedding File being Used
				DisplayEmbeddings.display(wordEmbedding.whichDictionaryFile(), wordEmbedding.getEmbeddings(), input);
				break;
			case 6: // Which Google 1000 File being Used
				DisplayEmbeddings.display(google1000.whichDictionaryFile(), google1000.getEmbeddings(), input);
				break;
			case 7: // Which Output File being Used
				System.out.println("You are using " + processor.whichDictionaryFile());
				break;
			case 8: // Search Embeddings from Embedding File
				System.out.println("Enter a word from Word Embedding:");
				words = input.nextLine();
				if (wordEmbedding.containsWord(words)) {
					System.out.println(words + " found in embeddings.");
					double[] embedding = wordEmbedding.getWordEmbedding(words);
					System.out.println(Arrays.toString(embedding));
				} else {
					System.out.println("Word not found in embeddings.");
				}
				break;
			case 9: // Search Embeddings from Google 1000 File
				System.out.println("Enter a word from Google 1000:");
				words = input.nextLine();
				if (google1000.containsWord(words)) {
					System.out.println(words + " found in embeddings.");
					double[] embedding = google1000.getWordEmbedding(words);
					System.out.println(Arrays.toString(embedding));
				} else {
					System.out.println("Word not found in embeddings.");
				}
				break;
			case -1:
				System.out.println("Quitting program.");
				input.close(); // Close the scanner
				return; // Exit the main method
			default:
				System.out.println("Invalid selection.");
			}

		}
	}
}