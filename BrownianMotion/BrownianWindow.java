import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The main window/main object, starts all the other things. Based on the SmallJFrame example.
 * @author Holger och Fredrik
 *
 */
public class BrownianWindow extends JFrame{
	public Timer timed;
	private JPanel mPanel = new JPanel();

	BrownianWindow () {
		
		setTitle("Brownsk rörelse?");
		setLayout(new FlowLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Model model = new Model(2200, 3, 250, 200, -100, -100, 410, 697);
		ActionListenerChainer chainer = new ActionListenerChainer();
		MainView mainView = new MainView(model);
		chainer.addChild(mainView);
		TableView tableView = new TableView(model);
		chainer.addChild(tableView);
		timed = new Timer(16, chainer);
		timed.addActionListener(model);
		timed.setActionCommand("update");
		Controller containers = new Controller(timed, model, mainView, tableView.getTableDataModel());
		
		mPanel.setPreferredSize(new Dimension(800,600));
		mPanel.setLayout(new BorderLayout());
		mPanel.add(containers.panel, BorderLayout.NORTH);
		mPanel.add(mainView, BorderLayout.CENTER);
		mPanel.setVisible(true);

		
		timed.start();
		//timed.schedule(new View(), 0, 16);
		
		getContentPane().add(mPanel);
		getContentPane().validate();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main (String [] x) {
		new BrownianWindow();
	}
}
