package Data;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.*;
import UI.UI_drive;
import System.System_parameters;

public class Xml_files {

	private Document d;
	private SAXReader reader = new SAXReader();
	private File f;
	private Element root;
	private int sumFile = 0;
	//private Map<String, Object> map ;

	public Xml_files() throws DocumentException {

		f = new File(System_parameters.get_db_path(0));
		d = reader.read(f);
		root = d.getRootElement();
	}
	
	
	public void delete_FileToList(int Flag,String inpath ) {
		@SuppressWarnings("unchecked")
		ArrayList<Object>files = (ArrayList<Object>) UI_drive.map.get(inpath);
		files.remove(Flag);
	}

	public void rename_FileToList(int Flag,String new_name) {
	
		@SuppressWarnings("unchecked")
		ArrayList<Object>files = (ArrayList<Object>) UI_drive.map.get(System_parameters.tree_path);
		Object[] obj = (Object[]) files.get(Flag);
		obj[1] = new_name;
	}
	
	
	public void delete_FileToTable(int Flag,String in_flag_path) {
		if(in_flag_path.equals(System_parameters.tree_path))
		UI_drive.Model_Drive.removeRow(Flag);
	}
	
	
	public void rename_FileToTable(int Flag,String new_name) {
		UI_drive.Model_Drive.setValueAt(new_name, Flag, 2);
	}
	
	
	

