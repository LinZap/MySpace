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

// 檔案分享( 接收端處理功能
public class Receive_requset extends TimerTask {

	private String act, psd;
	private Store store;
	private IMAPFolder inbox,garbage;

	// 執行收信
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
			garbage = (IMAPFolder) store.getFolder("[Gmail]").getFolder("垃圾郵件");
			garbage.open(Folder.READ_WRITE);
			

			SubjectTerm[] s = { new SubjectTerm("[MySpace]Request"),
					new SubjectTerm("[MySpace]Send"),
					new SubjectTerm("[MySpace]Complete"),
					new SubjectTerm("[MySpace]Accept") };

			OrTerm o = new OrTerm(s);

			Message[] msg = inbox.search(o);

			System.out.println("收到 " + msg.length + " 封郵件");

			for (Message m : msg) {

				String subject = m.getSubject();
				Address[] address = m.getFrom();

				MimeMultipart mp = (MimeMultipart) m.getContent();
				BodyPart part = mp.getBodyPart(0);
				String con = part.getContent().toString();

				System.out.println("郵件內容: " + con);

				String sp[] = con.split(",");

				// 分析不同工作
				switch (subject) {

				case "[MySpace]Request":

					System.out.println(Dialog_Doing.receive.get(sp[2]));
					if (Dialog_Doing.receive.get(sp[2]) == null)
						// 收到新請求
						add_request(con, address);
					UI_drive.btn_receive.setIcon(new ImageIcon("image\\new_ceive.fw.png"));
					break;

				case "[MySpace]Send":

					if (Dialog_Doing.receive.get(sp[2]) != null) {

						Object[] request = (Object[]) Dialog_Doing.receive
								.get(sp[2]);

						// 如果已經接受傳送
						if ((boolean) request[3]) {
							// 傳送狀態改成傳送
							request[4] = true;
							// 設定介面狀態
							set_receive_status(request, "傳送中 - ");
						}

					}

					break;

				case "[MySpace]Complete":

					if (Dialog_Doing.receive.get(sp[2]) != null) {
						Object[] request = (Object[]) Dialog_Doing.receive
								.get(sp[2]);
						// 如果已經屬於正在傳送
						if ((boolean) request[4]) {
							// 傳送狀態改成完成
							request[5] = true;
							// 設定介面狀態
							set_receive_status(request, "已經完成 - ");
						}

						// 寫入資料庫(map與xml檔)
						add_to_List_and_Xml(con);
						UI_drive.btn_receive.setIcon(new ImageIcon("image\\new_ceive.fw.png"));
					}

					break;

				case "[MySpace]Accept":
					// []
					// 檔名 0
					// 大小 1
					// 從何而來 2
					// 發送[MySpace]Send了沒 3
					// 檔案路徑 4
					// 檔案資料庫索引 5
					// 在ui中的index 6
					if (Dialog_Doing.send.get(sp[2]) != null) {

						System.out.println("取出資料的路徑是 " + sp[2]);

						Object[] reply = (Object[]) Dialog_Doing.send
								.get(sp[2]);

						// 寄送檔案與[MySpace]Send且 寄送完檔案後 再寄送[MySpace]Complete
						// address 寄送到哪裡
						send_file(reply, address, con);
					}

				}

				// 刪除以收過信件v0.3.2 (過時)
				//m.setFlag(Flags.Flag.DELETED, true)		
			}
			
			// 刪除已讀過信件
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

	// 寫入xml與List
	private void add_to_List_and_Xml(String con) throws DocumentException,
			IOException {

		// 新增到XML
		Xml_files xf = new Xml_files();
		Xml_act xa = new Xml_act();
		System.out.println("寫入資料庫之前的資料切割: " + con);

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

		// 新增到資料庫
		xf.add_Receive_FileToXml(name, size, id, sub, store, type, savedate,
				mod, use, at_act, at_psd);

		// 新增到List
		Object[] one_file = new Object[15];

		// 在 xml 內的index
		one_file[0] = xf.get_last_index();
		// id
		one_file[1] = id;
		// 檔案名稱
		one_file[2] = name;
		// 檔案大小
		one_file[3] = size;
		// 檔案類型
		one_file[4] = type;
		// 取得上傳日期
		one_file[5] = savedate;
		// 取得切割數量
		one_file[6] = sub;
		// 檔案儲存路徑
		one_file[7] = "[MySpace, 檔案接收櫃]";
		// 取得檔案儲存位置(把List交給副程式處理)
		one_file[8] = bulidstore(store, at_act, at_psd);
		// 使用頻率次數
		one_file[9] = 0;
		// 是否標星號
		one_file[10] = 0;
		// 檔案最後被修改的時間(long)
		one_file[11] = mod;
		// 在MySpace中最後被使用的時間
		one_file[12] = use;
		// 設定檔案上傳狀態(由Mailer來控制即可)
		one_file[13] = null;
		// 設定Checkbox的初始值
		one_file[14] = false;

		// 遞迴的將檔案用ArraysList串起來，在用Map排入對應的路徑內
		if (UI_drive.map.get("[MySpace, 檔案接收櫃]") == null) {
			ArrayList<Object> t = new ArrayList<Object>();
			t.add(one_file);
			UI_drive.map.put("[MySpace, 檔案接收櫃]", t);

		} else {

			@SuppressWarnings("unchecked")
			ArrayList<Object> t = (ArrayList<Object>) UI_drive.map
					.get("[MySpace, 檔案接收櫃]");
			t.add(one_file);
		}

		// 更新介面
		if ("[MySpace, 檔案接收櫃]".equals(System_parameters.tree_path)) {
			UI_drive.bulid_tableData();
			UI_drive.Model_Drive.setDataVector(UI_drive.table_data,
					UI_drive.columnTitle);
			UI_drive.table.setModel(UI_drive.Model_Drive);
			UI_drive.setTablefeeling();
		}

	}

