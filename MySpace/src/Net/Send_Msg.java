package Net;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Properties;

import javax.mail.Address;
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

public class Send_Msg extends Thread {

	private String account, password, subject, body;
	private Address[] address;
	private Session session = null;
	private Message msg;

	public Send_Msg(String act, String psd, String msg, String sub, Address[] to)
			throws UnsupportedEncodingException, MessagingException,
			InterruptedException {

		// 寄件人帳號
		account = act;
		// 寄件人密碼
		password = psd;
		// 信件主旨
		subject = sub;
		// 寄送位置
		address = to;
		// 信件內容
		this.body = msg;
		// 寄送
		do_send();
	}

	private void do_send() throws MessagingException,
			UnsupportedEncodingException, InterruptedException {

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
		msg.setRecipient(Message.RecipientType.TO, address[0]);
		msg.setFrom(new InternetAddress(account));
		msg.setSubject(subject);
		msg.setContent(body, "text/html;charset=UTF-8");

		// 附件類型
		MimeMultipart msgMultipart = new MimeMultipart("mixed");
		// 附件
		MimeBodyPart file = new MimeBodyPart();
		// 附件寫進郵件內容
		msgMultipart.addBodyPart(file);
		// 連結郵件
		msg.setContent(msgMultipart);
		// 設定附件名稱
		file.setText(body);

	}

	public void run() {
		try {
			System.out.println("正在寄送：" + subject + "  到  " + address[0] + "  內容為  " + body);
			Transport.send(msg);
			System.out.println("完成：" + subject);
			
		} catch (MessagingException e) {
			System.out.println("寄送失敗：" + subject);
		}
	}

}
