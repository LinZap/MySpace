package Net;

/*
 * �o��Ƶ{���D�n�t�d�N�ɮץH�l�󪺧Φ��H�e�X�h
 * �W�ǪŶ���Gmail
 * 
 * �ǤJ3�ӰѼ�(�b���B�K�X�P�ɮ�)�ýե�do_send()��k�Y�i
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

		Session session;
		// �إ�Session����

		session = Session.getInstance(pro, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(account, password);
			}
		});

		// �s���@�ʶl��
		MimeMessage msg = new MimeMessage(session);
		// �H�e��T
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(account));
		msg.setFrom(new InternetAddress(account));
		msg.setSubject(subject);
		msg.setContent("123", "text/html;charset=UTF-8");
		msg.setText("123");
		// �]�w���[��
		// ��������
		MimeMultipart msgMultipart = new MimeMultipart("mixed");
		// ����
		MimeBodyPart file = new MimeBodyPart();
		// ����g�i�l�󤺮e
		msgMultipart.addBodyPart(file);
		// �s���l��
		msg.setContent(msgMultipart);
		// �]�w����W��
		file.setFileName(MimeUtility.encodeText(f.getName(), "Big5", "B"));
		// ������|
		DataSource path = new FileDataSource(f.getAbsolutePath());
		// �ާ@����
		DataHandler h = new DataHandler(path);
		// �]�w�ާ@
		file.setDataHandler(h);

		try {
			System.out.println("���b�H�e�G" + subject);
			Transport.send(msg);
			System.out.println("�����G" + subject);
		} catch (MessagingException e) {
			System.out.println("�H�e���ѡG" + subject);
		}
	}

}
