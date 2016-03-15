import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * A BitFileWriter writes single bits to a file in a file system.
 * The last byte of the file is padded with the value of the last
 * intentionaly writen bit, eg 101 -> 11111101 and 010 -> 00000010.
 * @author holger
 *
 */
public class BitFileWriter implements Closeable, AutoCloseable {

	private byte bufferedByte = 0;
	private byte numberOffBufferedBits = 0;
	private FileOutputStream outputStream;
	
	/**
	 * Constructs a BitFileWriter from a file name.
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public BitFileWriter(String fileName) throws FileNotFoundException {
		outputStream = new FileOutputStream(fileName);
	}
	
	/**
	 * Writes a single bit, 1 if isBitSet is true, otherwise 0.
	 * @param isBitSet
	 * @throws IOException
	 */
	public void write(boolean isBitSet) throws IOException{
		bufferedByte <<= 1; // shift it down to leave space for the new bit.
		bufferedByte &= ~1; // we need to make shore that the topmost bit is not set
		bufferedByte |= (isBitSet ? (byte)1 : 0); // set it if it should be set
		numberOffBufferedBits++;
		if (numberOffBufferedBits == 8) { // if we have writen 8 bits the byte is full, so we write it out.
			numberOffBufferedBits = 0;
			outputStream.write(bufferedByte);
			bufferedByte = 0;
		}
	}
	
	/**
	 * Closes the writer and flushes its contents to the file.
	 */
	public void close() throws IOException {
		if (numberOffBufferedBits != 0) { // if there are anything left in bufferedByte we need to write that.
			outputStream.write((byte)(bufferedByte << (8 - numberOffBufferedBits)));
			// Shift it down so that the unwriten bits is at the end instead of at the begining.
		}
		outputStream.flush();
		outputStream.close();
	}
	
	public static void main(String[] args) throws IOException, FileNotFoundException {
		//BitFileWriter bfw = new BitFileWriter("test");
		boolean[] data = new boolean[500000];
		Random rnd = new Random();
		for (int i = 0; i < data.length; ++i) {
			data[i] = rnd.nextBoolean();
		}
		for (boolean d : data) {
			//bfw.write(d);
		}
		//bfw.close();
		BitFileReader bfr = new BitFileReader("test");
		for (int i = 0; i < 8; ++i) {
			//boolean d = data[i];
			if (bfr.available()) {
				boolean read = bfr.read();
				System.out.print(read ? "1" : "0");
			} else {
				System.out.println("Data that should be available is not!");
			}
		}
		System.out.println("");
		bfr.close();
	}
}