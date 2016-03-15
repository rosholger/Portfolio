import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class BitFileReaderTest {
	
	String filnamn;
	boolean bitArray[] = new boolean[8];
	static BitFileReader bitFilereader;
	
    protected void setUp(String str){
        String filnamn = "test.txt";
        try {
            FileWriter fstream = new FileWriter(filnamn);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(str);
            out.close();
            bitFilereader = new BitFileReader(filnamn);
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
	}
    

	@Test
	public void testA() throws IOException {
		
		setUp("ABC 32"); // stringen som testas
		// A = 01000001

		for(int i = 0; i < 8; i++){
			if(bitFilereader.available()){
				bitArray[i] = bitFilereader.read();
				}
		}

		assertEquals(bitArray[0], false);
		assertEquals(bitArray[1], true);
		assertEquals(bitArray[2], false);
		assertEquals(bitArray[3], false);
		assertEquals(bitArray[4], false);
		assertEquals(bitArray[5], false);
		assertEquals(bitArray[6], false);
		assertEquals(bitArray[7], true);
	}
	@Test
	public void testB() throws IOException {
		// B,	01000010
		for(int i = 0; i < 8; i++){
			if(bitFilereader.available()){
				bitArray[i] = bitFilereader.read();
				}
		}

		assertEquals(bitArray[0], false);
		assertEquals(bitArray[1], true);
		assertEquals(bitArray[2], false);
		assertEquals(bitArray[3], false);
		assertEquals(bitArray[4], false);
		assertEquals(bitArray[5], false);
		assertEquals(bitArray[6], true);
		assertEquals(bitArray[7], false);
	}
	@Test
	public void testC() throws IOException {
		// C,01000011
		for(int i = 0; i < 8; i++){
			if(bitFilereader.available()){
				bitArray[i] = bitFilereader.read();
				}
		}

		assertEquals(bitArray[0], false);
		assertEquals(bitArray[1], true);
		assertEquals(bitArray[2], false);
		assertEquals(bitArray[3], false);
		assertEquals(bitArray[4], false);
		assertEquals(bitArray[5], false);
		assertEquals(bitArray[6], true);
		assertEquals(bitArray[7], true);
	}
	
	@Test
	public void testS() throws IOException {
		
		for(int i = 0; i < 8; i++){
			if(bitFilereader.available()){
				bitArray[i] = bitFilereader.read();
				}
		}
		// space 00100000 
		assertEquals(bitArray[0], false);
		assertEquals(bitArray[1], false);
		assertEquals(bitArray[2], true);
		assertEquals(bitArray[3], false);
		assertEquals(bitArray[4], false);
		assertEquals(bitArray[5], false);
		assertEquals(bitArray[6], false);
		assertEquals(bitArray[7], false);
	}
	@Test
	public void testNumber1() throws IOException {
		bitArray = new boolean[8];
		for(int i = 0; i < 8; i++){
			if(bitFilereader.available()){
				bitArray[i] = bitFilereader.read();
				}
			
		}
		// 3 0011 0011
		assertEquals(bitArray[0], false);
		assertEquals(bitArray[1], false);
		assertEquals(bitArray[2], true);
		assertEquals(bitArray[3], true);
		assertEquals(bitArray[4], false);
		assertEquals(bitArray[5], false);
		assertEquals(bitArray[6], true);
		assertEquals(bitArray[7], true);

	}

	@Test
	public void testNumber2() throws IOException{

		for(int i = 0; i < 8; i++){
			if(bitFilereader.available()){
				bitArray[i] = bitFilereader.read();
				}
			
		}
		// 2 0011 0010
		assertEquals(bitArray[0], false);
		assertEquals(bitArray[1], false);
		assertEquals(bitArray[2], true);
		assertEquals(bitArray[3], true);
		assertEquals(bitArray[4], false);
		assertEquals(bitArray[5], false);
		assertEquals(bitArray[6], true);
		assertEquals(bitArray[7], false);
	}




	

}
