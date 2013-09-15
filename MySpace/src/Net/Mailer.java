package Net;

/*
 * 這支副程式主要負責將檔案以郵件的形式寄送出去
 * 上傳空間為Gmail
 * 
 * 傳入3個參數(帳號、密碼與檔案)並調用do_send()方法即可
 * 
 * */
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Mailer {

	public static synchronized void do_send(final String account,
			final String password, File f, String subject)
			throws MessagingException, UnsupportedEncodingException,
			InterruptedException {

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

		Session session;
		// 建立Session物件

		session = Session.getInstance(pro, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(account, password);
			}
		});

		// 新的一封郵件
		MimeMessage msg = new MimeMessage(session);
		// 寄送資訊
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(account));
		msg.setFrom(new InternetAddress(account));
		msg.setSubject(subject);
		msg.setContent("123", "text/html;charset=UTF-8");
		msg.setText("123");
		// 設定附加檔
		// 附件類型
		MimeMultipart msgMultipart = new MimeMultipart("mixed");
		// 附件
		MimeBodyPart file = new MimeBodyPart();
		// 附件寫進郵件內容
		msgMultipart.addBodyPart(file);
		// 連結郵件
		msg.setContent(msgMultipart);
		// 設定附件名稱
		file.setFileName(MimeUtility.encodeText(f.getName(), "Big5", "B"));
		// 附件路徑
		DataSource path = new FileDataSource(f.getAbsolutePath());
		// 操作附件
		DataHandler h = new DataHandler(path);
		// 設定操作
		file.setDataHandler(h);

		try {
			System.out.println("正在寄送：" + subject);
			Transport.send(msg);
			System.out.println("完成：" + subject);
		} catch (MessagingException e) {
			System.out.println("寄送失敗：" + subject);
		}
	}

}
