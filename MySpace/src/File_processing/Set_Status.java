package File_processing;

import java.util.ArrayList;
import java.util.TimerTask;

import System.System_parameters;
import UI.UI_drive;

public class Set_Status extends TimerTask {
	private int fg = 0;
	private String in_flag_path;
	public Set_Status(Object c, int fg , String in_flag_path) {
		this.fg = fg;
		this.in_flag_path = in_flag_path;
	}

	public void run() {	
		
		@SuppressWarnings("unchecked")
		Object[] f =  (Object[]) ((ArrayList<Object>) UI_drive.map.get(in_flag_path)).get(fg);
		f[13] = null;
		
		//如果檔案所在路徑 等於 目前路徑 則為使用者更新介面
		if(in_flag_path.equals(System_parameters.tree_path))
		{
			
			UI_drive.Model_Drive.setValueAt(null, fg , 4);	
			//UI_drive.bulid_tableData();
			//UI_drive.Model_Drive.setDataVector(UI_drive.table_data,UI_drive.columnTitle);
			//UI_drive.table.setModel(UI_drive.Model_Drive);
			//UI_drive.setTablefeeling();	
		}
	}
}
