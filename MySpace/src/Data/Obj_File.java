package Data;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import org.dom4j.DocumentException;

import System.System_parameters;

public class Obj_File extends File {

	private static final long serialVersionUID = 1L;
	public String id;
	public String name;
	public long size;
	public ImageIcon type;
	public String savedate;
	public int sub_number;
	public String dir;
	public String[][] store;
	public ImageIcon status;
	public Boolean check;
	public String tpe;

	public int freq;
	public int star;
	public long modified;
	public long used;
	public boolean is_enough = false;
	public boolean is_google_reject;


	public Obj_File(String p, String path) throws DocumentException, IOException {

		super(p);

		SimpleDateFormat fm1 = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat fm2 = new SimpleDateFormat("yyyy/MM/dd");
		
		Date d = new Date();
		name = this.getName();
		id = name + "_" + fm1.format(d);
		size = this.length();
		tpe = getType();
		is_google_reject = is_google_reject(tpe);
		type = System_parameters.getFileIcon(tpe);

		Date now = new Date();
		savedate = fm2.format(now);
		sub_number = getSub_number();
		dir = path;
		status = null;
		check = false;
		
		freq = 0;
		star = 0;
		modified = this.lastModified();
		used = modified;
		
		// 假如空間不夠，就會回傳null is_enough 就會維持false
		store = buildStore();
		if(store != null) is_enough = true;
		
	}
	
	
	
	private String getType() {

		if (!this.isDirectory()) {
			String sp[] = this.getName().split("\\.");
			return sp[sp.length - 1];
		} else {
			return "dir";
		}
	}

	// 有足夠空間就建立儲存空間，沒有空間就回傳null
	private String[][] buildStore() throws DocumentException, IOException {
		String[][] store = new String[sub_number][3];
		Xml_act xa = new Xml_act();
		String[] at = xa.getaccount(this.length());

		if (at == null)
			return null;

		else {
			for (int i = 0; i < sub_number; i++) {
				store[i][0] = (sub_number > 1) ? "[MySpace]" + id + ".zip"
						+ (i + 1) + ".tmp" : "[MySpace]" + id + ".zip";
				store[i][1] = at[0];
				store[i][2] = at[1];
			}
			return store;
		}
	}

	// 取得切割數量，目前以1MB為單位
	private int getSub_number() {

		int subnum;

		double num = (double) size / (double) 1048576;
		if (num != 0) {

			int x = (int) num;
			if ((num - (double) x) > 0)
				subnum = x + 1;
			else
				subnum = x;

		} else
			subnum = 0;

		// 如果不用切割，又是Google拒絕的檔案類型，就切割成2個
		if (is_google_reject && subnum == 1) {
			subnum = 2;
		}

		return subnum;

	}

	private boolean is_google_reject(String t) {

		for (int i = 0; i < System_parameters.google_reject.length; i++) {
			if (t.equals(System_parameters.google_reject[i])) {
				return true;
			}
		}
		return false;

	}
}
