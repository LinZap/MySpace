package UI;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/*
 * 
 * �����ɮפ�����Table Model�A�o�Ӫ���D�n�]�p��Table����ܥ~�[ 
 **/
public class Model_Drive extends DefaultTableModel {

	
	private static final long serialVersionUID = 1L;

	public Model_Drive(Object[][] data, Object[] title) {
		super(data, title);	
	}

		
	// columnTitle = { "���0","�ϥ�1","�ϥ�2","���D3", "���A4(�ݩ��)", "�W�Ǥ��4" };
	
	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return Boolean.class;
		case 1:
			return ImageIcon.class;
		case 2:
			return ImageIcon.class;
		case 3:
			return String.class;
		case 4:
			return ImageIcon.class;
		default:
			return String.class;
		}
	}
	
	
	public void set_notEdit(int row){
		
		
	}

	public boolean isCellEditable(int row, int column) {
		if (column == 0)
			return true;
		return false;
	}

	

}
