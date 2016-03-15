import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Represents the frequency of characters in a file.
 * @author holger
 *
 */
public class FrequencyTable { 
	 
	 HashMap<Byte, Integer> frequencyMap = new HashMap<Byte, Integer>();
	 PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
	 
	 private FrequencyTable() {
	 }
	 
	 /**
	  * Creates a FrequencyTable from the byte array data.
	  * @param data
	  */
	 public FrequencyTable(byte[] data) {
    	for(int i = 0; i < data.length; ++i){
    		byte c = data[i];
    		Integer frequency = frequencyMap.get(new Byte(c));
    		if(frequency != null){
    			frequencyMap.put(c, new Integer(frequency + 1));
    		}else{
    			frequencyMap.put(c, 1);
    		}
    	}
    	frequencyMap.put((byte)0, 1);
        addsToPrioQueue();
	 }
	
   	/*
   	 * 	Creates a HuffmanTree of each element of frequencyMap,
   	 *  and adds that object to the PriorityQueue trees.	
   	 */
	public void addsToPrioQueue(){
		for (byte c : frequencyMap.keySet())
		{
		   HuffmanTree node = new HuffmanTree(c, frequencyMap.get(c));
		   trees.add(node);
		}	
	}	
    
	/**
	 * Deserialize a FrequencyTable from a scanner and returns it.
	 * This method expects the format
	 * <format> := <entry>*
	 * <entry> := BYTE INT
	 * @param scanner
	 * @return
	 */
    public static FrequencyTable deserialize(Scanner scanner) {
    	FrequencyTable ret = new FrequencyTable();
    	while (scanner.hasNext()) {
    		ret.frequencyMap.put((byte)scanner.nextInt(), scanner.nextInt());
    		
    	}
        ret.addsToPrioQueue();
    	return ret;
    }
    
    /**
     * Serializes this FrequencyTable to the file with the name freqTableName using the format
     * <format> := <entry>*
     * <entry> := BYTE INT.
     * @param freqTableName
     * @return
     */
    public boolean serialize(String freqTableName) {
        try (BufferedWriter freqTableWriter = new BufferedWriter(new FileWriter(freqTableName))) {
			for(Entry<Byte, Integer> c : frequencyMap.entrySet()){
				freqTableWriter.write(c.getKey() + " " + c.getValue() + "\n");
			}
			freqTableWriter.flush();
        } catch (FileNotFoundException e) {
        	System.err.println("Could not open " + freqTableName + " for writing");
        	return false;
        } catch(IOException e) {
			System.err.println("Unknown IOException while serializing frequency table to " + freqTableName);
			e.printStackTrace();
			return false;
        }
        return true;
    }
    
    /**
     * Compress the file filename to the file compressedName and serialize the FrequencyTable of the
     * file to freqTableName.
     * @param filename
     * @param compressedName
     * @param freqTableName
     */
    public static void compress(String filename, String compressedName, String freqTableName) {
		// Create Frequency table from file.
		File file = new File(filename);
		
		byte[] fileData = new byte[(int)file.length()];
		try (FileInputStream fileToComprimate = new FileInputStream(file)) {
			fileToComprimate.read(fileData);
		} catch (FileNotFoundException fnfe) {
			System.err.println(filename + " not found");
			return;
		} catch (IOException e) {
			System.err.println("Unknown IOException while reading from " + filename);
			e.printStackTrace();
			return;
		}
		FrequencyTable table = new FrequencyTable(fileData);
		HuffmanTree tree = HuffmanTree.createTree(table.trees);

		// Serialize the table to a file.
		if (!table.serialize(freqTableName)) {
			return;
		}
        
        // Compress the file.
        Map<Byte, Boolean[]> codeMap = tree.createCodeMap();
        try (BitFileWriter writer = new BitFileWriter(compressedName)) {
			for (byte b : fileData) {
				Boolean[] code = codeMap.get(new Byte(b));
				for (boolean bit : code) {
					writer.write(bit);
				}
			}
			for (boolean bit : codeMap.get(new Byte((byte)0))) {
					writer.write(bit);
			}
        } catch (FileNotFoundException e) {
        	System.err.println("Could not open " + compressedName + " for writing");
        	return;
        } catch (IOException e) {
			System.err.println("Unknown IOException while writing to " + compressedName);
			e.printStackTrace();
			return;
        }
    }
    
    /**
     * Decompress the file described by compressedName and freqTableName, and write the decompressed file to
     * outputName.
     * @param compressedName
     * @param freqName
     * @param outputName
     */
    public static void decompress(String compressedName, String freqTableName, String outputName) {
        // Recreate the table from file.
        Scanner freqTableScanner = null;
        try {
        	freqTableScanner = new Scanner(new File(freqTableName));
        } catch(FileNotFoundException e) {
        	System.err.println("Could not find file " + freqTableName);
        }
        FrequencyTable table = deserialize(freqTableScanner);
        freqTableScanner.close();
		HuffmanTree tree = HuffmanTree.createTree(table.trees);
        
        // Decompress the file to a different file.
        try (BitFileReader bfr = new BitFileReader(compressedName);
        		FileOutputStream decodedOut = new FileOutputStream(outputName)) {
			while (bfr.available()) {
				byte decompressed = tree.decompressByte(bfr);
				if (decompressed == (byte)0) {
					break;
				}
				decodedOut.write(decompressed);
			}
        } catch (FileNotFoundException e) {
        	System.err.println("Either the file " + compressedName + " or " + outputName +
        			" could not be found/opened");
        } catch (IOException e) {
        	System.err.println("Unknown IOException happened while decompressing " + compressedName + 
        			" to " + outputName);
        }
    }
}
