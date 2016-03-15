import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/** 
 * Kedjar flera ActionListeners så att de allas actionPerformed anropas "samtidigt"
 * @author holger
 *
 */
public class ActionListenerChainer implements ActionListener{
	private LinkedList<ActionListener> children = new LinkedList<ActionListener>();
	
	/**
	 * Lägg till en ActionListener i kedjan med barn.
	 * @param al
	 */
	public void addChild(ActionListener al) {
		children.add(al);
	}
	
	/**
	 * Ta bort en Actionlistener ur kedjan med barn.
	 * @param al
	 */
	public void removeChild(ActionListener al) {
		children.remove(al);
	}
	
	public void actionPerformed(ActionEvent e) {
		for (ActionListener listener : children) {
			listener.actionPerformed(e);
		}
	}
}
