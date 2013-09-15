package Net;

import java.util.Properties;

import javax.mail.*;
import javax.mail.search.SubjectTerm;
import com.sun.mail.imap.IMAPFolder;

public class IMAP_Delete {

	public synchronized static void do_delete(String act, String psd,
			String downloadname) throws MessagingException {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getInstance(props, null);
		Store store = session.getStore();
		store.connect("imap.gmail.com", act, psd);
		IMAPFolder inbox = (IMAPFolder) store.getFolder("Inbox");
		inbox.open(Folder.READ_WRITE);

		IMAPFolder garbage = (IMAPFolder) store.getFolder("[Gmail]").getFolder(
				"�U���l��");
		garbage.open(Folder.READ_WRITE);

		// ��OR�j�M�A�N�����ɦW���������A����o�Ƕl��
		SubjectTerm s = new SubjectTerm(downloadname);
		// �L�o�X�l��
		System.out.println("�}�l�j�M�l��");
		Message[] msg = inbox.search(s);
		System.out.println("�j�M��" + msg.length + "�ʶl��");

		try {
			System.out.println("�}�l�h����U���l��");
			inbox.copyMessages(msg, garbage);

			System.out.println("�R���Ҧ��U���l��");
			Message[] garbage_msg = garbage.getMessages();
			garbage.setFlags(garbage_msg, new Flags(Flags.Flag.DELETED), true);
			System.out.println("�R������");
			inbox.close(true);
			garbage.close(true);
			store.close();
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
