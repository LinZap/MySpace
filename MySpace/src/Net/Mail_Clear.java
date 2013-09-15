package Net;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.NotTerm;
import javax.mail.search.SubjectTerm;
import com.sun.mail.imap.IMAPFolder;

// 清除非本系統郵件，本系統郵件都會附上[MySpace]字節
// 使用 NotTerm 過濾出沒有[MySpace] 字節的信件，加以刪除
/*
 INBOX : 10
 [Gmail]/全部郵件 : 58
 [Gmail]/垃圾桶 : 0
 [Gmail]/垃圾郵件 : 0
 [Gmail]/寄件備份 : 46
 [Gmail]/已加星號 : 0
 [Gmail]/草稿 : 0
 [Gmail]/重要郵件 : 36
 回條 : 0
 工作 : 0
 旅遊 : 0
 私人 : 0
 */

public class Mail_Clear extends Thread {

	private IMAPFolder inbox, sent, all, garbage,  draft, trash; //imp,
	private Store store;
	private Message[] msg, sent_msg, all_msg, garbage_msg,  draft_msg,
			trash_msg;//imp_msg,
	private String act, psd;

	public Mail_Clear(String act, String psd) {
		this.act = act;
		this.psd = psd;
System.out.println("clear mail act psd : " + act + ","+psd);
	}

	// 刪除
	public void run() {
		
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getInstance(props, null);
		try {
			store = session.getStore();
			store.connect("imap.gmail.com", act, psd);
			System.out.println("取得 " + act + " 收件夾");

			inbox = (IMAPFolder) store.getFolder("Inbox");
			sent = (IMAPFolder) store.getFolder("[Gmail]").getFolder("寄件備份");
			all = (IMAPFolder) store.getFolder("[Gmail]").getFolder("全部郵件");
			garbage = (IMAPFolder) store.getFolder("[Gmail]").getFolder("垃圾郵件");
			//imp = (IMAPFolder) store.getFolder("[Gmail]").getFolder("重要郵件");
			draft = (IMAPFolder) store.getFolder("[Gmail]").getFolder("草稿");
			trash = (IMAPFolder) store.getFolder("[Gmail]").getFolder("垃圾桶");

			inbox.open(Folder.READ_WRITE);
			sent.open(Folder.READ_WRITE);
			all.open(Folder.READ_WRITE);
			garbage.open(Folder.READ_WRITE);
			//imp.open(Folder.READ_WRITE);
			draft.open(Folder.READ_WRITE);
			trash.open(Folder.READ_WRITE);

			// 用 Not 搜尋，取得相反的結果

			SubjectTerm s = new SubjectTerm("[MySpace]");
			NotTerm not = new NotTerm(s);
			System.out.println("開始取得非本系統郵件");

			msg = inbox.search(not);
			all_msg = all.search(not);
			sent_msg = sent.search(not);
			//imp_msg = imp.search(not);
			draft_msg = draft.getMessages();

			// garbage_msg = garbage.getMessages();
			System.out.println("取得收件夾" + msg.length + "封郵件");
			System.out.println("取得寄件備份" + sent_msg.length + "封郵件");
			System.out.println("取得所有信件" + all_msg.length + "封郵件");
			//System.out.println("取得重要信件" + imp_msg.length + "封郵件");
			System.out.println("取得草稿" + draft_msg.length + "封郵件");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 收件夾,寄件備份,所有信件,重要信件,草稿 全部移到 垃圾郵件
		// 再刪除所有垃圾桶與垃圾郵件中的檔案

		try {
			System.out.println("搬移信件中...");

			inbox.copyMessages(msg, garbage);
			sent.copyMessages(sent_msg, garbage);
			all.copyMessages(all_msg, garbage);
			//imp.copyMessages(imp_msg, garbage);
			draft.copyMessages(draft_msg, garbage);

			System.out.println("刪除垃圾桶與垃圾信件中...");

			trash_msg = trash.getMessages();
			garbage_msg = garbage.getMessages();

			trash.setFlags(trash_msg, new Flags(Flags.Flag.DELETED), true);
			garbage.setFlags(garbage_msg, new Flags(Flags.Flag.DELETED), true);

			System.out.println("刪除完畢...");
			close();

		} catch (MessagingException e1) {
			e1.printStackTrace();
		}

	}

	private void close() throws MessagingException {

		inbox.close(true);
		sent.close(true);
		all.close(true);
		garbage.close(true);
		//imp.close(true);
		draft.close(true);
		trash.close(true);
		store.close();
	}
}

/*
 * 實驗階段使用 = 列出Gmail所有子目錄名稱 Folder[] folders =
 * store.getDefaultFolder().list("*"); for (javax.mail.Folder folder : folders)
 * { if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
 * System.out.println(folder.getFullName() + " : " + folder.getMessageCount());
 * } }
 */