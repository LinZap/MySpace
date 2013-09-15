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
	
	// �R�A�� �b����� �P �w�ϥΪŶ�
	private Document d;
	private SAXReader reader;
	private static File f;
	private static Element root;

	// �򥻰ѼƳ]�w
	public Xml_act() throws DocumentException {
		f = new File(System_parameters.get_db_path(2));
		reader = new SAXReader();
		d = reader.read(f);
		root = d.getRootElement();
	}

	// �ǥѥثe�����Ш��o�@���b�K
	@SuppressWarnings("unchecked")
	public String[] getaccount(long size) throws DocumentException, IOException {
		List<Element> e = new ArrayList<Element>();
		List<Element> p = new ArrayList<Element>();
		e = root.elements();
		boolean ok = false;
		//�নKB
		double f_size = (double)size/(double)1024;
		for(int i=0;i<e.size();i++){
			ok = false;
			String s = e.get(i).attribute("space").getText();
			double used =Double.parseDouble(s);
			
			// �[�J�Ŷ�
			used+=f_size;
			
			//���p�S�W�L�W���A�h�g�J�A�ø��X�j��
			if(used <= System_parameters.limit){
				p = e.get(i).elements();
				e.get(i).attribute("space").setText(String.valueOf(used));
				save_xml();
				ok = true;
				UI_drive.used_space_label.setText(" �w�g�ϥ�"+Math.round(used) +"KB  ");
				break;				
			}
		}
		
		// ���p���������e�q�x�s�A�h�^�Ǹӱb���K�X
		// ���p�����A�h�^��null
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
		//�নKB
		
		for(int i=0;i<e.size();i++){
		
			String s = e.get(i).attribute("space").getText();
			double used =Double.parseDouble(s);
			sum+=used;
			}
		
		UI_drive.used_space_label.setText(" �w�g�ϥ�"+ sum +" KB  ");		
	}
	// �s�W�@���b���K�X
	public void add_account(String act, String psd) throws IOException,
			DocumentException {
		Element account = root.addElement("account");
		account.addAttribute("space", "0");
		Element a = account.addElement("act");
		Element p = account.addElement("psd");
		a.setText(act);
		p.setText(psd);
		save_xml();
		
		UI_drive.used_space_label2.setText("�ثe�Ŷ��G "+ get_act_count()*10 + "GB  ");
		
	}

	// �����b��ثe�ƶq
	@SuppressWarnings("unchecked")
	public int get_act_count(){
		List<Element> e = new ArrayList<Element>();
		e = root.elements();
		return  e.size();
	}
	
	// ���ѵ�JTable���ƾ�Ū��
	// �|�^�Ǥ@��2���}�C���ƾڵ����ϥ�
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
	
	//�ϥαb���ӷj�M�K�X
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
	
	// �O�s�ק�xml�ɮ�
	public void save_xml() throws IOException {
		XMLWriter writer = new XMLWriter(new FileOutputStream(f),
				OutputFormat.createPrettyPrint());
		writer.write(d);
		writer.close();
	}
}