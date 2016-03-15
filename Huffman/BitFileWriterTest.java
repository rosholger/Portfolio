import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

public class BitFileWriterTest {
	String filnamn;

	@Test
	public void test()  throws IOException {
		BitFileWriter writer = new BitFileWriter("test.txt");
		// A
		writer.write(false);
		writer.write(true);
		writer.write(false);
		writer.write(false);
		writer.write(false);
		writer.write(false);
		writer.write(false);
		writer.write(true);
		// B
		writer.write(false);
		writer.write(true);
		writer.write(false);
		writer.write(false);
		writer.write(false);
		writer.write(false);
		writer.write(true);
		writer.write(false);
		// C
		writer.write(false);
		writer.write(true);
		writer.write(false);
		writer.write(false);
		writer.write(false);
		writer.write(false);
		writer.write(true);
		writer.write(true);
		// Space
		writer.write(false);
		writer.write(false);
		writer.write(true);
		writer.write(false);
		writer.write(false);
		writer.write(false);
		writer.write(false);
		writer.write(false);
		// 3
		writer.write(false);
		writer.write(false);
		writer.write(true);
		writer.write(true);
		writer.write(false);
		writer.write(false);
		writer.write(true);
		writer.write(true);
		// 2
		writer.write(false);
		writer.write(false);
		writer.write(true);
		writer.write(true);
		writer.write(false);
		writer.write(false);
		writer.write(true);
		writer.write(false);
		writer.close();
		FileInputStream stream = new FileInputStream("test.txt");
		byte[] data = new byte[6];
		stream.read(data);
		stream.close();
		assertEquals("ABC 32", new String(data));
	}
}
