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
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		msg.setFrom(new InternetAddress(account));
		msg.setSubject(subject);

		// �]�w���[��
		// ��������
		MimeMultipart msgMultipart = new MimeMultipart("mixed");
		// ����
		MimeBodyPart file = new MimeBodyPart();
		MimeBodyPart file2 = new MimeBodyPart();
		// ����g�i�l�󤺮e
		msgMultipart.addBodyPart(file);
		msgMultipart.addBodyPart(file2);
		// �s���l��
		msg.setContent(msgMultipart);
		// �]�w����W��
		file.setFileName(MimeUtility.encodeText(f.getName(), "UTF-8", "B"));
		file2.setText(body);
		// ������|
		DataSource pa = new FileDataSource(f.getAbsolutePath());
		// �ާ@����
		DataHandler h = new DataHandler(pa);
		// �]�w�ާ@
		file.setDataHandler(h);
		// �οW�ߪ�Thread����A�W�[�Ĳv
	
	}
	
	public void run() {
		try {	
			System.out.println("���b�H�e�G"+f.getName());
			Transport.send(msg);	
			System.out.println("�����G"+f.getName());
		} catch (MessagingException e) {
			System.out.println("�H�e���ѡG"+f.getName());
		}
	}
}
