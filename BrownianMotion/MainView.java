import java.awt.event.*;
import java.util.*;
import java.util.List;

import java.awt.*;

 /**
  * View till brownsk rörelse.
  * @author Holger och Fredrik
  *
  */
public class MainView extends javax.swing.JPanel implements ActionListener{

	private Model model;
	private static final Color PARTICLE_COLOR = new Color(0, 0, 255);
	private static final List<String> trackedParticlesColors = Arrays.asList("#FF0000", "#FF33FF", "#0000FF", "#FFFFFF", "#FF8000", "#FFF801", "#FF007F", "#CC00CC", "#FFCCCC", "#00FFFF");
	private ArrayList<ArrayList<Position>> trackedParticlesPaths = new ArrayList<ArrayList<Position>>();
	private ArrayList<Integer> trackedParticleIndices = new ArrayList<Integer>();

	MainView(Model model) {
		super();
		this.model = model;
		setBackground(new Color(0, 0, 0));
		Random rnd = new Random();
		for (int i = 0; i < 10; ++i) {
			trackedParticlesPaths.add(new ArrayList<Position>());
		}
	}
	
	//public Dimension getPreferredSize() {
	//	return new Dimension(480, 360);
	//}
	
	/**
	 * Lägg till partikeln med index index till de partiklar som blir "följda".
	 * index måste ligga mellan 0 och 9.
	 * @param index
	 */
	public void addTrackedParticle(int index) {
		if (index < 0 || index >= 10) {
			throw new IllegalArgumentException("index = " + index + " but is only allowed to be between 0 and 9");
		}
		trackedParticleIndices.add(index);
	}
	
	/**
	 * Sluta följa partikeln med index index.
	 * index måste ligga mella 0 och 9.
	 * @param index
	 */
	public void removeTrackedParticle(int index) {
		if (index < 0 || index >= 10) {
			throw new IllegalArgumentException("index = " + index + " but is only allowed to be between 0 and 9");
		}
		for (int i = 0; i < trackedParticleIndices.size(); ++i) {
			if (trackedParticleIndices.get(i).intValue() == index) {
				trackedParticleIndices.remove(i);
				break;
			}
		}
	}
	
	/**
	 * Rita partiklarna samt "vägarna" som de "följda" partiklarna tagit.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ArrayList<Particle> particles = model.getParticles();
		for (int i = 0; i < particles.size(); ++i) {
			paintParticle(g, particles.get(i), PARTICLE_COLOR);
		    if (trackedParticleIndices.contains(new Integer(i))) {
		    	if (particles.get(i).isMoving) {
		    		trackedParticlesPaths.get(i).add(new Position((int)particles.get(i).getX()+100,
		    				(int)particles.get(i).getY()+100));
		    	}
		    }
		}
		for (int i = 0; i < trackedParticlesPaths.size(); ++i) {
		    if (trackedParticleIndices.contains(new Integer(i))) {
				ArrayList<Position> path = trackedParticlesPaths.get(i);
				Color color = Color.decode(trackedParticlesColors.get(i));
				paintPath(g, path, color, "" + i);
				if (path.size() > 0) {
					paintCrossHair(g, path.get(path.size()-1), color);
					g.setColor(Color.white);
					g.drawString("" + i, (int)path.get(path.size()-1).x, 10);
					g.drawString("" + i, 0, (int)path.get(path.size()-1).y);
				}
		    }
		}
		//for (Integer trackedIndex : trackedParticleIndices) {
			//g.setColor(Color.decode(trackedParticlesColors.get(trackedIndex)));
			//g.fillRect((int)particles.get(trackedIndex).getX()+100-2,
					//(int)particles.get(trackedIndex).getY()+100-2, 4, 4);
			//g.drawLine(0, (int)particles.get(trackedIndex).getY()+100, getWidth(),
					//(int)particles.get(trackedIndex).getY()+100);
			//g.drawLine((int)particles.get(trackedIndex).getX()+100, 0,
					//(int)particles.get(trackedIndex).getX()+100, getHeight());
		//}

		g.dispose();
		Toolkit.getDefaultToolkit().sync(); // detta e ett hack, ignorera den bara...
	}
	
	/**
	 * Rita partikeln particle på g med färgen color.
	 * @param g
	 * @param particle
	 * @param color
	 */
	private void paintParticle(Graphics g, Particle particle, Color color) {
		g.setColor(color);
		g.fillRect((int)particle.getX()+100,
			   (int)particle.getY()+100, 2, 2);
	}
	
	/**
	 * Rita "Vägen" path i färgen color.
	 * @param g
	 * @param path
	 * @param color
	 * @param pathName
	 */
	private void paintPath(Graphics g, ArrayList<Position> path, Color color, String pathName) {
		g.setColor(color);
		for (int i = 0; i < path.size()-1 && path.size() > 1; ++i) {
			g.drawLine(path.get(i).x, path.get(i).y, path.get(i+1).x, path.get(i+1).y);
		}
	}
	
	/**
	 * Ritar ett "Sikte" i färgen color på g med sitt centrum i center.
	 * @param g
	 * @param center
	 * @param color
	 */
	private void paintCrossHair(Graphics g, Position center, Color color) {
		g.setColor(color);
		g.drawLine(0, (int)center.y, getWidth(),
				(int)center.y);
		g.drawLine((int)center.x, 0,
				(int)center.x, getHeight());
	}

	/**
	 * Begär omritning.
	 */
	public void actionPerformed(ActionEvent event) {
		repaint(); // denna 'begär' en omritning, dvs paintComponent anropas med rätt Graphics.
	}
}
