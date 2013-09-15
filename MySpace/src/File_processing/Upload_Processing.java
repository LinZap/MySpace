package File_processing;

/**
 * 
 * �o�ӰƵ{�����������T(�ݭn�W����UML��A�i��ץ�)
 * �o�Ӫ���D�n�\��O�G�O���ɮץ浹�t�ΡA�̰_�l���P�_�u�@�B�I�s�W�ǡB�g�J�ɮ׸�T���A���ѳo��{���өI�s�P�հt
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

	// ���W��ɮסA�ݥ��ഫ���۩w�q��Obj_file���ͬ������
	private Obj_File f;

	private int FileFlag;

	// obj_file ���x�s���
	private String[][] store;
	// ���������W�ǬO�_����
	public  boolean fail = false;
	// �ɮ׳B�z�ɲ��ͪ����Y��
	private File zipFile;
	// �ɮשҦb��Ƨ����|
	private String in_dir_path;

	// �B�zxml��Ʈw����
	private Xml_files xf;

	// file ���W���ɮ� // in_path �ɮשҦb��Ƨ����|
	public Upload_Processing(File file, String in_path)
			throws DocumentException, IOException {

		// �B�zxml��Ʈw����

		// �N�ɮ��ন�ۭq�� Obj_File ���� (�ɮ�,�ɮשҦb��Ƨ����|)
		this.f = new Obj_File(file.getAbsolutePath(), in_path);

		//�Ŷ������A�~�|����
		if(f.is_enough){
			
			// �ɮ��x�s���
			this.store = f.store;

			// �ɮשҦb��Ƨ���m
			in_dir_path = in_path;

			xf = new Xml_files();
			// �����g�J��Ʈw
			xf.add_FileToXml(f);
			
			// �s�W�� Map
			add_to_list();

			@SuppressWarnings("unchecked")
			ArrayList<Object> files = (ArrayList<Object>) UI_drive.map
					.get(in_dir_path);
			FileFlag = files.size() - 1;
			
			changeStatus(0);
		}
		else{
			
			JOptionPane.showMessageDialog(UI_drive.ui_dive, "�z���x�s�Ŷ������A�i�H�s�W��hGmail�b�����X�R�Ŷ�!!",
					"���~", JOptionPane.ERROR_MESSAGE);
			fail = true;
		}
			
		
		
	}

	public void run() {

		// �⪬�A�令����
	

		try {
			if (f.size > 0) {

				
				// �������Y
				zipFile = dozip();
			}

		} catch (IOException e1) {
			JOptionPane.showMessageDialog(UI_drive.ui_dive, e1.getMessage(),
					"���~", JOptionPane.ERROR_MESSAGE);
			fail = true;
		}

		// �ɮפj�p�j��0�~��
		if (f.size > 0) {
			if (zipFile.exists()) {
				// ���μƶq�n�j��1�~�ݭn��
				if (f.sub_number > 1)
					try {
						// �ݭn����+�H�e
						Split_and_Send(zipFile);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(UI_drive.ui_dive,
								e.getMessage(), "�W�ǥ���",
								JOptionPane.ERROR_MESSAGE);
						fail = true;
					}
				else
					try {
						// �������Ϊ����H�e
						Send(zipFile);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(UI_drive.ui_dive,
								e.getMessage(), "�W�ǥ���",
								JOptionPane.ERROR_MESSAGE);
						fail = true;
					}
			}
		} else
			JOptionPane.showMessageDialog(UI_drive.ui_dive, "�L�Ī��ɮסA�]���ɮפj�p��0k",
					"�L�Ī��ɮ� !", JOptionPane.ERROR_MESSAGE);

		// �p�G�W�Ǫ��~�������󥢱Ѫ��ܡA���W�M�������ɮ׸��
		if (fail) {

			// delete_inList();

		} else {
			// System.out.println("�R�������Y�U���ɮסG"+zipFile.delete());
		}

		zipFile.delete();

	}

	// �ݭn���ΡA������for�j��H�e
	private void Split_and_Send(File zipfile) throws Exception {

		System.out.println("���Y�ä��Ϊ��ɮ׬�: " + zipfile);

		// ������ΡA�æ^�Ǥ��Ϋ᪺�ɮ�
		File[] subFile = Splitter.split(f, zipfile);

		// ���Χ���A����W��
		changeStatus(1);

		// for�j��H�e
		for (int i = 0; i < f.sub_number; i++) {
			// ��J�H�e�촼�b�K�A�PList��m
			Mailer.do_send(store[i][1], store[i][2], subFile[i],
					store[i][0]);
		}

		// �⪬�A�אּ���\
		changeStatus(2);

		// 5��� �⦨�\�����A����
		Timer time = new Timer();
		time.schedule(new Set_Status(null, FileFlag, in_dir_path), 5000);

		// �R���ɮ׳B�z�L�{�����ͪ�������
		for (File f : subFile)
			f.delete();

	}

	// ���ݭn���ΡA�����H�e
	private void Send(File zipfile) throws Exception {

		// ���A�אּ�W�Ǥ�
		changeStatus(1);

		// �}�l�H�e
		Mailer.do_send(store[0][1], store[0][2], zipfile, store[0][0]);
		
		// ���A�אּ����
		changeStatus(2);

		// 5���⦨�\�����A����
		Timer time = new Timer();
		time.schedule(new Set_Status(null, FileFlag, in_dir_path), 5000);

	}

	// ������ΡA�^�Ǥ��Ϋ᪺�ɮװ}�C
	private synchronized File dozip() throws FileNotFoundException, IOException {

		// �������Y�A�^�����Y�ɦW
		String zipFile_Name = Zipper.makeZip(f);
		// ���s�ɦV�ɮסA�^�����Y�X�Ӫ��s�ɮ�
		return new File(zipFile_Name);
	}

	// �s�W��ƨ�map��
	private void add_to_list() {

		Object[] one_file = new Object[15];

		// �b xml ����index
		one_file[0] = xf.get_last_index();
		// id
		one_file[1] = f.id;
		// �ɮצW��
		one_file[2] = f.name;
		// �ɮפj�p
		one_file[3] = f.size;
		// �ɮ�����
		one_file[4] = f.tpe;
		// ���o�W�Ǥ��
		one_file[5] = f.savedate;
		// ���o���μƶq
		one_file[6] = f.sub_number;
		// �ɮ��x�s���|
		one_file[7] = f.dir;
		// ���o�ɮ��x�s��m(��List�浹�Ƶ{���B�z)
		one_file[8] = f.store;
		// �ϥ��W�v����
		one_file[9] = f.freq;
		// �O�_�ЬP��
		one_file[10] = f.star;
		// �ɮ׳̫�Q�ק諸�ɶ�(long)
		one_file[11] = f.modified;
		// �bMySpace���̫�Q�ϥΪ��ɶ�
		one_file[12] = f.used;
		// �]�w�ɮפW�Ǫ��A(��Mailer�ӱ���Y�i)
		one_file[13] = null;
		// �]�wCheckbox����l��
		one_file[14] = false;

		// ���j���N�ɮץ�ArraysList��_�ӡA�b��Map�ƤJ���������|��
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

	// �����ɮץثe���A
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

		// �p�G�ɮשҦb���| ���� �ثe���| �h���ϥΪ̧�s����
		if (in_dir_path.equals(System_parameters.tree_path)) {
			UI_drive.bulid_tableData();
			UI_drive.Model_Drive.setDataVector(UI_drive.table_data,
					UI_drive.columnTitle);
			//UI_drive.table.setModel(UI_drive.Model_Drive);
			UI_drive.setTablefeeling();
		}

	}

}
