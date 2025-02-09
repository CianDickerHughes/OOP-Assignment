// Cian Dicker-Hughes
// G00415413@atu.ie
package ie.atu.sw;

import java.util.List;
import java.util.Map;

public abstract class AbstractEmbedding {

	// Abstract method to set the dictionary file
	public abstract void setDictionaryFile(String filePath);

	// Abstract method to check if the word exists in the embeddings
	public abstract boolean containsWord(String word);

	// Abstract method to get the word embedding for a specific word
	public abstract double[] getWordEmbedding(String word);

	// Abstract method to get which dictionary file is being used
	public abstract String whichDictionaryFile();

	// Abstract method to return the full map of embeddings
	public abstract Map<String, double[]> getEmbeddings();

	// Abstract method to get the size
	public abstract int getSize();

	// Abstract method to get embedding for a specific word
	public abstract double[] getEmbedding(String word);
}
