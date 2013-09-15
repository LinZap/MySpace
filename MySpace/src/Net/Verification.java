package Net;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.SubjectTerm;
import javax.swing.JOptionPane;

import System.System_parameters;
import UI.UI_act;

import com.sun.mail.imap.IMAPFolder;

public class Verification {

	public static boolean verification(String act, String psd)
			throws MessagingException, UnsupportedEncodingException, InterruptedException {

		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getInstance(props, null);
		Store store = session.getStore();

		try {

			store.connect("imap.gmail.com", act, psd);
		}

		catch (Exception e) {
			JOptionPane.showConfirmDialog(UI_act.ui_act,
					"帳號或密碼錯誤，請重新檢查並在試一次!!", "錯誤",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
			IMAPFolder inbox = (IMAPFolder) store.getFolder("Inbox");
			inbox.open(Folder.READ_WRITE);
			// 用OR搜尋，將分割檔名全部給予，抓取這些郵件
			SubjectTerm s = new SubjectTerm("[MySpace]Sub_Act");
			// 過濾出郵件
			Message[] msg = inbox.search(s);

			// 如果有找到識別郵件
			if (msg.length > 0) {

				//拆解信件主旨字串 [MySpace]Sub_Act + 空白 + 隸屬的帳號 
				String[] part_of = msg[0].getSubject().split(" ");
				
				// 通知使用者被哪個帳號加入過  part_of[1]
				JOptionPane.showConfirmDialog(UI_act.ui_act, "您新增的帳號已經被 "
						+ part_of[1] + " 加入過，無法再被加入...", "空間不能被重複加入!!",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
			
			// 沒有找到識別郵件，馬上自己寄送一封識別郵件進去
			else {
				
				//識別字串，設定其為信件的subject
				String subject = "[MySpace]Sub_Act " + System_parameters.root_act;
				Address[] to = { new InternetAddress(act) };
				Send_Msg smg = new Send_Msg(act, psd, " ", subject, to);
				smg.start();
				return true;
			}

		

	}

}
