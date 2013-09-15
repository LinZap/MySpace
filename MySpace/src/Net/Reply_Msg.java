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

	// ���X���
	// �h���̦��H
	// �]�w�H�����
	// �H�X
	// ������H�X[MySpace]Complete
	// �q���ϥΪ̤w�g����
	
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
	
	//���H�ó]�w�H�H��
	private void do_receive() throws MessagingException {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		IMAP_session = Session.getInstance(props, null);
		store = IMAP_session.getStore();
		store.connect("imap.gmail.com", act , psd);
		inbox = (IMAPFolder)store.getFolder("Inbox");
		inbox.open(Folder.READ_WRITE);
		// ��OR�j�M�A�N�����ɦW���������A����o�Ƕl��
		SubjectTerm s = new SubjectTerm(downloadname);
		// �L�o�X�l��
		System.out.println("�}�l�j�M�l��");
		msg = inbox.search(s);
		System.out.println("�j�M��" + msg.length + "�ʶl��");
		
	}

	public void run() {

		//�s�u��SMTP
		online();
	
		for (Message m : msg) {
			try {
				
				newMsg= new MimeMessage(SMTP_session);
				System.out.println("�H�e�� "+to[0].toString());
				newMsg.setRecipient(Message.RecipientType.TO, new InternetAddress(to[0].toString()));
				newMsg.setFrom(new InternetAddress(act));
				newMsg.setSubject(subject);
				newMsg.setContent("", "text/html;charset=UTF-8");
				newMsg.setText("");
				
				MimeMultipart msgMultipart = new MimeMultipart("mixed");
				// ����
				MimeBodyPart file = new MimeBodyPart();
				file.setContent(m ,"message/rfc822");
				// ����g�i�l�󤺮e
				msgMultipart.addBodyPart(file);
				// �s���l��
				newMsg.setContent(msgMultipart);
				
				System.out.println("���b�H�e"+m.getSubject());
				
				Transport.send(newMsg);

				System.out.println("�����H�e"+m.getSubject());
				
				
				
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
