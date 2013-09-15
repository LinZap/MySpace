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
			UI_waiting.status.setText("開始驗證帳號密碼...");
			store.connect("imap.gmail.com", act, psd);
			UI_waiting.status.setText("驗證成功...");

			// 產生 xml 檔案
			

			// 產生xml檔案名稱
			db_name = new String[] { System_parameters.get_db_name(0),
					System_parameters.get_db_name(1),
					System_parameters.get_db_name(2) };
		}

		catch (Exception e) {
			UI_waiting.status.setText("帳號或密碼錯誤...");
			JOptionPane.showConfirmDialog(UI_drive.ui_dive,
					"請確認帳號或密碼是否輸入錯誤，\n或是您網路尚未連線...", "登入失敗",
					JOptionPane.WARNING_MESSAGE);
			goback_UI_login();

		}

		try {
			inbox = (IMAPFolder) store.getFolder("Inbox");
			inbox.open(Folder.READ_WRITE);
		} catch (MessagingException e1) {
			JOptionPane
					.showConfirmDialog(UI_drive.ui_dive,
							"請確認您的網路是否連線，然後再試一次!!", "連線失敗",
							JOptionPane.WARNING_MESSAGE);
			goback_UI_login();
		}

		SubjectTerm[] s = { new SubjectTerm("[Myspace]dir.xml"),
				new SubjectTerm("[Myspace]files.xml"),
				new SubjectTerm("[Myspace]act.xml") };

		SubjectTerm main_act = new SubjectTerm("[MySpace]Sub_Act");

		OrTerm o = new OrTerm(s);
		UI_waiting.status.setText("正在搜尋資料庫...");
		// "開始搜尋郵件"
		try {
			msg = inbox.search(o);
			main_msg = inbox.search(main_act);
			System.out.println(msg.length);
		} catch (MessagingException e) {
			JOptionPane
					.showConfirmDialog(UI_drive.ui_dive,
							"請確認您的網路是否連線，然後再試一次!!", "連線失敗",
							JOptionPane.WARNING_MESSAGE);
			goback_UI_login();
		}
		UI_waiting.status.setText("正在建立系統資料...");

		// 資料庫檢查
		try {
			if (database_check()) {
				// 呼叫主介面 與系統功能

				try {

					// 偏好設定介面
					UI_set ui_set = new UI_set();
					ui_set.setVisible(false);

					// 雲端檔案介面
					UI_drive ui_dive = new UI_drive();
					UI_main.main_Container.add(ui_dive);

					
					
					// 隱藏等待畫面
					UI_waiting.ui_waiting.setVisible(false);
					ui_dive.setVisible(true);

					// 呼叫常駐
					new Daemon();

					// 啟動資料庫檢查是否需要備份的排程，每4分鐘(240,000 毫秒)一次(延遲5秒開始運作)
					timer = new Timer();
					timer.schedule(new Database_backup(), 5000, 240000);

					// 啟動收信排程，是否有傳送或接收檔案的需求信件，每15秒收一次
					timer2 = new Timer();
					timer2.schedule(new Receive_requset(
							System_parameters.root_act,
							System_parameters.root_psd), 0, 15000);

					// 刪除 以新增的帳戶 中 所有非本系統的郵件
					Xml_act xa = new Xml_act();
					Object[][] columnContext = xa.read_account();
					for (int g = 0; g < xa.get_act_count(); g++) {

						Mail_Clear mc = new Mail_Clear(
								columnContext[g][1].toString(),
								columnContext[g][2].toString());
						mc.start();
						mc.join();
					}

					UI_drive.used_space_label2.setText("目前空間： "+ xa.get_act_count()*10 + "GB  ");
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
		// 有識別信件
		if (check_key_msg()) {

			// 且又是主帳號
			if (is_main_act()) {
				
				// 在系統參數中紀錄資料
				System_parameters.root_act = System_parameters.useing_act = act;
				System_parameters.root_psd = System_parameters.useing_psd = psd;
				
				if (check_root_act_database()) {

					// 檢查本地端是否有資料庫
					if (check_local_database()) {

						// 比較與遠端的檔案哪個較新
						UI_waiting.status.setText("正在比對最新版本...");
						try {
							compare_version();
							set_db_modify();
						} catch (Exception e) {
							goback_UI_login();
							e.printStackTrace();
						}
						UI_waiting.status.setText("正在完成...");

					} else {

						// 表示使用者移至新平台使用
						UI_waiting.status.setText("下載檔案列表與資料庫...");
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

					// 帳戶沒有資料庫，但本地有，表示資料庫尚未備份
					if (check_local_database()) {

						UI_waiting.status.setText("正在備份資料庫...");
						database_mailer();
						set_db_modify();
						UI_waiting.status.setText("正在完成...");

						// 帳戶屬於第一次使用
					} else {

						UI_waiting.status.setText("開始建構資料庫...");
						// create xml files
						try {

							

							// 傳送識別子
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
								UI_waiting.status.setText("建構完成");
								UI_waiting.status.setText("正在備份資料庫...");
								database_mailer();
								UI_waiting.status.setText("正在完成...");
							}

						} catch (IOException | MessagingException
								| InterruptedException e) {
							UI_waiting.status.setText("資料庫建構失敗");
							goback_UI_login();
						}

					}
				}
				return true;
			}
			// 有識別信，但非主帳號
			else {
				//拒絕登入
				JOptionPane
				.showConfirmDialog(UI_drive.ui_dive,
						"您登入的帳號屬於一個子帳號，請使用主要的帳號登入!!", "不可使用子帳號登入",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}

		// 沒有識別信件
		else {

			// 傳送識別子
			String subject = "[MySpace]Sub_Act " + act;
			Address[] to = { new InternetAddress(act) };
			Send_Msg smg = new Send_Msg(act, psd, " ", subject, to);
			smg.start();
			smg.join();
			
			// 在系統參數中紀錄資料
			System_parameters.root_act = System_parameters.useing_act = act;
			System_parameters.root_psd = System_parameters.useing_psd = psd;
			

			if (check_root_act_database()) {

				// 檢查本地端是否有資料庫
				if (check_local_database()) {

					// 比較與遠端的檔案哪個較新
					UI_waiting.status.setText("正在比對最新版本...");
					try {
						compare_version();
						set_db_modify();
					} catch (Exception e) {
						goback_UI_login();
						e.printStackTrace();
					}
					UI_waiting.status.setText("正在完成...");

				} else {

					// 表示使用者移至新平台使用
					UI_waiting.status.setText("下載檔案列表與資料庫...");
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
			// 沒有資料庫 也沒有識別信件
			else {

				// 帳戶沒有資料庫，但本地有，表示資料庫尚未備份
				if (check_local_database()) {

					UI_waiting.status.setText("正在備份資料庫...");
					database_mailer();
					set_db_modify();
					UI_waiting.status.setText("正在完成...");

					// 帳戶屬於第一次使用
				} else {

					UI_waiting.status.setText("開始建構資料庫...");
					// create xml files
					try {

						// 在系統參數中紀錄資料
						System_parameters.root_act = System_parameters.useing_act = act;
						System_parameters.root_psd = System_parameters.useing_psd = psd;

						Xml_create xc = new Xml_create();
						for (int i = 0; i < System_parameters.get_db_count(); i++) {
							xc.create(System_parameters.get_db_path(i));
						}

						if (check_local_database()) {
							set_db_modify();
							UI_waiting.status.setText("建構完成");
							UI_waiting.status.setText("正在備份資料庫...");
							database_mailer();
							UI_waiting.status.setText("正在完成...");
						}

					} catch (IOException e) {
						UI_waiting.status.setText("資料庫建構失敗");
						goback_UI_login();
					}

				}
			}
			return true;
		}

	}

	// 紀錄資料庫 「最後被修改」 的時間
	private void set_db_modify() {
		for (int i = 0; i < System_parameters.get_db_count(); i++)
			System_parameters.db_modify[i] = new File(
					System_parameters.get_db_path(i)).lastModified();
	}

	// 上傳所有資料庫
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

	// 比較資料庫版本，並下載較新的資料庫，若無較新，則不下載
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

	// 比較本地端與雲端版本，判斷需要上傳覆蓋或是下載覆蓋
	private void save_or_download(long xml_version, long cloud_mod, Message m)
			throws Exception {
		if (xml_version < cloud_mod) {
			// 下載覆蓋
			download(m);
		} else if (xml_version > cloud_mod) {
			// 上傳覆蓋
			upload(m);
		}

	}

	// 回到登入畫面
	private void goback_UI_login() {
		UI_waiting.ui_waiting.setVisible(false);
		UI_login.ui_login.setVisible(true);
	}

	// 檢查本地端是否存在資料庫，3個都存在則回傳true
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

	// 檢查是否搜尋到資料庫
	private boolean check_root_act_database() {
		if (msg.length > 2)
			return true;
		else
			return false;
	}

	// 檢查是否找到識別郵件

	private boolean check_key_msg() throws MessagingException {

		if (main_msg.length > 0)
			return true;
		else
			return false;
	}

	// 檢查是否登入的為主帳號而不是子帳號
	private boolean is_main_act() throws MessagingException {

		String tar = main_msg[0].getSubject().split(" ")[1];
		if (tar.equals(act))
			return true;
		else
			return false;

	}

	// 執行上傳覆蓋
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

		// 刪除資料庫
		IMAP_Delete.do_delete(System_parameters.root_act,
				System_parameters.root_psd, db_name);

		

		// 寄出資料庫
		Database_Mailer dm = new Database_Mailer();
		dm.do_send(db_path);
		dm.start();
		dm.join();

	}

	// 執行下載
	public void download(Message m) throws Exception {
		resolve(m);

	}

	// 下載郵件
	private void resolve(Message messages) throws Exception {
		System.out.println("開始下載郵件");
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

	// 把附件檔案的BodyPart寫出去
	private synchronized void saveFile(Message message, BodyPart part)
			throws Exception {
		InputStream in = part.getInputStream();
		File f = new File("Database\\" + message.getSubject());
		FileOutputStream out = new FileOutputStream(f);
		int i = 0;
		while ((i = in.read()) != -1) {
			out.write(i);
		}
		System.out.println("下載郵件完成");
		out.close();
		in.close();

	}

	private void close() throws MessagingException {
		inbox.close(false);
		store.close();
	}
}
