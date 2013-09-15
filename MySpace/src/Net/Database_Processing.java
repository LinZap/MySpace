package Net;

import java.io.*;
import java.util.Properties;
import java.util.Timer;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.OrTerm;
import javax.mail.search.SubjectTerm;
import javax.swing.JOptionPane;

import org.dom4j.DocumentException;
import Data.Xml_act;
import Data.Xml_create;
import System.System_parameters;
import UI.Daemon;
import UI.UI_drive;
import UI.UI_login;
import UI.UI_main;
import UI.UI_set;
import UI.UI_waiting;

import com.sun.mail.imap.IMAPFolder;

public class Database_Processing extends Thread {

	private IMAPFolder inbox;
	private Store store;
	private Message[] msg, main_msg;
	private String act, psd;
	// files[0] , dir[1] , act[2]
	//private File[] xml_file;
	private String[] db_name;
	private Timer timer, timer2;

	public Database_Processing(String act, String psd)
			throws MessagingException {
		this.act = act;
		this.psd = psd;

		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getInstance(props, null);
		store = session.getStore();
	}

	public void run() {

		try {
			UI_waiting.status.setText("�}�l���ұb���K�X...");
			store.connect("imap.gmail.com", act, psd);
			UI_waiting.status.setText("���Ҧ��\...");

			// ���� xml �ɮ�
			

			// ����xml�ɮצW��
			db_name = new String[] { System_parameters.get_db_name(0),
					System_parameters.get_db_name(1),
					System_parameters.get_db_name(2) };
		}

		catch (Exception e) {
			UI_waiting.status.setText("�b���αK�X���~...");
			JOptionPane.showConfirmDialog(UI_drive.ui_dive,
					"�нT�{�b���αK�X�O�_��J���~�A\n�άO�z�����|���s�u...", "�n�J����",
					JOptionPane.WARNING_MESSAGE);
			goback_UI_login();

		}

		try {
			inbox = (IMAPFolder) store.getFolder("Inbox");
			inbox.open(Folder.READ_WRITE);
		} catch (MessagingException e1) {
			JOptionPane
					.showConfirmDialog(UI_drive.ui_dive,
							"�нT�{�z�������O�_�s�u�A�M��A�դ@��!!", "�s�u����",
							JOptionPane.WARNING_MESSAGE);
			goback_UI_login();
		}

		SubjectTerm[] s = { new SubjectTerm("[Myspace]dir.xml"),
				new SubjectTerm("[Myspace]files.xml"),
				new SubjectTerm("[Myspace]act.xml") };

		SubjectTerm main_act = new SubjectTerm("[MySpace]Sub_Act");

		OrTerm o = new OrTerm(s);
		UI_waiting.status.setText("���b�j�M��Ʈw...");
		// "�}�l�j�M�l��"
		try {
			msg = inbox.search(o);
			main_msg = inbox.search(main_act);
			System.out.println(msg.length);
		} catch (MessagingException e) {
			JOptionPane
					.showConfirmDialog(UI_drive.ui_dive,
							"�нT�{�z�������O�_�s�u�A�M��A�դ@��!!", "�s�u����",
							JOptionPane.WARNING_MESSAGE);
			goback_UI_login();
		}
		UI_waiting.status.setText("���b�إߨt�θ��...");

		// ��Ʈw�ˬd
		try {
			if (database_check()) {
				// �I�s�D���� �P�t�Υ\��

				try {

					// ���n�]�w����
					UI_set ui_set = new UI_set();
					ui_set.setVisible(false);

					// �����ɮפ���
					UI_drive ui_dive = new UI_drive();
					UI_main.main_Container.add(ui_dive);

					
					
					// ���õ��ݵe��
					UI_waiting.ui_waiting.setVisible(false);
					ui_dive.setVisible(true);

					// �I�s�`�n
					new Daemon();

					// �Ұʸ�Ʈw�ˬd�O�_�ݭn�ƥ����Ƶ{�A�C4����(240,000 �@��)�@��(����5��}�l�B�@)
					timer = new Timer();
					timer.schedule(new Database_backup(), 5000, 240000);

					// �Ұʦ��H�Ƶ{�A�O�_���ǰe�α����ɮת��ݨD�H��A�C15���@��
					timer2 = new Timer();
					timer2.schedule(new Receive_requset(
							System_parameters.root_act,
							System_parameters.root_psd), 0, 15000);

					// �R�� �H�s�W���b�� �� �Ҧ��D���t�Ϊ��l��
					Xml_act xa = new Xml_act();
					Object[][] columnContext = xa.read_account();
					for (int g = 0; g < xa.get_act_count(); g++) {

						Mail_Clear mc = new Mail_Clear(
								columnContext[g][1].toString(),
								columnContext[g][2].toString());
						mc.start();
						mc.join();
					}

					UI_drive.used_space_label2.setText("�ثe�Ŷ��G "+ xa.get_act_count()*10 + "GB  ");
					xa.setsp();
					
				} catch (DocumentException | InterruptedException e) {

					goback_UI_login();
					e.printStackTrace();
				}

				try {
					close();
				} catch (MessagingException e) {

				}

			} else

			{
				goback_UI_login();

			}
		} catch (MessagingException e1) {

			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean database_check() throws MessagingException,
			UnsupportedEncodingException, InterruptedException {
		// ���ѧO�H��
		if (check_key_msg()) {

			// �B�S�O�D�b��
			if (is_main_act()) {
				
				// �b�t�ΰѼƤ��������
				System_parameters.root_act = System_parameters.useing_act = act;
				System_parameters.root_psd = System_parameters.useing_psd = psd;
				
				if (check_root_act_database()) {

					// �ˬd���a�ݬO�_����Ʈw
					if (check_local_database()) {

						// ����P���ݪ��ɮ׭��Ӹ��s
						UI_waiting.status.setText("���b���̷s����...");
						try {
							compare_version();
							set_db_modify();
						} catch (Exception e) {
							goback_UI_login();
							e.printStackTrace();
						}
						UI_waiting.status.setText("���b����...");

					} else {

						// ��ܨϥΪ̲��ܷs���x�ϥ�
						UI_waiting.status.setText("�U���ɮצC��P��Ʈw...");
						for (Message m : msg)
							try {
								download(m);
								set_db_modify();
							} catch (Exception e) {
								e.printStackTrace();
								goback_UI_login();
							}
					}

				}
			
				else {

					// �b��S����Ʈw�A�����a���A��ܸ�Ʈw�|���ƥ�
					if (check_local_database()) {

						UI_waiting.status.setText("���b�ƥ���Ʈw...");
						database_mailer();
						set_db_modify();
						UI_waiting.status.setText("���b����...");

						// �b���ݩ�Ĥ@���ϥ�
					} else {

						UI_waiting.status.setText("�}�l�غc��Ʈw...");
						// create xml files
						try {

							

							// �ǰe�ѧO�l
							String subject = "[MySpace]Sub_Act " + act;
							Address[] to = { new InternetAddress(act) };
							Send_Msg smg = new Send_Msg(act, psd, " ", subject,
									to);
							smg.start();
							smg.join();

							Xml_create xc = new Xml_create();
							for (int i = 0; i < System_parameters
									.get_db_count(); i++) {
								xc.create(System_parameters.get_db_path(i));
							}

							if (check_local_database()) {
								set_db_modify();
								UI_waiting.status.setText("�غc����");
								UI_waiting.status.setText("���b�ƥ���Ʈw...");
								database_mailer();
								UI_waiting.status.setText("���b����...");
							}

						} catch (IOException | MessagingException
								| InterruptedException e) {
							UI_waiting.status.setText("��Ʈw�غc����");
							goback_UI_login();
						}

					}
				}
				return true;
			}
			// ���ѧO�H�A���D�D�b��
			else {
				//�ڵ��n�J
				JOptionPane
				.showConfirmDialog(UI_drive.ui_dive,
						"�z�n�J���b���ݩ�@�Ӥl�b���A�ШϥΥD�n���b���n�J!!", "���i�ϥΤl�b���n�J",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}

		// �S���ѧO�H��
		else {

			// �ǰe�ѧO�l
			String subject = "[MySpace]Sub_Act " + act;
			Address[] to = { new InternetAddress(act) };
			Send_Msg smg = new Send_Msg(act, psd, " ", subject, to);
			smg.start();
			smg.join();
			
			// �b�t�ΰѼƤ��������
			System_parameters.root_act = System_parameters.useing_act = act;
			System_parameters.root_psd = System_parameters.useing_psd = psd;
			

			if (check_root_act_database()) {

				// �ˬd���a�ݬO�_����Ʈw
				if (check_local_database()) {

					// ����P���ݪ��ɮ׭��Ӹ��s
					UI_waiting.status.setText("���b���̷s����...");
					try {
						compare_version();
						set_db_modify();
					} catch (Exception e) {
						goback_UI_login();
						e.printStackTrace();
					}
					UI_waiting.status.setText("���b����...");

				} else {

					// ��ܨϥΪ̲��ܷs���x�ϥ�
					UI_waiting.status.setText("�U���ɮצC��P��Ʈw...");
					for (Message m : msg)
						try {
							download(m);
							set_db_modify();
						} catch (Exception e) {
							e.printStackTrace();
							goback_UI_login();
						}
				}

			}
			// �S����Ʈw �]�S���ѧO�H��
			else {

				// �b��S����Ʈw�A�����a���A��ܸ�Ʈw�|���ƥ�
				if (check_local_database()) {

					UI_waiting.status.setText("���b�ƥ���Ʈw...");
					database_mailer();
					set_db_modify();
					UI_waiting.status.setText("���b����...");

					// �b���ݩ�Ĥ@���ϥ�
				} else {

					UI_waiting.status.setText("�}�l�غc��Ʈw...");
					// create xml files
					try {

						// �b�t�ΰѼƤ��������
						System_parameters.root_act = System_parameters.useing_act = act;
						System_parameters.root_psd = System_parameters.useing_psd = psd;

						Xml_create xc = new Xml_create();
						for (int i = 0; i < System_parameters.get_db_count(); i++) {
							xc.create(System_parameters.get_db_path(i));
						}

						if (check_local_database()) {
							set_db_modify();
							UI_waiting.status.setText("�غc����");
							UI_waiting.status.setText("���b�ƥ���Ʈw...");
							database_mailer();
							UI_waiting.status.setText("���b����...");
						}

					} catch (IOException e) {
						UI_waiting.status.setText("��Ʈw�غc����");
						goback_UI_login();
					}

				}
			}
			return true;
		}

	}

	// ������Ʈw �u�̫�Q�ק�v ���ɶ�
	private void set_db_modify() {
		for (int i = 0; i < System_parameters.get_db_count(); i++)
			System_parameters.db_modify[i] = new File(
					System_parameters.get_db_path(i)).lastModified();
	}

	// �W�ǩҦ���Ʈw
	private void database_mailer() {

		for (int i = 0; i < System_parameters.get_db_count(); i++) {
			try {
				Database_Mailer dm = new Database_Mailer();
				dm.do_send(System_parameters.get_db_path(i));
				dm.start();
				dm.join();
			} catch (UnsupportedEncodingException | MessagingException
					| InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	// �����Ʈw�����A�äU�����s����Ʈw�A�Y�L���s�A�h���U��
	private void compare_version() throws Exception {
		File[] xml_file = new File[] { new File(System_parameters.get_db_path(0)),
				new File(System_parameters.get_db_path(1)),
				new File(System_parameters.get_db_path(2)) };
		long xml_files_version = xml_file[0].lastModified();
		long xml_dir_version = xml_file[1].lastModified();
		long xml_act_version = xml_file[2].lastModified();

		// "[Myspace]files.xml","[Myspace]dir.xml","[Myspace]act.xml"}

		for (Message m : msg) {

			String subject = m.getSubject();
			MimeMultipart mp = (MimeMultipart) m.getContent();
			BodyPart part = mp.getBodyPart(1);
			long cloud_mod = Long.parseLong(part.getContent().toString());

			if (subject.equals(db_name[0])) {
				System.out.println(xml_files_version + "   " + cloud_mod);
				save_or_download(xml_files_version, cloud_mod, m);
				
			} else if (subject.equals(db_name[1])) {
				System.out.println(xml_files_version + "   " + cloud_mod);
				save_or_download(xml_dir_version, cloud_mod, m);

			} else if (subject.equals(db_name[2])) {
				System.out.println(xml_files_version + "   " + cloud_mod);
				save_or_download(xml_act_version, cloud_mod, m);
			}
		}
	}

	// ������a�ݻP���ݪ����A�P�_�ݭn�W���л\�άO�U���л\
	private void save_or_download(long xml_version, long cloud_mod, Message m)
			throws Exception {
		if (xml_version < cloud_mod) {
			// �U���л\
			download(m);
		} else if (xml_version > cloud_mod) {
			// �W���л\
			upload(m);
		}

	}

	// �^��n�J�e��
	private void goback_UI_login() {
		UI_waiting.ui_waiting.setVisible(false);
		UI_login.ui_login.setVisible(true);
	}

	// �ˬd���a�ݬO�_�s�b��Ʈw�A3�ӳ��s�b�h�^��true
	private boolean check_local_database() {
		File[] xml_file = new File[] { new File(System_parameters.get_db_path(0)),
				new File(System_parameters.get_db_path(1)),
				new File(System_parameters.get_db_path(2)) };
		if (xml_file[0].exists() && xml_file[1].exists()
				&& xml_file[2].exists())
			return true;
		else
			return false;
	}

	// �ˬd�O�_�j�M���Ʈw
	private boolean check_root_act_database() {
		if (msg.length > 2)
			return true;
		else
			return false;
	}

	// �ˬd�O�_����ѧO�l��

	private boolean check_key_msg() throws MessagingException {

		if (main_msg.length > 0)
			return true;
		else
			return false;
	}

	// �ˬd�O�_�n�J�����D�b���Ӥ��O�l�b��
	private boolean is_main_act() throws MessagingException {

		String tar = main_msg[0].getSubject().split(" ")[1];
		if (tar.equals(act))
			return true;
		else
			return false;

	}

	// ����W���л\
	private void upload(Message m) throws UnsupportedEncodingException,
			MessagingException, InterruptedException {

		String sj = m.getSubject();
		String db_name = null, db_path = null;

		if (sj.equals(System_parameters.get_db_name(0))) {
			db_name = System_parameters.db_search[0];
			db_path = System_parameters.get_db_path(0);
		} else if (sj.equals(System_parameters.get_db_name(1))) {
			db_name = System_parameters.db_search[1];
			db_path = System_parameters.get_db_path(1);
		} else if (sj.equals(System_parameters.get_db_name(2))) {
			db_name = System_parameters.db_search[2];
			db_path = System_parameters.get_db_path(2);
		}

		// �R����Ʈw
		IMAP_Delete.do_delete(System_parameters.root_act,
				System_parameters.root_psd, db_name);

		

		// �H�X��Ʈw
		Database_Mailer dm = new Database_Mailer();
		dm.do_send(db_path);
		dm.start();
		dm.join();

	}

	// ����U��
	public void download(Message m) throws Exception {
		resolve(m);

	}

	// �U���l��
	private void resolve(Message messages) throws Exception {
		System.out.println("�}�l�U���l��");
		String disposition;
		BodyPart part;
		MimeMultipart mp = (MimeMultipart) messages.getContent();
		int mpCount = mp.getCount();
		for (int m = 0; m < mpCount; m++) {
			part = mp.getBodyPart(m);
			disposition = part.getDisposition();
			if (disposition != null
					&& disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
				saveFile(messages, part);
			}
		}
	}

	// ������ɮת�BodyPart�g�X�h
	private synchronized void saveFile(Message message, BodyPart part)
			throws Exception {
		InputStream in = part.getInputStream();
		File f = new File("Database\\" + message.getSubject());
		FileOutputStream out = new FileOutputStream(f);
		int i = 0;
		while ((i = in.read()) != -1) {
			out.write(i);
		}
		System.out.println("�U���l�󧹦�");
		out.close();
		in.close();

	}

	private void close() throws MessagingException {
		inbox.close(false);
		store.close();
	}
}