	// 建立儲存內容的物件
	private Object bulidstore(String[] store2, String at_act, String at_psd) {
		Object[][] s = new Object[store2.length][3];

		for (int i = 0; i < store2.length; i++) {
			s[i][0] = store2[i];
			s[i][1] = at_act;
			s[i][2] = at_psd;

		}
		return s;
	}

	// 寄送檔案與[MySpace]Send且 寄送完檔案後 再寄送[MySpace]Complete
	// address 寄送到哪裡
	//
	private void send_file(Object[] reply, Address[] address, String con)
			throws UnsupportedEncodingException, MessagingException,
			InterruptedException {

		String detail = con;
		String sp[] = con.split(",");
		// 設定寄送位置
		Address[] to = { new InternetAddress(sp[3]) };

		String key = sp[2];

		// 寄一封傳送的訊息回去
		Send_Msg send = new Send_Msg(act, psd, con, "[MySpace]Send", address);
		send.start();

		set_send_status(reply, "正在", address);

		// 傳檔案
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

		// +檔案切割數量
		detail += "," + subnum;
		detail += ",";
		for (int i = 0; i < subnum; i++) {

			store_name[i] = store[i][0].toString();
			String act = store[i][1].toString();
			String psd = store[i][2].toString();

			// 檔案儲存名稱
			if (i != 0)
				detail += " ";
			detail += store_name[i];

			// 執行回信
			try {
				Reply_Msg rm = new Reply_Msg(act, psd, store_name[i], to,
						store_name[i]);
				rm.start();
				rm.join();
			} catch (MessagingException e) {
				e.printStackTrace();
			}

		}

		// 檔案類型
		detail += "," + file[4].toString();
		// 上傳日期
		detail += "," + file[5].toString();
		// 最後修改時間
		detail += "," + file[11].toString();

		// 寄一封傳送的訊息回去
		send = new Send_Msg(act, psd, detail, "[MySpace]Complete", address);
		send.start();
		set_send_status(reply, "已經完成", address);

	}

	// 新增一條請求(參數信件內容)
	private void add_request(String con, Address[] from)
			throws MessagingException,
			InterruptedException, DocumentException, IOException {

		Object[] request = new Object[7];
		String[] sp = con.split(",");

		// 檔案名稱
		request[0] = sp[0];
		// 檔案大小
		request[1] = sp[1];
		// 從何而來
		request[2] = from;
		// 是否已經接受傳送
		request[3] = false;
		// 是否已經傳送
		request[4] = false;
		// 是否已經完成
		request[5] = false;
		// 在介面中的索引
		request[6] = add_req(request, con);

		Dialog_Doing.receive.put(sp[2], request);

	}

	// 新增到資料庫
	private int add_req(Object[] request, String con)
			throws MessagingException,
			InterruptedException, DocumentException, IOException {

		Address[] a = (Address[]) request[2];
		String m = "從" + a[0] + "傳送：" + request[0] + " ，大小：" + request[1]
				+ "byte";
		Dialog_Doing.liatmodel.addElement("已接受檔案，正在等候" + m);
		int index = Dialog_Doing.liatmodel.size();
		request[5] = index;

		// 詢問使用者是否接受
		int msg = JOptionPane.showConfirmDialog(UI_drive.ui_dive, "是否接受   " + m
				+ "byte ?", "確認接受", JOptionPane.YES_NO_CANCEL_OPTION);

		if (msg == 0) {
			request[3] = true;

			String[] sp = con.split(",");
			long size = Long.parseLong(sp[1]);
			
			Xml_act xa = new Xml_act();
			String[] save_tar = xa.getaccount(size);
			
			if (save_tar != null) {
				// 寄送到正在使用的帳號，表示容量足夠
				con += "," + System_parameters.useing_act;
				Send_Msg send = new Send_Msg(act, psd, con, "[MySpace]Accept",
						(Address[]) request[2]);
				send.start();
			} else
				{JOptionPane.showConfirmDialog(UI_drive.ui_dive, "您已經沒有足夠的空間存放",
						"空間不足", JOptionPane.WARNING_MESSAGE);
				Dialog_Doing.liatmodel.setElementAt("空間不足，已經停止傳送"+request[0], index - 1);
				}

		}
		return index - 1;
	}

	// 設定介面顯示
	private void set_receive_status(Object[] request, String status) {

		Dialog_Doing.liatmodel.setElementAt(status + request[0],
				(int) request[6]);

	}

	// 設定介面顯示
	private void set_send_status(Object[] request, String status, Address[] to) {

		Dialog_Doing.liatmodel.setElementAt(status + request[0],
				(int) request[6]);

	}

	// 關閉連線
	private void close() throws MessagingException {
		inbox.close(true);
		garbage.close(true);
		store.close();
	}

}
