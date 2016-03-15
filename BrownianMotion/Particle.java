import java.util.*;

/**
 * En "brownsk" partikel.
 * @author Holger och Fredrik
 *
 */
public class Particle {
	
	private Random rnd = new Random();
	private double x, y;
	boolean isMoving = true; // Rörlig eller ej, rörlig från början
	public static double moveLength = 3; // Alla partiklar tar lika stora steg.

	/**
	 * Placerar partikeln på en slumpad punkt inuti cirkeln som beskrivs av argumenten.
	 * @param startCircleCenterX
	 * @param startCircleCenterY
	 * @param startCircleRadius
	 */
	Particle (double startCircleCenterX, double startCircleCenterY, double startCircleRadius){
		double v = rnd.nextDouble()*2;
		x = startCircleCenterX + rnd.nextDouble()*startCircleRadius*Math.cos(v*Math.PI);
		y = startCircleCenterY + rnd.nextDouble()*startCircleRadius*Math.sin(v*Math.PI);
	}
	
	/**
	 * Placerar partikeln på xs,ys.
	 * @param xs
	 * @param ys
	 */
	Particle (double xs, double ys){
		x = xs;
		y = ys;
	}
	
	
	/**
	 * returnerar x.
	 * @return
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * returnerar y.
	 * @return
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Sätter isMoving
	 * @param isMoving
	 */
	public void setIsMoving(boolean isMoving){
		this.isMoving = isMoving;
	}
	
	/**
	 * returnerar huruvida partikeln är i rörelse.
	 * @return
	 */
	public boolean getIsMoving(){
		return isMoving;
	}

	/**
	 * Förflytta partikeln moveLength långt i en slumpad riktning.
	 */
	public void randomMove(){
		double v = rnd.nextDouble()*2;
		x += moveLength*Math.cos(v*Math.PI);
		y += moveLength*Math.sin(v*Math.PI);
		if(x > 697){
			x = 697;
		}
		if(x < -100){
			x = -100;
		}
		if(y > 410){
			y = 410;
		}
		if(y < -100){
			y = -100;
		}
	}
}
