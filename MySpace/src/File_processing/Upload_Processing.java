package File_processing;

/**
 * 
 * 這個副程式不完全正確(需要規劃完UML後再進行修正)
 * 這個物件主要功能是：是當檔案交給系統，最起始的判斷工作、呼叫上傳、寫入檔案資訊等，均由這支程式來呼叫與調配
 * 
 * */

import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.dom4j.DocumentException;

import Data.Obj_File;
import Data.Xml_files;
import Net.Mailer;
import System.System_parameters;
import UI.UI_drive;

public class Upload_Processing extends Thread {

	// 欲上船的檔案，需先轉換成自定義的Obj_file產生相關資料
	private Obj_File f;

	private int FileFlag;

	// obj_file 的儲存資料
	private String[][] store;
	// 紀錄本次上傳是否失敗
	public  boolean fail = false;
	// 檔案處理時產生的壓縮檔
	private File zipFile;
	// 檔案所在資料夾路徑
	private String in_dir_path;

	// 處理xml資料庫物件
	private Xml_files xf;

	// file 欲上的檔案 // in_path 檔案所在資料夾路徑
	public Upload_Processing(File file, String in_path)
			throws DocumentException, IOException {

		// 處理xml資料庫物件

		// 將檔案轉成自訂的 Obj_File 物件 (檔案,檔案所在資料夾路徑)
		this.f = new Obj_File(file.getAbsolutePath(), in_path);

		//空間足夠，才會執行
		if(f.is_enough){
			
			// 檔案儲存資料
			this.store = f.store;

			// 檔案所在資料夾位置
			in_dir_path = in_path;

			xf = new Xml_files();
			// 直接寫入資料庫
			xf.add_FileToXml(f);
			
			// 新增到 Map
			add_to_list();

			@SuppressWarnings("unchecked")
			ArrayList<Object> files = (ArrayList<Object>) UI_drive.map
					.get(in_dir_path);
			FileFlag = files.size() - 1;
			
			changeStatus(0);
		}
		else{
			
			JOptionPane.showMessageDialog(UI_drive.ui_dive, "您的儲存空間不足，可以新增更多Gmail帳號來擴充空間!!",
					"錯誤", JOptionPane.ERROR_MESSAGE);
			fail = true;
		}
			
		
		
	}

	public void run() {

		// 把狀態改成等待
	

		try {
			if (f.size > 0) {

				
				// 執行壓縮
				zipFile = dozip();
			}

		} catch (IOException e1) {
			JOptionPane.showMessageDialog(UI_drive.ui_dive, e1.getMessage(),
					"錯誤", JOptionPane.ERROR_MESSAGE);
			fail = true;
		}

		// 檔案大小大於0才傳
		if (f.size > 0) {
			if (zipFile.exists()) {
				// 切割數量要大於1才需要切
				if (f.sub_number > 1)
					try {
						// 需要切割+寄送
						Split_and_Send(zipFile);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(UI_drive.ui_dive,
								e.getMessage(), "上傳失敗",
								JOptionPane.ERROR_MESSAGE);
						fail = true;
					}
				else
					try {
						// 不須切割直接寄送
						Send(zipFile);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(UI_drive.ui_dive,
								e.getMessage(), "上傳失敗",
								JOptionPane.ERROR_MESSAGE);
						fail = true;
					}
			}
		} else
			JOptionPane.showMessageDialog(UI_drive.ui_dive, "無效的檔案，因為檔案大小為0k",
					"無效的檔案 !", JOptionPane.ERROR_MESSAGE);

		// 如果上傳的途中有任何失敗的話，馬上清除那筆檔案資料
		if (fail) {

			// delete_inList();

		} else {
			// System.out.println("刪除該壓縮垃圾檔案："+zipFile.delete());
		}

		zipFile.delete();

	}

