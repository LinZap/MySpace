package Net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.*;

import com.sun.mail.imap.IMAPFolder;

/*  
 * 這個副程式不完全正確
 * 主要原因：下載的過程中會出現多執行續的錯誤，後續要進行debug
 * 
 * */

public class IMAP extends Thread {

	private String act, psd;
	private String downloadname;
	private IMAPFolder inbox;
	private Store store;
	private Message msg[];

	public IMAP(String act, String psd, String downloadname)
			throws MessagingException {
		this.act = act;
		this.psd = psd;
		this.downloadname = downloadname;
		do_Read();
	}

	public void do_Read() throws MessagingException {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getInstance(props, null);
		store = session.getStore();
		store.connect("imap.gmail.com", act, psd);
		inbox = (IMAPFolder)store.getFolder("Inbox");
		inbox.open(Folder.READ_WRITE);
		// 用OR搜尋，將分割檔名全部給予，抓取這些郵件
		SubjectTerm s = new SubjectTerm(downloadname);
		// 過濾出郵件
		System.out.println("開始搜尋郵件: "+ downloadname);
		msg = inbox.search(s);
		System.out.println("搜尋到" + msg.length + "封郵件");
	}

	public void run() {

		for (Message m : msg) {
			try {
				resolve(m);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	//下載郵件
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
			} else {
				System.out.println("有找到郵件，但無法下載附件");
				// 準備遞迴Reader
			}

		}
	}

	// 把附件檔案的BodyPart寫出去
	private void saveFile(Message message, BodyPart part) throws Exception {
		InputStream in = part.getInputStream();
		File f = new File(message.getSubject());
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
