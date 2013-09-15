package Net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.TimerTask;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.OrTerm;
import javax.mail.search.SubjectTerm;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.dom4j.DocumentException;

import UI.Dialog_Doing;
import UI.UI_drive;
import Data.Xml_act;
import Data.Xml_files;
import System.System_parameters;

import com.sun.mail.imap.IMAPFolder;

// �ɮפ���( �����ݳB�z�\��
public class Receive_requset extends TimerTask {

	private String act, psd;
	private Store store;
	private IMAPFolder inbox,garbage;

	// ���榬�H
	public Receive_requset(String act, String psd) {

		this.act = act;
		this.psd = psd;

	}

	public void run() {

		try {

			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");
			Session session = Session.getInstance(props, null);
			store = session.getStore();
			store.connect("imap.gmail.com", act, psd);
			inbox = (IMAPFolder) store.getFolder("Inbox");
			inbox.open(Folder.READ_WRITE);
			garbage = (IMAPFolder) store.getFolder("[Gmail]").getFolder("�U���l��");
			garbage.open(Folder.READ_WRITE);
			

			SubjectTerm[] s = { new SubjectTerm("[MySpace]Request"),
					new SubjectTerm("[MySpace]Send"),
					new SubjectTerm("[MySpace]Complete"),
					new SubjectTerm("[MySpace]Accept") };

			OrTerm o = new OrTerm(s);

			Message[] msg = inbox.search(o);

			System.out.println("���� " + msg.length + " �ʶl��");

			for (Message m : msg) {

				String subject = m.getSubject();
				Address[] address = m.getFrom();

				MimeMultipart mp = (MimeMultipart) m.getContent();
				BodyPart part = mp.getBodyPart(0);
				String con = part.getContent().toString();

				System.out.println("�l�󤺮e: " + con);

				String sp[] = con.split(",");

				// ���R���P�u�@
				switch (subject) {

				case "[MySpace]Request":

					System.out.println(Dialog_Doing.receive.get(sp[2]));
					if (Dialog_Doing.receive.get(sp[2]) == null)
						// ����s�ШD
						add_request(con, address);
					UI_drive.btn_receive.setIcon(new ImageIcon("image\\new_ceive.fw.png"));
					break;

				case "[MySpace]Send":

					if (Dialog_Doing.receive.get(sp[2]) != null) {

						Object[] request = (Object[]) Dialog_Doing.receive
								.get(sp[2]);

						// �p�G�w�g�����ǰe
						if ((boolean) request[3]) {
							// �ǰe���A�令�ǰe
							request[4] = true;
							// �]�w�������A
							set_receive_status(request, "�ǰe�� - ");
						}

					}

					break;

				case "[MySpace]Complete":

					if (Dialog_Doing.receive.get(sp[2]) != null) {
						Object[] request = (Object[]) Dialog_Doing.receive
								.get(sp[2]);
						// �p�G�w�g�ݩ󥿦b�ǰe
						if ((boolean) request[4]) {
							// �ǰe���A�令����
							request[5] = true;
							// �]�w�������A
							set_receive_status(request, "�w�g���� - ");
						}

						// �g�J��Ʈw(map�Pxml��)
						add_to_List_and_Xml(con);
						UI_drive.btn_receive.setIcon(new ImageIcon("image\\new_ceive.fw.png"));
					}

					break;

				case "[MySpace]Accept":
					// []
					// �ɦW 0
					// �j�p 1
					// �q��Ө� 2
					// �o�e[MySpace]Send�F�S 3
					// �ɮ׸��| 4
					// �ɮ׸�Ʈw���� 5
					// �bui����index 6
					if (Dialog_Doing.send.get(sp[2]) != null) {

						System.out.println("���X��ƪ����|�O " + sp[2]);

						Object[] reply = (Object[]) Dialog_Doing.send
								.get(sp[2]);

						// �H�e�ɮ׻P[MySpace]Send�B �H�e���ɮ׫� �A�H�e[MySpace]Complete
						// address �H�e�����
						send_file(reply, address, con);
					}

				}

				// �R���H���L�H��v0.3.2 (�L��)
				//m.setFlag(Flags.Flag.DELETED, true)		
			}
			
			// �R���wŪ�L�H��
			inbox.copyMessages(msg, garbage);
			Message[] garbage_msg = garbage.getMessages();
			garbage.setFlags(garbage_msg, new Flags(Flags.Flag.DELETED), true);
			close();
			
		} catch (MessagingException e) {

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// �g�Jxml�PList
	private void add_to_List_and_Xml(String con) throws DocumentException,
			IOException {

		// �s�W��XML
		Xml_files xf = new Xml_files();
		Xml_act xa = new Xml_act();
		System.out.println("�g�J��Ʈw���e����Ƥ���: " + con);

		String[] sp = con.split(",");

		String name = sp[0];
		String size = sp[1];
		String id = sp[4];
		String sub = sp[5];
		String[] store = sp[6].split(" ");
		String type = sp[7];
		String savedate = sp[8];
		String mod = sp[9];

		SimpleDateFormat fm1 = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d = new Date();
		String use = fm1.format(d);

		String at_act = sp[3];
		String at_psd = xa.getPsd(sp[3]);

		// �s�W���Ʈw
		xf.add_Receive_FileToXml(name, size, id, sub, store, type, savedate,
				mod, use, at_act, at_psd);

		// �s�W��List
		Object[] one_file = new Object[15];

		// �b xml ����index
		one_file[0] = xf.get_last_index();
		// id
		one_file[1] = id;
		// �ɮצW��
		one_file[2] = name;
		// �ɮפj�p
		one_file[3] = size;
		// �ɮ�����
		one_file[4] = type;
		// ���o�W�Ǥ��
		one_file[5] = savedate;
		// ���o���μƶq
		one_file[6] = sub;
		// �ɮ��x�s���|
		one_file[7] = "[MySpace, �ɮױ����d]";
		// ���o�ɮ��x�s��m(��List�浹�Ƶ{���B�z)
		one_file[8] = bulidstore(store, at_act, at_psd);
		// �ϥ��W�v����
		one_file[9] = 0;
		// �O�_�ЬP��
		one_file[10] = 0;
		// �ɮ׳̫�Q�ק諸�ɶ�(long)
		one_file[11] = mod;
		// �bMySpace���̫�Q�ϥΪ��ɶ�
		one_file[12] = use;
		// �]�w�ɮפW�Ǫ��A(��Mailer�ӱ���Y�i)
		one_file[13] = null;
		// �]�wCheckbox����l��
		one_file[14] = false;

		// ���j���N�ɮץ�ArraysList��_�ӡA�b��Map�ƤJ���������|��
		if (UI_drive.map.get("[MySpace, �ɮױ����d]") == null) {
			ArrayList<Object> t = new ArrayList<Object>();
			t.add(one_file);
			UI_drive.map.put("[MySpace, �ɮױ����d]", t);

		} else {

			@SuppressWarnings("unchecked")
			ArrayList<Object> t = (ArrayList<Object>) UI_drive.map
					.get("[MySpace, �ɮױ����d]");
			t.add(one_file);
		}

		// ��s����
		if ("[MySpace, �ɮױ����d]".equals(System_parameters.tree_path)) {
			UI_drive.bulid_tableData();
			UI_drive.Model_Drive.setDataVector(UI_drive.table_data,
					UI_drive.columnTitle);
			UI_drive.table.setModel(UI_drive.Model_Drive);
			UI_drive.setTablefeeling();
		}

	}

	// �إ��x�s���e������
	private Object bulidstore(String[] store2, String at_act, String at_psd) {
		Object[][] s = new Object[store2.length][3];

		for (int i = 0; i < store2.length; i++) {
			s[i][0] = store2[i];
			s[i][1] = at_act;
			s[i][2] = at_psd;

		}
		return s;
	}

	// �H�e�ɮ׻P[MySpace]Send�B �H�e���ɮ׫� �A�H�e[MySpace]Complete
	// address �H�e�����
	//
	private void send_file(Object[] reply, Address[] address, String con)
			throws UnsupportedEncodingException, MessagingException,
			InterruptedException {

		String detail = con;
		String sp[] = con.split(",");
		// �]�w�H�e��m
		Address[] to = { new InternetAddress(sp[3]) };

		String key = sp[2];

		// �H�@�ʶǰe���T���^�h
		Send_Msg send = new Send_Msg(act, psd, con, "[MySpace]Send", address);
		send.start();

		set_send_status(reply, "���b", address);

		// ���ɮ�
		@SuppressWarnings("unchecked")
		ArrayList<Object> files = (ArrayList<Object>) UI_drive.map
				.get(reply[4]);

		int ggyy = Integer.parseInt(reply[5].toString());

		Object[] file = (Object[]) files.get(ggyy);
		Object[][] store = (Object[][]) file[8];
		String id = file[1].toString();
		int subnum = Integer.parseInt(file[6].toString());
		String[] store_name = new String[subnum];

		String[] spid = id.split("_");

		String new_id = spid[0] + "_" + key;

		// +id
		detail += "," + new_id;

		// +�ɮפ��μƶq
		detail += "," + subnum;
		detail += ",";
		for (int i = 0; i < subnum; i++) {

			store_name[i] = store[i][0].toString();
			String act = store[i][1].toString();
			String psd = store[i][2].toString();

			// �ɮ��x�s�W��
			if (i != 0)
				detail += " ";
			detail += store_name[i];

			// ����^�H
			try {
				Reply_Msg rm = new Reply_Msg(act, psd, store_name[i], to,
						store_name[i]);
				rm.start();
				rm.join();
			} catch (MessagingException e) {
				e.printStackTrace();
			}

		}

		// �ɮ�����
		detail += "," + file[4].toString();
		// �W�Ǥ��
		detail += "," + file[5].toString();
		// �̫�ק�ɶ�
		detail += "," + file[11].toString();

		// �H�@�ʶǰe���T���^�h
		send = new Send_Msg(act, psd, detail, "[MySpace]Complete", address);
		send.start();
		set_send_status(reply, "�w�g����", address);

	}

	// �s�W�@���ШD(�ѼƫH�󤺮e)
	private void add_request(String con, Address[] from)
			throws MessagingException,
			InterruptedException, DocumentException, IOException {

		Object[] request = new Object[7];
		String[] sp = con.split(",");

		// �ɮצW��
		request[0] = sp[0];
		// �ɮפj�p
		request[1] = sp[1];
		// �q��Ө�
		request[2] = from;
		// �O�_�w�g�����ǰe
		request[3] = false;
		// �O�_�w�g�ǰe
		request[4] = false;
		// �O�_�w�g����
		request[5] = false;
		// �b������������
		request[6] = add_req(request, con);

		Dialog_Doing.receive.put(sp[2], request);

	}

	// �s�W���Ʈw
	private int add_req(Object[] request, String con)
			throws MessagingException,
			InterruptedException, DocumentException, IOException {

		Address[] a = (Address[]) request[2];
		String m = "�q" + a[0] + "�ǰe�G" + request[0] + " �A�j�p�G" + request[1]
				+ "byte";
		Dialog_Doing.liatmodel.addElement("�w�����ɮסA���b����" + m);
		int index = Dialog_Doing.liatmodel.size();
		request[5] = index;

		// �߰ݨϥΪ̬O�_����
		int msg = JOptionPane.showConfirmDialog(UI_drive.ui_dive, "�O�_����   " + m
				+ "byte ?", "�T�{����", JOptionPane.YES_NO_CANCEL_OPTION);

		if (msg == 0) {
			request[3] = true;

			String[] sp = con.split(",");
			long size = Long.parseLong(sp[1]);
			
			Xml_act xa = new Xml_act();
			String[] save_tar = xa.getaccount(size);
			
			if (save_tar != null) {
				// �H�e�쥿�b�ϥΪ��b���A��ܮe�q����
				con += "," + System_parameters.useing_act;
				Send_Msg send = new Send_Msg(act, psd, con, "[MySpace]Accept",
						(Address[]) request[2]);
				send.start();
			} else
				{JOptionPane.showConfirmDialog(UI_drive.ui_dive, "�z�w�g�S���������Ŷ��s��",
						"�Ŷ�����", JOptionPane.WARNING_MESSAGE);
				Dialog_Doing.liatmodel.setElementAt("�Ŷ������A�w�g����ǰe"+request[0], index - 1);
				}

		}
		return index - 1;
	}

	// �]�w�������
	private void set_receive_status(Object[] request, String status) {

		Dialog_Doing.liatmodel.setElementAt(status + request[0],
				(int) request[6]);

	}

	// �]�w�������
	private void set_send_status(Object[] request, String status, Address[] to) {

		Dialog_Doing.liatmodel.setElementAt(status + request[0],
				(int) request[6]);

	}

	// �����s�u
	private void close() throws MessagingException {
		inbox.close(true);
		garbage.close(true);
		store.close();
	}

}
