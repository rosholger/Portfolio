import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A BitFileReader obtains single bits from a file in the file system.
 * @author holger
 *
 */
public class BitFileReader implements Closeable, AutoCloseable {
	private byte bufferedByte;
	private byte numberOfReadBits = 0;
	private FileInputStream inputStream;

	
	/**
	 * Constructs BitFileReader from a filename.
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public BitFileReader(String filename) throws FileNotFoundException {
		inputStream = new FileInputStream(filename);
	}
	
	/**
	 * Returns true if more bits can be read from this reader.
	 * @return
	 * @throws IOException
	 */
	public boolean available() throws IOException {
		return (numberOfReadBits != 0) || (inputStream.available() != 0);
	}
	
	/**
	 * Reads a single byte and returns it as a boolean, true = bit is 1 and false = bit is 0.
	 * @return
	 * @throws IOException
	 */
	public boolean read() throws IOException {
		if (numberOfReadBits == 0) { // if all bits from bufferedByte has been read we need to read in another one.
			int read = inputStream.read(); // why is this an int, when the spec says it reads a byte?
			bufferedByte = (byte)(read);
		}
		boolean result = (bufferedByte & 0b10000000) != 0; // is the first bit set?
		bufferedByte = (byte)(bufferedByte << 1); // shift it down so that the next time the first bit will be the current second one.
		numberOfReadBits = (byte)((numberOfReadBits + 1) % 8); // % 8 = if its 8 set it to 0
		return result;
	}
	
	/**
	 * Closes this reader.
	 */
	public void close() throws IOException {
		inputStream.close();
	}
}