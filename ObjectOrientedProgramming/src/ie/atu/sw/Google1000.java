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
 *The Google1000 Class is where the file is load
 *in the ConcurrentHashMap and check Similarity of the Word 
 *Embedding of this program. 
 *
 */

public class Google1000 {
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
	public void load() {
		google1000.clear();
	    System.out.println("Loading google-1000 from file: " + dictionaryFile);
	    try (var br = new BufferedReader(new InputStreamReader(new FileInputStream(dictionaryFile)))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            line = line.trim();
	            String[] parts = line.split(",\\s*");
	            String word = parts[0];
	            double[] google = Arrays.stream(parts, 1, parts.length).mapToDouble(Double::parseDouble).toArray();
	            google1000.put(word, google);
	        }
	    } catch (IOException e) {
	        System.err.println("[ERROR] Encountered a problem reading the file: " + e.getMessage());
	        if (dictionaryFile.equals("google-1000.txt")) {
	            System.err.println("google-1000.txt file not found.");
	        } 
	        else {
	            System.out.println("Reverting to default file: google-1000.txt");
	            dictionaryFile = "google-1000.txt";
	            load(); // Reload using default file
	        }
	    }
	}
	
	
	public boolean containsWord(String word) {
	    return google1000.containsKey(word);
	}
	
	public int getSize() {
	    return google1000.size();
	}

	
	public Map<String, double[]> getEmbeddings() {
	    return Collections.unmodifiableMap(google1000);
	}


}
