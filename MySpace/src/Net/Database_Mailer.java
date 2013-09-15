package Net;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import System.System_parameters;

public class Database_Mailer extends Thread {
	private Session session = null;
	private File f;
	private String account = System_parameters.root_act,
			password = System_parameters.root_psd, subject, to, body;

	private Message msg;


	public void do_send(String path) throws MessagingException,
			UnsupportedEncodingException, InterruptedException {

		f = new File(path);
		subject = f.getName();
		to = account;
		body = String.valueOf(f.lastModified());
		
		// 以下開始連線設定
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
		if (session == null) {
			session = Session.getInstance(pro, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(account, password);
				}
			});
		}
		// 新的一封郵件
		msg = new MimeMessage(session);
		// 寄送資訊
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		msg.setFrom(new InternetAddress(account));
		msg.setSubject(subject);

		// 設定附加檔
		// 附件類型
		MimeMultipart msgMultipart = new MimeMultipart("mixed");
		// 附件
		MimeBodyPart file = new MimeBodyPart();
		MimeBodyPart file2 = new MimeBodyPart();
		// 附件寫進郵件內容
		msgMultipart.addBodyPart(file);
		msgMultipart.addBodyPart(file2);
		// 連結郵件
		msg.setContent(msgMultipart);
		// 設定附件名稱
		file.setFileName(MimeUtility.encodeText(f.getName(), "UTF-8", "B"));
		file2.setText(body);
		// 附件路徑
		DataSource pa = new FileDataSource(f.getAbsolutePath());
		// 操作附件
		DataHandler h = new DataHandler(pa);
		// 設定操作
		file.setDataHandler(h);
		// 用獨立的Thread執行，增加效率
	
	}
	
	public void run() {
		try {	
			System.out.println("正在寄送："+f.getName());
			Transport.send(msg);	
			System.out.println("完成："+f.getName());
		} catch (MessagingException e) {
			System.out.println("寄送失敗："+f.getName());
		}
	}
}
