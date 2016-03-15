import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * A huffman tree, used for compression.
 * @author holger
 *
 */
public class HuffmanTree implements Comparable<HuffmanTree>{
	
	private byte character;
    private int frequency;
	private HuffmanTree left;
	private HuffmanTree  right;
	
	/**
	 * Creates a leaf with a character and a frequency.
	 * @param character
	 * @param frequency
	 */
	public HuffmanTree(byte character, int frequency) {
		this.character = character;
		this.frequency = frequency;
	}
	
	/**
	 * Creates a non-leaf node.
	 * @param left
	 * @param right
	 */
	public HuffmanTree(HuffmanTree left, HuffmanTree right) {
		frequency = left.frequency + right.frequency;
		this.left = left;
		this.right = right;
	}
	
	/**
	 * Compare this tree to another one, from Comparable.
	 * @param other
	 */
	public int compareTo(HuffmanTree other) {
		return frequency - other.frequency;
	}

	/**
	 * Represent this node as a string.
	 */
	public String toString(){
		return character+" "+frequency;
	}
	
	/**
	 * Decompress a byte using the BitFileReader reader.
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public byte decompressByte(BitFileReader reader) throws IOException {
		if (left == null) {
			return character;
		}
		boolean read = reader.read();
		if (read) {
			return right.decompressByte(reader);
		} else {
			return left.decompressByte(reader);
		}
	}
	
	/**
	 * Recursively fill codeMap with the bitcodes for each leaf in this tree.
	 * @param codeMap
	 * @param code
	 */
	public void fillCodeMap(Map<Byte, Boolean[]> codeMap, ArrayList<Boolean> code) {
		if(this.left == null){ // löv
			Boolean[] codeArray = new Boolean[code.size()];
			code.toArray(codeArray);
			codeMap.put(character, codeArray);
		}
		else{
			code.add(false);
			this.left.fillCodeMap(codeMap, code);
			code.set(code.size()-1, true);
			this.right.fillCodeMap(codeMap, code);
			code.remove(code.size()-1);
		}
	}

	 /**
	  * Create a code map representing the bitcodes for each leaf in this tree.
	  * Conveniancy method for fillCodeMap.
	  * @return
	  */
    public Map<Byte, Boolean[]> createCodeMap(){
    	Map<Byte, Boolean[]> codeMap = new HashMap<Byte, Boolean[]>();
		fillCodeMap(codeMap, new ArrayList<Boolean>());
    	return codeMap;
    }

	 /**
	  * Creates a complete HuffmanTree from a FrequencyTable.
	  * @param treeQueue
	  * @return
	  */
	public static HuffmanTree createTree(PriorityQueue<HuffmanTree> treeQueue){
		while(treeQueue.size() > 1){
			treeQueue.add(new HuffmanTree(treeQueue.remove(), treeQueue.remove()));
		}
		return treeQueue.remove(); // root är trädet.
	}
    
	public static void main(String[] args) {
		boolean failed = false;
		if (args.length > 0) {
			if (args[0].equals("decompress")) {
				if (args.length > 1) {
					String compressedName = args[1];
					String outputName = ""; // GAH! It will always be initialized when its used stupeid java!!!!
					if (args.length > 2) {
						outputName = args[2];
					} else if (compressedName.endsWith(".huff")) {
						outputName = compressedName.substring(0, compressedName.length()-5);
					} else {
						System.err.println("Unnamed output file only works if compressed file end with .huff");
						failed = true;
					}
					String frequencyName = "";
					if (args.length > 3) {
						frequencyName = args[3];
					} else if (compressedName.endsWith(".huff")) {
						frequencyName = compressedName.substring(0, compressedName.length()-5)+".freq";
					} else {
						System.err.println("Unnamed frequency file only works if compressed file end with .huff");
						failed = true;
					}
					if (!failed) {
						FrequencyTable.decompress(compressedName, frequencyName, outputName);
					}
				}
			} else {
				String inputName = args[0];
				String compressedName = inputName + ".huff";
				if (args.length > 1) {
					compressedName = args[1];
				}
				String frequencyName = inputName + ".freq";
				if (args.length > 2) {
					frequencyName = args[2];
				}
				FrequencyTable.compress(inputName, compressedName, frequencyName);
			}
		} else {
			failed = true;
		}
		if (failed) {
			System.out.println("Usage compression: java HuffmanTree inputName [compressedName [frequencyName]]");
			System.out.println("Usage decompression: java HuffmanTree decompress compressedName [outputName [frequencyName]]");
		}
	}
}