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
				// ���o id
				if(xml_id.equals(id)){idx=i;break;}	
			}	
		}
		
		
		if(idx!=-1){
			e.remove(idx);
			save_xml();
			System.out.println("�ɮױqXML������");
			return true;
		}
		else{
			System.out.println("�ɮק䤣��");
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
				// ���o id
				String xml_id = ((Element) e.get(i).elements().get(0)).getText();
				//�p�G���N��W��
				if(xml_id.equals(id)){
					Element ele_name =  (Element) e.get(i).elements().get(1);
					ele_name.setText(new_name);
					return true;
				}	
				
				
			}	
		}
		
		
		return false;
		
	}
	

	// �g�J�@���ɮ�
	public void add_Receive_FileToXml(String name,String size,String id,String sub,String[] store,String type,String savedate,String mod,String use
			,String at_act,String at_psd) throws IOException, DocumentException {

		// �إ�xml�ɮת�����
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

		

		// �]�w�������x�s����
		Eid.setText(id);
		Ename.setText(name);
		Esize.setText(size);
		Etype.setText(type);
		Esavedate.setText(savedate);
		EsubNo.setText(sub);
		Edir.setText("[MySpace, �ɮױ����d]");
		
		freq.setText("0");
		star.setText("0");
		modified.setText(mod);
		used.setText(use);
		
		
		
		// �]�w�x�s��m��������
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
	
	

	// �g�J�@���ɮ�
	public void add_FileToXml(Obj_File f) throws IOException, DocumentException {

		// �إ�xml�ɮת�����
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

		

		// �]�w�������x�s����
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
		
		
		
		// �]�w�x�s��m��������
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
	
	//�^�ǥثe���ӼơA�ɮ׼g�J��|�X�{�b�̫�@�ӡA�ӼƧY���ɮצbxml�̪�����
	public int get_last_index(){
		return root.elements().size();
	}
	
	
	
	
	// �qfile.xml����Ū�iJava�̡A�إ��ɮת���
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> bulid_files() throws DocumentException {

		HashMap<String, Object> map = new HashMap<String, Object>();
		
		List<Element> e = new ArrayList<Element>();
		List<Element> p = new ArrayList<Element>();

		if (f.exists()) {

			e = root.elements();
			sumFile = e.size();

			for (int i = 0; i < sumFile; i++) {
				// �Ыطs���x�s�Ŷ�
				Object[] one_file = new Object[15];

				p = e.get(i).elements();
				
				// �b xml ����index
				one_file[0] = i;						
				// id
				one_file[1] = p.get(0).getText();
				// �ɮצW��
				one_file[2] = p.get(1).getText();
				// �ɮפj�p
				one_file[3] = p.get(2).getText();
				// �ɮ�����
				one_file[4] = p.get(3).getText();
				// ���o�W�Ǥ��
				one_file[5] = p.get(4).getText();
				// ���o���μƶq
				one_file[6] = p.get(5).getText();
				// �ɮ��x�s���|
				one_file[7] = p.get(6).getText();
				// ���o�ɮ��x�s��m(��List�浹�Ƶ{���B�z)
				one_file[8] = getStore(p.get(7).elements());		
				// �ϥ��W�v����
				one_file[9] = p.get(8).getText();
				// �O�_�ЬP��
				one_file[10] = p.get(9).getText();	
				// �ɮ׳̫�Q�ק諸�ɶ�(long)
				one_file[11] = p.get(10).getText();	
				// �bMySpace���̫�Q�ϥΪ��ɶ�
				one_file[12] = p.get(11).getText();
				// �]�w�ɮפW�Ǫ��A(��Mailer�ӱ���Y�i)
				one_file[13] = null;
				// �]�wCheckbox����l��
				one_file[14] = false;

				// ���j���N�ɮץ�ArraysList��_�ӡA�b��Map�ƤJ���������|��
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
	// ����store�����̭����ȡA�æ^��Object����
	private Object[][] getStore(List<Element> obj_e) {
		// �s�W�@�Ӫ���}�C�s��
		Object[][] store = new Object[obj_e.size()][3];
		// ���o�ɮת��C�@���x�s��T
		for (int i = 0; i < obj_e.size(); i++) {

			List<Element> data = new ArrayList<Element>();
			// ���o<sub_file>�����Ҧ�����
			data = obj_e.get(i).elements();
			// ���o�u���x�s�ɦW
			store[i][0] = data.get(0).getText();
			// ���o�x�s���b��
			store[i][1] = data.get(1).getText();
			// ���o�x�s���K�X
			store[i][2] = data.get(2).getText();
		}
		return store;
	}

	
	
	
	
	
	// �x�s
	public void save_xml() throws IOException {
		XMLWriter writer = new XMLWriter(new FileOutputStream(f),
				OutputFormat.createPrettyPrint());
		writer.write(d);
		writer.close();
	}




}
