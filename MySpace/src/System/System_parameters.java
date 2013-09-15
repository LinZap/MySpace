package System;

import javax.swing.ImageIcon;

public class System_parameters {

	// 系統root帳號
	public static String root_act = "";

	// 系統root密碼
	public static String root_psd = "";

	// 系統正在使用的帳號
	public static String useing_act = "";

	// 系統正在使用的密碼
	public static String useing_psd = "";

	
	
	
	// 系統使用空間上限 (上限9.5G) (單位KB)
	public static double limit = 3072;

	// 系統專用資料夾路徑
	public static String system_dir_path = "C:\\MySpace";

	// 系統小ICON 圖路徑
	public static String system_icon_path = "image\\cat.ico";

	// 系統標題文字
	public static String system_title = "MySpace";

	// 3個資料庫路徑(搜尋subject用)，因為如果加上帳號搜尋不到
	public static String[] db_search = { "[Myspace]files.xml",
			"[Myspace]dir.xml", "[Myspace]act.xml" };

	// 取得資料庫路徑
	public static String get_db_path(int i) {
		return "Database\\" + root_act + "_" + db_search[i];
	}
	

	// 資料庫最後被修改的時間，用來進行版本比對
	public static long[] db_modify = { 0, 0, 0 };

	// 資料庫數量
	public static int get_db_count() {
		return db_search.length;
	}

	// 3個資料庫名稱
	public static String get_db_name(int i) {
		return root_act + "_" + db_search[i];
	}

	// 系統目前所指向的分類資料夾路徑
	public static String tree_path = "[MySpace]";

	// google 拒絕的檔案類型
	public static String[] google_reject = { "ade", "adp", "bat", "chm", "cmd",
			"com", "cpl", "exe", "hta", "ins", "isp", "jse", "lib", "mde",
			"msc", "msp", "mst", "pif", "scr", "sct", "shb", "sys", "vb",
			"vbe", "vbs", "vxd", "wsc", "wsf", "wsh" };

	public static ImageIcon getFileIcon(String tpe2) {

		ImageIcon ico = null;

		switch (tpe2) {

		// 屬於資料夾 0
		case "dir":
			ico = new ImageIcon("type\\dir.fw.png");
			break;

		// 屬於壓縮類 1
		case "zip":
		case "7z":
		case "rar":
		case "tar":
		case "tgz":
		case "gz":
			ico = new ImageIcon("type\\rar.fw.png");
			break;

		// 屬於視訊類 2
		case "mp4":
		case "avi":
		case "mpeg":
		case "wmv":
		case "rm":
		case "rmvb":
		case "mov":
		case "asf":
			ico = new ImageIcon("type\\video.fw.png");
			break;

		// 屬於音訊類 3
		case "wav":
		case "mp3":
		case "wma":
		case "ogg":
			ico = new ImageIcon("type\\music.fw.png");
			break;

		// 屬於圖片類 4
		case "bmp":
		case "jepg":
		case "gif":
		case "jpg":
		case "pcx":
		case "png":
			ico = new ImageIcon("type\\jpg.fw.png");
			break;

		// 屬於純文字類 5
		case "txt":
		case "rtf":
			ico = new ImageIcon("type\\txt.fw.png");
			break;

		// 屬於 Word 類 6
		case "doc":
		case "docx":
			ico = new ImageIcon("type\\doc.fw.png");
			break;

		// 屬於 excel 類 7
		case "xls":
		case "xlsx":
			ico = new ImageIcon("type\\xls.fw.png");
			break;

		// 屬於 ppt 類 8
		case "ppt":
		case "pptx":
			ico = new ImageIcon("type\\ppt.fw.png");
			break;

		// 屬於其他類
		default:
			ico = new ImageIcon("type\\files.fw.png");

		}

		return ico;
	}

}
