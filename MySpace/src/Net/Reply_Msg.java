package Net;

import java.security.Security;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SubjectTerm;

import com.sun.mail.imap.IMAPFolder;


public class Reply_Msg extends Thread {

	// 取出資料
	// 去哪裡收信
	// 設定寄到哪裡
	// 寄出
	// 完成後寄出[MySpace]Complete
	// 通知使用者已經完成
	
	private String act, psd,subject;
	private String downloadname;
	private IMAPFolder inbox;
	private Store store;
	private Message msg[];
	private MimeMessage newMsg;
	private Address[] to;
	private Session IMAP_session;
	private Session SMTP_session;
	
	
	public Reply_Msg(String act, String psd, String dname, Address[] address,String subject) throws MessagingException{
		
		
		this.act = act;
		this.psd = psd;
		this.subject = subject;
		downloadname= dname;
		to = address;
		
		do_receive();
		
		
	}
	
	//收信並設定寄信位
	private void do_receive() throws MessagingException {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		IMAP_session = Session.getInstance(props, null);
		store = IMAP_session.getStore();
		store.connect("imap.gmail.com", act , psd);
		inbox = (IMAPFolder)store.getFolder("Inbox");
		inbox.open(Folder.READ_WRITE);
		// 用OR搜尋，將分割檔名全部給予，抓取這些郵件
		SubjectTerm s = new SubjectTerm(downloadname);
		// 過濾出郵件
		System.out.println("開始搜尋郵件");
		msg = inbox.search(s);
		System.out.println("搜尋到" + msg.length + "封郵件");
		
	}

	public void run() {

		//連線到SMTP
		online();
	
		for (Message m : msg) {
			try {
				
				newMsg= new MimeMessage(SMTP_session);
				System.out.println("寄送到 "+to[0].toString());
				newMsg.setRecipient(Message.RecipientType.TO, new InternetAddress(to[0].toString()));
				newMsg.setFrom(new InternetAddress(act));
				newMsg.setSubject(subject);
				newMsg.setContent("", "text/html;charset=UTF-8");
				newMsg.setText("");
				
				MimeMultipart msgMultipart = new MimeMultipart("mixed");
				// 附件
				MimeBodyPart file = new MimeBodyPart();
				file.setContent(m ,"message/rfc822");
				// 附件寫進郵件內容
				msgMultipart.addBodyPart(file);
				// 連結郵件
				newMsg.setContent(msgMultipart);
				
				System.out.println("正在寄送"+m.getSubject());
				
				Transport.send(newMsg);

				System.out.println("完成寄送"+m.getSubject());
				
				
				
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



	private void online() {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		// 寫入 parameter for Gmail SMTP
		Properties pro = new Properties();
		pro.setProperty("mail.smtp.host", "smtp.gmail.com");
		pro.setProperty("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		pro.setProperty("mail.smtp.socketFactory.fallback", "false");
		pro.setProperty("mail.smtp.port", "465");
		pro.setProperty("mail.smtp.socketFactory.port", "465");
		pro.put("mail.smtp.auth", "true");

		// 建立Session物件
		if (SMTP_session == null) {
			SMTP_session = Session.getInstance(pro, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(act, psd);
				}
			});
		}
	}

	private void close() throws MessagingException {
		inbox.close(true);
		store.close();
	}


}
