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

		// �H��H�b��
		account = act;
		// �H��H�K�X
		password = psd;
		// �H��D��
		subject = sub;
		// �H�e��m
		address = to;
		// �H�󤺮e
		this.body = msg;
		// �H�e
		do_send();
	}

	private void do_send() throws MessagingException,
			UnsupportedEncodingException, InterruptedException {

		// �H�U�}�l�s�u�]�w
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		// �g�J parameter for Gmail SMTP
		Properties pro = new Properties();
		pro.setProperty("mail.smtp.host", "smtp.gmail.com");
		pro.setProperty("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		pro.setProperty("mail.smtp.socketFactory.fallback", "false");
		pro.setProperty("mail.smtp.port", "465");
		pro.setProperty("mail.smtp.socketFactory.port", "465");
		pro.put("mail.smtp.auth", "true");

		// �إ�Session����
		if (session == null) {
			session = Session.getInstance(pro, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(account, password);
				}
			});
		}
		// �s���@�ʶl��
		msg = new MimeMessage(session);
		// �H�e��T
		msg.setRecipient(Message.RecipientType.TO, address[0]);
		msg.setFrom(new InternetAddress(account));
		msg.setSubject(subject);
		msg.setContent(body, "text/html;charset=UTF-8");

		// ��������
		MimeMultipart msgMultipart = new MimeMultipart("mixed");
		// ����
		MimeBodyPart file = new MimeBodyPart();
		// ����g�i�l�󤺮e
		msgMultipart.addBodyPart(file);
		// �s���l��
		msg.setContent(msgMultipart);
		// �]�w����W��
		file.setText(body);

	}

	public void run() {
		try {
			System.out.println("���b�H�e�G" + subject + "  ��  " + address[0] + "  ���e��  " + body);
			Transport.send(msg);
			System.out.println("�����G" + subject);
			
		} catch (MessagingException e) {
			System.out.println("�H�e���ѡG" + subject);
		}
	}

}