	// 需要切割，切完後for迴圈寄送
	private void Split_and_Send(File zipfile) throws Exception {

		System.out.println("壓縮並切割的檔案為: " + zipfile);

		// 執行切割，並回傳切割後的檔案
		File[] subFile = Splitter.split(f, zipfile);

		// 切割完後，執行上傳
		changeStatus(1);

		// for迴圈寄送
		for (int i = 0; i < f.sub_number; i++) {
			// 填入寄送位智帳密，與List位置
			Mailer.do_send(store[i][1], store[i][2], subFile[i],
					store[i][0]);
		}

		// 把狀態改為成功
		changeStatus(2);

		// 5秒後 把成功的狀態消除
		Timer time = new Timer();
		time.schedule(new Set_Status(null, FileFlag, in_dir_path), 5000);

		// 刪除檔案處理過程中產生的切割檔
		for (File f : subFile)
			f.delete();

	}

	// 不需要切割，直接寄送
	private void Send(File zipfile) throws Exception {

		// 狀態改為上傳中
		changeStatus(1);

		// 開始寄送
		Mailer.do_send(store[0][1], store[0][2], zipfile, store[0][0]);
		
		// 狀態改為完成
		changeStatus(2);

		// 5秒後把成功的狀態消除
		Timer time = new Timer();
		time.schedule(new Set_Status(null, FileFlag, in_dir_path), 5000);

	}

	// 執行切割，回傳切割後的檔案陣列
	private synchronized File dozip() throws FileNotFoundException, IOException {

		// 執行壓縮，回傳壓縮檔名
		String zipFile_Name = Zipper.makeZip(f);
		// 重新導向檔案，回傳壓縮出來的新檔案
		return new File(zipFile_Name);
	}

	// 新增資料到map內
	private void add_to_list() {

		Object[] one_file = new Object[15];

		// 在 xml 內的index
		one_file[0] = xf.get_last_index();
		// id
		one_file[1] = f.id;
		// 檔案名稱
		one_file[2] = f.name;
		// 檔案大小
		one_file[3] = f.size;
		// 檔案類型
		one_file[4] = f.tpe;
		// 取得上傳日期
		one_file[5] = f.savedate;
		// 取得切割數量
		one_file[6] = f.sub_number;
		// 檔案儲存路徑
		one_file[7] = f.dir;
		// 取得檔案儲存位置(把List交給副程式處理)
		one_file[8] = f.store;
		// 使用頻率次數
		one_file[9] = f.freq;
		// 是否標星號
		one_file[10] = f.star;
		// 檔案最後被修改的時間(long)
		one_file[11] = f.modified;
		// 在MySpace中最後被使用的時間
		one_file[12] = f.used;
		// 設定檔案上傳狀態(由Mailer來控制即可)
		one_file[13] = null;
		// 設定Checkbox的初始值
		one_file[14] = false;

		// 遞迴的將檔案用ArraysList串起來，在用Map排入對應的路徑內
		if (UI_drive.map.get(in_dir_path) == null) {
			ArrayList<Object> t = new ArrayList<Object>();
			t.add(one_file);
			UI_drive.map.put(in_dir_path, t);

		} else {

			@SuppressWarnings("unchecked")
			ArrayList<Object> t = (ArrayList<Object>) UI_drive.map
					.get(in_dir_path);
			t.add(one_file);
		}

	}

	// 改變檔案目前狀態
	private void changeStatus(int i) {
		String s = null;

		switch (i) {
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
		}

		@SuppressWarnings("unchecked")
		ArrayList<Object> files = (ArrayList<Object>) UI_drive.map
				.get(in_dir_path);
		Object[] f = (Object[]) files.get(FileFlag);
		f[13] = new ImageIcon(s);

		// 如果檔案所在路徑 等於 目前路徑 則為使用者更新介面
		if (in_dir_path.equals(System_parameters.tree_path)) {
			UI_drive.bulid_tableData();
			UI_drive.Model_Drive.setDataVector(UI_drive.table_data,
					UI_drive.columnTitle);
			//UI_drive.table.setModel(UI_drive.Model_Drive);
			UI_drive.setTablefeeling();
		}

	}

}
