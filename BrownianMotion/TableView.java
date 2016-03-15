import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Skriver data om de 10 första partiklarna i en fin tabell.
 * @author holger
 *
 */
public class TableView extends JFrame implements ActionListener{
	
	private Model model;
	private DefaultTableModel dataModel;
	
	public TableView (Model m) {
		super("Tracked");
		model = m;
		
		// anonym klass som ser till så att booleans ritas som checkbox samt att datan uppdateras.
		dataModel = new DefaultTableModel() {
			@Override
			public Class getColumnClass(int columnIndex) {
				return getValueAt(0, columnIndex).getClass();
			}
			
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return columnIndex == 3;
			}
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Particle particle = model.getParticles().get(rowIndex);
				switch (columnIndex) {
				case 0:
					return rowIndex;
				case 1:
					return "" + particle.getX() + " " + particle.getY();
				case 2:
					return particle.isMoving ? "Moving" : "Not Moving";
				}
				return super.getValueAt(rowIndex, columnIndex);
			}
		};
		dataModel.setRowCount(0);
		dataModel.setColumnCount(4);
		for (int i = 0; i < 10; ++i) {
			Object[] rowData = {i, model.getParticles().get(i).getX(),
					model.getParticles().get(i).isMoving ? "moving" : "not moving", false};
			dataModel.addRow(rowData);
		}
		JTable table = new JTable(dataModel);
		add(table);
		setVisible(true);
		pack();
	}
	
	/**
	 * returnerar tabellens data model
	 * @return
	 */
	public DefaultTableModel getTableDataModel() {
		return dataModel;		
	}

	public void actionPerformed(ActionEvent event) {
		repaint(); // denna 'begär' en omritning, dvs paintComponent anropas med rätt Graphics.
	}
}
