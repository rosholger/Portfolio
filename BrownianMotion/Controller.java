import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Timer;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Controller implements ChangeListener, ActionListener, TableModelListener{
	
	JPanel panel = new JPanel();
	JPanel containerPanel = new JPanel();
	JSlider updateFrequencySlider;
	JSlider movementLengthSlider = new JSlider(JSlider.HORIZONTAL, 0, 40, (int)Particle.moveLength);
	JButton pauseButton = new JButton("Start");
	Timer timer;
	Model model;
	JFrame tableFrame;
	MainView view;
	DefaultTableModel tableState;
	
	Controller(Timer timer, Model m, MainView view, DefaultTableModel tableState) {
		this.tableState = tableState;
		tableState.addTableModelListener(this);
		this.timer = timer;
		model = m;
		this.view = view;
		updateFrequencySlider = new JSlider(JSlider.HORIZONTAL, 0, 40, timer.getDelay());
		
		// slider 1 
		slideSettings(updateFrequencySlider);
		JLabel sliderLabel = new JLabel("Milli-seconds", JLabel.CENTER);

		// slider 2
		slideSettings(movementLengthSlider);
		JLabel sliderLabel2 = new JLabel("Movement-length", JLabel.CENTER);

		containerPanel.setLayout(new GridLayout(2,3));
		containerPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		pauseButton.addActionListener(this);
		containerPanel.add(pauseButton);
		containerPanel.add(sliderLabel);
		containerPanel.add(sliderLabel2);
		containerPanel.add(new JLabel("", JLabel.CENTER));
		containerPanel.add(updateFrequencySlider);
		containerPanel.add(movementLengthSlider);
		containerPanel.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		
		panel.setLayout(new BorderLayout());
		panel.add(containerPanel);
		panel.setVisible(true);
		
	}
	
	private void slideSettings(JSlider slider){
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setLabelTable(slider.createStandardLabels(10));
		slider.addChangeListener(this);
	}
		
	

	public void stateChanged(ChangeEvent e) {
		if((JSlider)e.getSource() == updateFrequencySlider){
			timer.setDelay(updateFrequencySlider.getValue());
		}
		if((JSlider)e.getSource() == movementLengthSlider) {
			model.setL(movementLengthSlider.getValue());
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == pauseButton) {
			if (!model.getPaused()) {
				pauseButton.setText("Start");
			} else {
				pauseButton.setText("Pause");
			}
			model.setPaused(!model.getPaused());
		}
	}

	public void tableChanged(TableModelEvent e) {
		// om tabbellen ändrats kollar vi om den cell som ändrats e "satt" eller inte.
		Boolean checkmarkState = (Boolean)tableState.getValueAt(e.getFirstRow(), e.getColumn());
		if (!checkmarkState) {
			view.removeTrackedParticle(e.getFirstRow());
		} else {
			view.addTrackedParticle(e.getFirstRow());
		}
	}
}
