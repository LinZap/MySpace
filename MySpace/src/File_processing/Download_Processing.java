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
	//flag �b arraylist ����m
	private int flag, subnum;
	// ���ݪ��ɦW�A�x�s���b���A�x�s���K�X�A��l�ɦW
	private String act, psd, id;
	private String[] store_name;
	private Object[][] store;
	private String in_flag_path;
	
	// �ɮצs�ɥؼСA�ϥ�setTarget�ӧ��ܡA�w�] C:\\MySpace
	private String target = "C:\\MySpace";

	//�ǤJ�barraylist����m �P �q����hashMap���X�����|
	public Download_Processing(int index,String path) {
		
		flag = index;
		in_flag_path = path;
		
	}
	
	// �]�w���ɮ׷|�x�s�b���ӥؿ����U
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

			// ���榬�H
			try {
				IMAP imap = new IMAP(act, psd, store_name[i]);
				imap.start();
				imap.join();
			} catch (MessagingException | InterruptedException e) {
				e.printStackTrace();
			}

		}

		File zipfile;

		// �ɮ׫���B�z
		if (subnum <= 1) {
			zipfile = new File(store_name[0]);
			// �S���Q���ΡA��������Y
			try {
				unZipFile(zipfile.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			// ���Q���ΡA����X��
			System.out.println("�ɮ׶}�l�X��");
			zipfile = MergeFile();
			System.out.println("�X�֧���");
			System.out.println("�ɮ׶}�l�����Y");

			try {
				unZipFile(zipfile.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("�����Y����");

		}

		zipfile.delete();

		// �����Ҧ��B�z�A�����q���ϥΪ�
		
		changeStatus(2);
		Timer time = new Timer();
		time.schedule(new Set_Status(null, flag, in_flag_path), 5000);

	}

	// �X���ɮ�
	private File MergeFile() {
		
		return Merge.File_Merge(id, store_name);
	}

	// �����Y�ɮ�
	private void unZipFile(String zipPath) throws Exception {

		//�����Y�ɮ� (�ɮ׸��|,�����Y����ӥؿ����U)
		
		UnZip.unZip(zipPath, target);

	}

	// �����ɮץثe���A
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
