import java.util.ArrayList;
import java.util.Comparator;

public class Thing{
	public int frequency;
	public char character;
	public Thing left;
	public Thing right;
	
	public Thing(){}
	public Thing(char chara, int freq) {
		this.frequency = freq;
		this.character = chara;
	}

	public int getFrequency(){
		return this.frequency;
	}
	public char getChar(){
		return this.character;
	}
	public void setFrequency(int freq){
		this.frequency = freq;
	}
	public String toString(){
		return "Character is : "+this.character+" frequency is : "+this.frequency;
	}

}
