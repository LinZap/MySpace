package UI;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

public class Model_Act  extends DefaultTableModel {


	private static final long serialVersionUID = 1L;
	
	
	public Model_Act(Object[][] data, Object[] title) {
		super(data, title);	
	}
	
	
	public Class<?> getColumnClass(int column) {
		
		switch (column) {
		case 0:
			return ImageIcon.class;
		default:
			return String.class;
		}	
	}
	
	
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	
}
