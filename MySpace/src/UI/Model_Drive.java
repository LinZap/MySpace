package UI;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/*
 * 
 * 雲端檔案介面的Table Model，這個物件主要設計該Table的顯示外觀 
 **/
public class Model_Drive extends DefaultTableModel {

	
	private static final long serialVersionUID = 1L;

	public Model_Drive(Object[][] data, Object[] title) {
		super(data, title);	
	}

		
	// columnTitle = { "選擇0","圖示1","圖示2","標題3", "狀態4(屬於圖)", "上傳日期4" };
	
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
