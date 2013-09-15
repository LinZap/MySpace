package File_processing;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import javax.mail.MessagingException;
import javax.swing.ImageIcon;
import Net.IMAP;
import System.System_parameters;
import UI.UI_drive;

public class Download_Processing extends Thread {
	//flag 在 arraylist 的位置
	private int flag, subnum;
	// 遠端的檔名，儲存的帳號，儲存的密碼，原始檔名
	private String act, psd, id;
	private String[] store_name;
	private Object[][] store;
	private String in_flag_path;
	
	// 檔案存檔目標，使用setTarget來改變，預設 C:\\MySpace
	private String target = "C:\\MySpace";

	//傳入在arraylist的位置 與 從哪個hashMap取出的路徑
	public Download_Processing(int index,String path) {
		
		flag = index;
		in_flag_path = path;
		
	}
	
	// 設定該檔案會儲存在哪個目錄底下
	public void setTarget(File f){
		target = f.getAbsolutePath();
	}
	
	public void run() {

		
		changeStatus(4);
		@SuppressWarnings("unchecked")
		ArrayList<Object> files = (ArrayList<Object>) UI_drive.map.get(in_flag_path);
		Object[] file = (Object[]) files.get(flag);
		store = (Object[][]) file[8];
		id = file[1].toString();
		subnum = Integer.parseInt(file[6].toString());
		store_name = new String[subnum];
		
		
		for (int i = 0; i < subnum; i++) {

			store_name[i] = store[i][0].toString();
			act = store[i][1].toString();
			psd = store[i][2].toString();

			// 執行收信
			try {
				IMAP imap = new IMAP(act, psd, store_name[i]);
				imap.start();
				imap.join();
			} catch (MessagingException | InterruptedException e) {
				e.printStackTrace();
			}

		}

		File zipfile;

		// 檔案後續處理
		if (subnum <= 1) {
			zipfile = new File(store_name[0]);
			// 沒有被切割，執行解壓縮
			try {
				unZipFile(zipfile.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			// 有被切割，執行合併
			System.out.println("檔案開始合併");
			zipfile = MergeFile();
			System.out.println("合併完成");
			System.out.println("檔案開始解壓縮");

			try {
				unZipFile(zipfile.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("解壓縮完成");

		}

		zipfile.delete();

		// 完成所有處理，介面通知使用者
		
		changeStatus(2);
		Timer time = new Timer();
		time.schedule(new Set_Status(null, flag, in_flag_path), 5000);

	}

	// 合併檔案
	private File MergeFile() {
		
		return Merge.File_Merge(id, store_name);
	}

	// 解壓縮檔案
	private void unZipFile(String zipPath) throws Exception {

		//解壓縮檔案 (檔案路徑,解壓縮到哪個目錄底下)
		
		UnZip.unZip(zipPath, target);

	}

	// 改變檔案目前狀態
	private void changeStatus(int arg) {
	
		
		String s = null;
		
		switch(arg){
		case 0:
			s = "image\\waiting.fw.png";
			break;
		case 1:
			s = "image\\uploading.fw.png";
			break;
		case 2:
			s = "image\\success.fw.png";
			break;
		case 3:
			s = "image\\fail.fw.png";
			break;
		case 4:
			s = "image\\downloading.fw.png";
			break;
		}

		@SuppressWarnings("unchecked")
		Object[] f =  (Object[]) ((ArrayList<Object>) UI_drive.map.get(in_flag_path)).get(flag);
		f[13] = new ImageIcon(s);
		

		if(in_flag_path.equals(System_parameters.tree_path))
		{
			//UI_drive.bulid_tableData();	
			UI_drive.Model_Drive.setValueAt(new ImageIcon(s), flag, 4);	
			//UI_drive.Model_Drive.setDataVector(UI_drive.table_data,UI_drive.columnTitle);
			//UI_drive.table.setModel(UI_drive.Model_Drive);
			//UI_drive.setTablefeeling();	
		}
		
	}

}
