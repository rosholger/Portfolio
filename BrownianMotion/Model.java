import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * Model av brownsk rörelse.
 * @author Holger och Fredrik
 *
 */
public class Model implements ActionListener {
	
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	private float simAreaMinX;
	private float simAreaMinY;
	private float simAreaMaxX;
	private float simAreaMaxY;
	private boolean isPaused = true;
	
	/**
	 * Konstruktor.
	 * Skapar numberOfParticles instanser av Particle och lagrar dessa i ArrayList
	 * Sätter isMoving till sant för alla partiklar.
	 * @param numberOfParticles
	 * @param startCircleRadius
	 */
	public Model(int numberOfParticles, int startCircleRadius, int x, int y,
					float simAreaMinX, float simAreaMinY, float simAreaMaxX, float simAreaMaxY){
		this.simAreaMinX = simAreaMinX;
		this.simAreaMinY = simAreaMinY;
		this.simAreaMaxX = simAreaMaxX;
		this.simAreaMaxY = simAreaMaxY;
		// TODO:felhantering för args
		for(int i = numberOfParticles; i>0; i--){
			particles.add(new Particle(x,y,startCircleRadius));
			particles.get(particles.size() - 1).setIsMoving(true);
		}
		//selectTrackedParticles();
	}
	
	public void setPaused(boolean paused) {
		isPaused = paused;
	}
	
	public boolean getPaused() {
		return isPaused;
	}
	
	/**
	 * Returnerar de simulerade partiklarna.
	 * @return
	 */
	public ArrayList<Particle> getParticles() {
		return particles;
	}

	/**
	 * Väljer ut 10 partiklar ur ArrayList particles,
	 * lägger till dessa i ArrayList specialParticles.
	 */
	//void selectTrackedParticles(){
		//for(int i = 0; i < 10; ++i) {
			//int randomInt = new Random().nextInt(particles.size());
			//trackedParticles.add(particles.get(randomInt));
		//}
	//}

	
	/**
	 * Flyttar alla partiklar i ArrayList particle
	 * OMM isMoving
	 */
	void moveAll(){
		for(int i = 0; i < this.particles.size(); i++){
			if(particles.get(i).getX() <= simAreaMinX || particles.get(i).getY() <= simAreaMinY || 
					particles.get(i).getY() >= simAreaMaxX || particles.get(i).getX() >= simAreaMaxY) {
				particles.get(i).setIsMoving(false);
			}
			if(this.particles.get(i).getIsMoving()) {
				this.particles.get(i).randomMove();
			}
			
		}
	}

	/**
	 * Setter Particle.moveLength.
	 * @param value
	 */
	void setL(double value){
		Particle.moveLength = value;
	}

	/**
	 * Kör moveAll()
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		if (!isPaused) {
			moveAll();
		}
	}
	
}