	@SuppressWarnings("unchecked")
	public boolean delete_FileToXml(String id) throws IOException{
		
		List<Element> e = new ArrayList<Element>();
		int idx=-1; 
		
		if (f.exists()) {
			e = root.elements();
			sumFile = e.size();
			for (int i = 0; i < sumFile; i++) {
				String xml_id = ((Element) e.get(i).elements().get(0)).getText();
				// 取得 id
				if(xml_id.equals(id)){idx=i;break;}	
			}	
		}
		
		
		if(idx!=-1){
			e.remove(idx);
			save_xml();
			System.out.println("檔案從XML中移除");
			return true;
		}
		else{
			System.out.println("檔案找不到");
			return false;
		}
		
		
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean rename_FileToXml(String id,String new_name) throws IOException{
		
		List<Element> e = new ArrayList<Element>(); 
		
		if (f.exists()) {
			
			e = root.elements();
			sumFile = e.size();
			for (int i = 0; i < sumFile; i++) {
				// 取得 id
				String xml_id = ((Element) e.get(i).elements().get(0)).getText();
				//如果找到就改名稱
				if(xml_id.equals(id)){
					Element ele_name =  (Element) e.get(i).elements().get(1);
					ele_name.setText(new_name);
					return true;
				}	
				
				
			}	
		}
		
		
		return false;
		
	}
	

	// 寫入一個檔案
	public void add_Receive_FileToXml(String name,String size,String id,String sub,String[] store,String type,String savedate,String mod,String use
			,String at_act,String at_psd) throws IOException, DocumentException {

		// 建立xml檔案的元素
		Element Efile = root.addElement("file");
		Element Eid = Efile.addElement("id");
		Element Ename = Efile.addElement("name");
		Element Esize = Efile.addElement("size");
		Element Etype = Efile.addElement("type");
		Element Esavedate = Efile.addElement("savedate");
		Element EsubNo = Efile.addElement("sub_number");
		Element Edir = Efile.addElement("dir");
		Element Estore = Efile.addElement("store");
		
		Element freq = Efile.addElement("freq");
		Element star = Efile.addElement("star");
		Element modified = Efile.addElement("modified");
		Element used = Efile.addElement("used");

		

		// 設定元素該儲存的值
		Eid.setText(id);
		Ename.setText(name);
		Esize.setText(size);
		Etype.setText(type);
		Esavedate.setText(savedate);
		EsubNo.setText(sub);
		Edir.setText("[MySpace, 檔案接收櫃]");
		
		freq.setText("0");
		star.setText("0");
		modified.setText(mod);
		used.setText(use);
		
		
		
		// 設定儲存位置元素的值
		for (int i = 0; i < store.length; i++) {
			Element Esub_file = Estore.addElement("sub_file");
			Element Esname = Esub_file.addElement("sname");
			Element Esact = Esub_file.addElement("sact");
			Element Espsd = Esub_file.addElement("spsd");
			Esname.setText(store[i]);
			Esact.setText(at_act);
			Espsd.setText(at_psd);
		}
		
		
		save_xml();
	}
	
	

	// 寫入一個檔案
	public void add_FileToXml(Obj_File f) throws IOException, DocumentException {

		// 建立xml檔案的元素
		Element Efile = root.addElement("file");
		Element Eid = Efile.addElement("id");
		Element Ename = Efile.addElement("name");
		Element Esize = Efile.addElement("size");
		Element Etype = Efile.addElement("type");
		Element Esavedate = Efile.addElement("savedate");
		Element EsubNo = Efile.addElement("sub_number");
		Element Edir = Efile.addElement("dir");
		Element Estore = Efile.addElement("store");
		
		Element freq = Efile.addElement("freq");
		Element star = Efile.addElement("star");
		Element modified = Efile.addElement("modified");
		Element used = Efile.addElement("used");

		

		// 設定元素該儲存的值
		Eid.setText(f.id);
		Ename.setText(f.name);
		Esize.setText(String.valueOf(f.size));
		Etype.setText(f.tpe);
		Esavedate.setText(f.savedate);
		EsubNo.setText(String.valueOf(f.sub_number));
		Edir.setText(f.dir);
		
		freq.setText(String.valueOf(f.freq));
		star.setText(String.valueOf(f.star));
		modified.setText(String.valueOf(f.modified));
		used.setText(String.valueOf(f.used));
		
		
		
		// 設定儲存位置元素的值
		for (int i = 0; i < f.sub_number; i++) {
			Element Esub_file = Estore.addElement("sub_file");
			Element Esname = Esub_file.addElement("sname");
			Element Esact = Esub_file.addElement("sact");
			Element Espsd = Esub_file.addElement("spsd");
			Esname.setText(f.store[i][0]);
			Esact.setText(f.store[i][1]);
			Espsd.setText(f.store[i][2]);
		}
		
		
		save_xml();
	}
	
	//回傳目前的個數，檔案寫入後會出現在最後一個，個數即為檔案在xml裡的索引
	public int get_last_index(){
		return root.elements().size();
	}
	
	
	
	
	// 從file.xml把資料讀進Java裡，建立檔案物件
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> bulid_files() throws DocumentException {

		HashMap<String, Object> map = new HashMap<String, Object>();
		
		List<Element> e = new ArrayList<Element>();
		List<Element> p = new ArrayList<Element>();

		if (f.exists()) {

			e = root.elements();
			sumFile = e.size();

			for (int i = 0; i < sumFile; i++) {
				// 創建新的儲存空間
				Object[] one_file = new Object[15];

				p = e.get(i).elements();
				
				// 在 xml 內的index
				one_file[0] = i;						
				// id
				one_file[1] = p.get(0).getText();
				// 檔案名稱
				one_file[2] = p.get(1).getText();
				// 檔案大小
				one_file[3] = p.get(2).getText();
				// 檔案類型
				one_file[4] = p.get(3).getText();
				// 取得上傳日期
				one_file[5] = p.get(4).getText();
				// 取得切割數量
				one_file[6] = p.get(5).getText();
				// 檔案儲存路徑
				one_file[7] = p.get(6).getText();
				// 取得檔案儲存位置(把List交給副程式處理)
				one_file[8] = getStore(p.get(7).elements());		
				// 使用頻率次數
				one_file[9] = p.get(8).getText();
				// 是否標星號
				one_file[10] = p.get(9).getText();	
				// 檔案最後被修改的時間(long)
				one_file[11] = p.get(10).getText();	
				// 在MySpace中最後被使用的時間
				one_file[12] = p.get(11).getText();
				// 設定檔案上傳狀態(由Mailer來控制即可)
				one_file[13] = null;
				// 設定Checkbox的初始值
				one_file[14] = false;

				// 遞迴的將檔案用ArraysList串起來，在用Map排入對應的路徑內
				if (map.get(one_file[7].toString()) == null) {
					ArrayList<Object> t = new ArrayList<Object>();
					t.add(one_file);
					map.put(one_file[7].toString(), t);

				} else {
					ArrayList<Object> t = (ArrayList<Object>) map
							.get(one_file[7].toString());
					t.add(one_file);
				}
			}
		}
		return map;
	}
	
	
	

	@SuppressWarnings("unchecked")
	// 分解store元素裡面的值，並回傳Object物件
	private Object[][] getStore(List<Element> obj_e) {
		// 新增一個物件陣列存放
		Object[][] store = new Object[obj_e.size()][3];
		// 取得檔案的每一個儲存資訊
		for (int i = 0; i < obj_e.size(); i++) {

			List<Element> data = new ArrayList<Element>();
			// 取得<sub_file>內的所有元素
			data = obj_e.get(i).elements();
			// 取得真實儲存檔名
			store[i][0] = data.get(0).getText();
			// 取得儲存的帳號
			store[i][1] = data.get(1).getText();
			// 取得儲存的密碼
			store[i][2] = data.get(2).getText();
		}
		return store;
	}

	
	
	
	
	
	// 儲存
	public void save_xml() throws IOException {
		XMLWriter writer = new XMLWriter(new FileOutputStream(f),
				OutputFormat.createPrettyPrint());
		writer.write(d);
		writer.close();
	}




}
