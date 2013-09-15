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
 * �o�ӰƵ{�����������T
 * �D�n��]�G�U�����L�{���|�X�{�h�����򪺿��~�A����n�i��debug
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
		// ��OR�j�M�A�N�����ɦW���������A����o�Ƕl��
		SubjectTerm s = new SubjectTerm(downloadname);
		// �L�o�X�l��
		System.out.println("�}�l�j�M�l��: "+ downloadname);
		msg = inbox.search(s);
		System.out.println("�j�M��" + msg.length + "�ʶl��");
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
	
	//�U���l��
	private void resolve(Message messages) throws Exception {
		System.out.println("�}�l�U���l��");
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
				System.out.println("�����l��A���L�k�U������");
				// �ǳƻ��jReader
			}

		}
	}

	// ������ɮת�BodyPart�g�X�h
	private void saveFile(Message message, BodyPart part) throws Exception {
		InputStream in = part.getInputStream();
		File f = new File(message.getSubject());
		FileOutputStream out = new FileOutputStream(f);
		int i = 0;
		while ((i = in.read()) != -1) {
			out.write(i);
		}
		System.out.println("�U���l�󧹦�");
		
		out.close();
		in.close();
		
	}

	private void close() throws MessagingException {
		inbox.close(false);
		store.close();
	}

}
