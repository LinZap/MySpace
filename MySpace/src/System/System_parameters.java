package System;

import javax.swing.ImageIcon;

public class System_parameters {

	// �t��root�b��
	public static String root_act = "";

	// �t��root�K�X
	public static String root_psd = "";

	// �t�Υ��b�ϥΪ��b��
	public static String useing_act = "";

	// �t�Υ��b�ϥΪ��K�X
	public static String useing_psd = "";

	
	
	
	// �t�ΨϥΪŶ��W�� (�W��9.5G) (���KB)
	public static double limit = 3072;

	// �t�αM�θ�Ƨ����|
	public static String system_dir_path = "C:\\MySpace";

	// �t�ΤpICON �ϸ��|
	public static String system_icon_path = "image\\cat.ico";

	// �t�μ��D��r
	public static String system_title = "MySpace";

	// 3�Ӹ�Ʈw���|(�j�Msubject��)�A�]���p�G�[�W�b���j�M����
	public static String[] db_search = { "[Myspace]files.xml",
			"[Myspace]dir.xml", "[Myspace]act.xml" };

	// ���o��Ʈw���|
	public static String get_db_path(int i) {
		return "Database\\" + root_act + "_" + db_search[i];
	}
	

	// ��Ʈw�̫�Q�ק諸�ɶ��A�ΨӶi�檩�����
	public static long[] db_modify = { 0, 0, 0 };

	// ��Ʈw�ƶq
	public static int get_db_count() {
		return db_search.length;
	}

	// 3�Ӹ�Ʈw�W��
	public static String get_db_name(int i) {
		return root_act + "_" + db_search[i];
	}

	// �t�Υثe�ҫ��V��������Ƨ����|
	public static String tree_path = "[MySpace]";

	// google �ڵ����ɮ�����
	public static String[] google_reject = { "ade", "adp", "bat", "chm", "cmd",
			"com", "cpl", "exe", "hta", "ins", "isp", "jse", "lib", "mde",
			"msc", "msp", "mst", "pif", "scr", "sct", "shb", "sys", "vb",
			"vbe", "vbs", "vxd", "wsc", "wsf", "wsh" };

	public static ImageIcon getFileIcon(String tpe2) {

		ImageIcon ico = null;

		switch (tpe2) {

		// �ݩ��Ƨ� 0
		case "dir":
			ico = new ImageIcon("type\\dir.fw.png");
			break;

		// �ݩ����Y�� 1
		case "zip":
		case "7z":
		case "rar":
		case "tar":
		case "tgz":
		case "gz":
			ico = new ImageIcon("type\\rar.fw.png");
			break;

		// �ݩ���T�� 2
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

		// �ݩ󭵰T�� 3
		case "wav":
		case "mp3":
		case "wma":
		case "ogg":
			ico = new ImageIcon("type\\music.fw.png");
			break;

		// �ݩ�Ϥ��� 4
		case "bmp":
		case "jepg":
		case "gif":
		case "jpg":
		case "pcx":
		case "png":
			ico = new ImageIcon("type\\jpg.fw.png");
			break;

		// �ݩ�¤�r�� 5
		case "txt":
		case "rtf":
			ico = new ImageIcon("type\\txt.fw.png");
			break;

		// �ݩ� Word �� 6
		case "doc":
		case "docx":
			ico = new ImageIcon("type\\doc.fw.png");
			break;

		// �ݩ� excel �� 7
		case "xls":
		case "xlsx":
			ico = new ImageIcon("type\\xls.fw.png");
			break;

		// �ݩ� ppt �� 8
		case "ppt":
		case "pptx":
			ico = new ImageIcon("type\\ppt.fw.png");
			break;

		// �ݩ��L��
		default:
			ico = new ImageIcon("type\\files.fw.png");

		}

		return ico;
	}

}
