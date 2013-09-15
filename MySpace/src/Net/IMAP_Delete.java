package Net;

import java.util.Properties;

import javax.mail.*;
import javax.mail.search.SubjectTerm;
import com.sun.mail.imap.IMAPFolder;

public class IMAP_Delete {

	public synchronized static void do_delete(String act, String psd,
			String downloadname) throws MessagingException {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getInstance(props, null);
		Store store = session.getStore();
		store.connect("imap.gmail.com", act, psd);
		IMAPFolder inbox = (IMAPFolder) store.getFolder("Inbox");
		inbox.open(Folder.READ_WRITE);

		IMAPFolder garbage = (IMAPFolder) store.getFolder("[Gmail]").getFolder(
				"垃圾郵件");
		garbage.open(Folder.READ_WRITE);

		// 用OR搜尋，將分割檔名全部給予，抓取這些郵件
		SubjectTerm s = new SubjectTerm(downloadname);
		// 過濾出郵件
		System.out.println("開始搜尋郵件");
		Message[] msg = inbox.search(s);
		System.out.println("搜尋到" + msg.length + "封郵件");

		try {
			System.out.println("開始搬移到垃圾郵件");
			inbox.copyMessages(msg, garbage);

			System.out.println("刪除所有垃圾郵件");
			Message[] garbage_msg = garbage.getMessages();
			garbage.setFlags(garbage_msg, new Flags(Flags.Flag.DELETED), true);
			System.out.println("刪除完成");
			inbox.close(true);
			garbage.close(true);
			store.close();
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
