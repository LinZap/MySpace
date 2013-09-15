package Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import org.dom4j.*;
import org.dom4j.io.*;
import System.System_parameters;
import UI.UI_drive;

public class Xml_act {
	
	// 靜態的 帳戶指標 與 已使用空間
	private Document d;
	private SAXReader reader;
	private static File f;
	private static Element root;

	// 基本參數設定
	public Xml_act() throws DocumentException {
		f = new File(System_parameters.get_db_path(2));
		reader = new SAXReader();
		d = reader.read(f);
		root = d.getRootElement();
	}

	// 藉由目前的指標取得一隻帳密
	@SuppressWarnings("unchecked")
	public String[] getaccount(long size) throws DocumentException, IOException {
		List<Element> e = new ArrayList<Element>();
		List<Element> p = new ArrayList<Element>();
		e = root.elements();
		boolean ok = false;
		//轉成KB
		double f_size = (double)size/(double)1024;
		for(int i=0;i<e.size();i++){
			ok = false;
			String s = e.get(i).attribute("space").getText();
			double used =Double.parseDouble(s);
			
			// 加入空間
			used+=f_size;
			
			//假如沒超過上限，則寫入，並跳出迴圈
			if(used <= System_parameters.limit){
				p = e.get(i).elements();
				e.get(i).attribute("space").setText(String.valueOf(used));
				save_xml();
				ok = true;
				UI_drive.used_space_label.setText(" 已經使用"+Math.round(used) +"KB  ");
				break;				
			}
		}
		
		// 假如有足夠的容量儲存，則回傳該帳號密碼
		// 假如不夠，則回傳null
		if(ok){
			
			String act[] = { p.get(0).getText().toString(),
					p.get(1).getText().toString() };
			return act;
		}
		else return null;
	}
	
	@SuppressWarnings("unchecked")
	public void setsp(){
		List<Element> e = new ArrayList<Element>();
		e = root.elements();
		int sum =0;
		//轉成KB
		
		for(int i=0;i<e.size();i++){
		
			String s = e.get(i).attribute("space").getText();
			double used =Double.parseDouble(s);
			sum+=used;
			}
		
		UI_drive.used_space_label.setText(" 已經使用"+ sum +" KB  ");		
	}
	// 新增一隻帳號密碼
	public void add_account(String act, String psd) throws IOException,
			DocumentException {
		Element account = root.addElement("account");
		account.addAttribute("space", "0");
		Element a = account.addElement("act");
		Element p = account.addElement("psd");
		a.setText(act);
		p.setText(psd);
		save_xml();
		
		UI_drive.used_space_label2.setText("目前空間： "+ get_act_count()*10 + "GB  ");
		
	}

	// 取的帳戶目前數量
	@SuppressWarnings("unchecked")
	public int get_act_count(){
		List<Element> e = new ArrayList<Element>();
		e = root.elements();
		return  e.size();
	}
	
	// 提供給JTable的數據讀取
	// 會回傳一個2維陣列的數據給表格使用
	@SuppressWarnings("unchecked")
	public Object[][] read_account() throws DocumentException {
		Object[][] act_data = null;
		List<Element> e = new ArrayList<Element>();
		List<Element> p = new ArrayList<Element>();
		
		if (f.exists()) {
			e = root.elements();
			act_data = new Object[e.size()][3];
			for (int i = 0; i < e.size(); i++) {
				
				act_data[i][0] = i==0 ? new ImageIcon("image\\gar.fw.png") : null ;
						
				p = e.get(i).elements();
				act_data[i][1] = p.get(0).getText();
				act_data[i][2] = p.get(1).getText();
			}
		}
		return act_data;
	}
	
	//使用帳號來搜尋密碼
	@SuppressWarnings("unchecked")
	public String getPsd(String act) {
		
		List<Element> e = new ArrayList<Element>();
		List<Element> p = new ArrayList<Element>();	
		e = root.elements();
		
		for (int i = 0; i < e.size(); i++) {
			
			p = e.get(i).elements();
			if(p.get(0).getText().equals(act))
				return p.get(1).getText();
		}
		return null;
	}
	
	// 保存修改xml檔案
	public void save_xml() throws IOException {
		XMLWriter writer = new XMLWriter(new FileOutputStream(f),
				OutputFormat.createPrettyPrint());
		writer.write(d);
		writer.close();
	}
}