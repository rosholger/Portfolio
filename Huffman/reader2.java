/*Bitfillaesare.java
 *
 *Laeser en bit i taget från valfri fil (filnamn anges i konstruktorn)
 *
 */
import java.io.*;
public class reader2{
    public static final boolean ETTA  = true, NOLLA = false;

    private BufferedInputStream inlaesare;
    protected  boolean[] bitarray = new boolean[8];
    private int senasteBitindex = -1; //pos i bitarrayen
    private int senastLaestaByte=-2;

    //PUBLIKA metoder
    
    
    public reader2(String filnamn) throws FileNotFoundException, SecurityException, IOException{
        inlaesare = new BufferedInputStream(new FileInputStream(filnamn));
        laesNaestaByte();
    }

    public boolean laesNaestaBit() throws IOException{
        int index = ++senasteBitindex;
        boolean res = bitarray[index];
        if(senasteBitindex == 7){
            laesNaestaByte();
            senasteBitindex = -1;
        }
        return res;
    }


    public boolean aerSlut(){
        return (senastLaestaByte==-1) || inlaesare ==null;
    }


    public void close() throws IOException{
        if(inlaesare!=null)
            inlaesare.close();
    }

    // ----------------------------------------------
    protected void laesNaestaByte() throws IOException{
        senastLaestaByte = inlaesare.read();
        int decTal = senastLaestaByte;
        for(int n=0; n<8; n++){
            int pot2 = (int)Math.pow(2,n);
            bitarray[7-n] = (decTal&pot2)==pot2;
        }
        //byten blir endast ettor om vi når eol för -1 = 1111 1111 eller
    }
}