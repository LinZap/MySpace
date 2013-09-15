package File_processing;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.swing.JOptionPane;

import org.dom4j.DocumentException;

import Data.Xml_files;
import Net.IMAP_Delete;
import UI.UI_drive;
import System.System_parameters;

public class Delete_Processing extends Thread {

	private int flag, subnum;
	private String in_flag_path;
	private String act, psd, id;
	private String[] store_name;
	private Object[][] store;


	public Delete_Processing(String in_path,int index) {
		flag = index;
		in_flag_path = in_path;
	}

	public void run() {

		Xml_files xf = null;
		
		@SuppressWarnings("unchecked")
		ArrayList<Object> files = (ArrayList<Object>)UI_drive.map
				.get(System_parameters.tree_path);
		
		Object[] file = (Object[]) files.get(flag);
		
		
		store = (Object[][]) file[8];
		id = file[1].toString();
		subnum = Integer.parseInt(file[6].toString());
		store_name = new String[subnum];
		
		try {
			
			xf = new Xml_files();
			xf.delete_FileToTable(flag, in_flag_path);
			xf.delete_FileToList(flag ,in_flag_path);
			
			
		} catch (DocumentException e1) {
			JOptionPane.showConfirmDialog(UI_drive.ui_dive, "�R������!�Цb�դ@��!!",
					"����", JOptionPane.WARNING_MESSAGE);
			e1.printStackTrace();
		}

		
		for (int i = 0; i < subnum; i++) {

			store_name[i] = store[i][0].toString();
			act = store[i][1].toString();
			psd = store[i][2].toString();

			// ����R�H
			try {

				IMAP_Delete.do_delete(act, psd, store_name[i]);
				

			} catch (MessagingException e) {
				e.printStackTrace();
			}

		}

		// �R��xml�������

		try {
			
			if (xf.delete_FileToXml(id)) {
				
				
				JOptionPane.showConfirmDialog(UI_drive.ui_dive, "�ɮצ��\�R��!",
						"���\", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showConfirmDialog(UI_drive.ui_dive, "�R������!�Цb�դ@��!!",
						"����", JOptionPane.WARNING_MESSAGE);
			}
		} catch (IOException e) {
			JOptionPane.showConfirmDialog(UI_drive.ui_dive, "�R������!�Цb�դ@��!!",
					"����", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}

		// �R��table�W�����

	}

	

}
