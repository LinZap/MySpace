package Net;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.NotTerm;
import javax.mail.search.SubjectTerm;
import com.sun.mail.imap.IMAPFolder;

// �M���D���t�ζl��A���t�ζl�󳣷|���W[MySpace]�r�`
// �ϥ� NotTerm �L�o�X�S��[MySpace] �r�`���H��A�[�H�R��
/*
 INBOX : 10
 [Gmail]/�����l�� : 58
 [Gmail]/�U���� : 0
 [Gmail]/�U���l�� : 0
 [Gmail]/�H��ƥ� : 46
 [Gmail]/�w�[�P�� : 0
 [Gmail]/��Z : 0
 [Gmail]/���n�l�� : 36
 �^�� : 0
 �u�@ : 0
 �ȹC : 0
 �p�H : 0
 */

public class Mail_Clear extends Thread {

	private IMAPFolder inbox, sent, all, garbage,  draft, trash; //imp,
	private Store store;
	private Message[] msg, sent_msg, all_msg, garbage_msg,  draft_msg,
			trash_msg;//imp_msg,
	private String act, psd;

	public Mail_Clear(String act, String psd) {
		this.act = act;
		this.psd = psd;
System.out.println("clear mail act psd : " + act + ","+psd);
	}

	// �R��
	public void run() {
		
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getInstance(props, null);
		try {
			store = session.getStore();
			store.connect("imap.gmail.com", act, psd);
			System.out.println("���o " + act + " ����");

			inbox = (IMAPFolder) store.getFolder("Inbox");
			sent = (IMAPFolder) store.getFolder("[Gmail]").getFolder("�H��ƥ�");
			all = (IMAPFolder) store.getFolder("[Gmail]").getFolder("�����l��");
			garbage = (IMAPFolder) store.getFolder("[Gmail]").getFolder("�U���l��");
			//imp = (IMAPFolder) store.getFolder("[Gmail]").getFolder("���n�l��");
			draft = (IMAPFolder) store.getFolder("[Gmail]").getFolder("��Z");
			trash = (IMAPFolder) store.getFolder("[Gmail]").getFolder("�U����");

			inbox.open(Folder.READ_WRITE);
			sent.open(Folder.READ_WRITE);
			all.open(Folder.READ_WRITE);
			garbage.open(Folder.READ_WRITE);
			//imp.open(Folder.READ_WRITE);
			draft.open(Folder.READ_WRITE);
			trash.open(Folder.READ_WRITE);

			// �� Not �j�M�A���o�ۤϪ����G

			SubjectTerm s = new SubjectTerm("[MySpace]");
			NotTerm not = new NotTerm(s);
			System.out.println("�}�l���o�D���t�ζl��");

			msg = inbox.search(not);
			all_msg = all.search(not);
			sent_msg = sent.search(not);
			//imp_msg = imp.search(not);
			draft_msg = draft.getMessages();

			// garbage_msg = garbage.getMessages();
			System.out.println("���o����" + msg.length + "�ʶl��");
			System.out.println("���o�H��ƥ�" + sent_msg.length + "�ʶl��");
			System.out.println("���o�Ҧ��H��" + all_msg.length + "�ʶl��");
			//System.out.println("���o���n�H��" + imp_msg.length + "�ʶl��");
			System.out.println("���o��Z" + draft_msg.length + "�ʶl��");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ����,�H��ƥ�,�Ҧ��H��,���n�H��,��Z �������� �U���l��
		// �A�R���Ҧ��U����P�U���l�󤤪��ɮ�

		try {
			System.out.println("�h���H��...");

			inbox.copyMessages(msg, garbage);
			sent.copyMessages(sent_msg, garbage);
			all.copyMessages(all_msg, garbage);
			//imp.copyMessages(imp_msg, garbage);
			draft.copyMessages(draft_msg, garbage);

			System.out.println("�R���U����P�U���H��...");

			trash_msg = trash.getMessages();
			garbage_msg = garbage.getMessages();

			trash.setFlags(trash_msg, new Flags(Flags.Flag.DELETED), true);
			garbage.setFlags(garbage_msg, new Flags(Flags.Flag.DELETED), true);

			System.out.println("�R������...");
			close();

		} catch (MessagingException e1) {
			e1.printStackTrace();
		}

	}

	private void close() throws MessagingException {

		inbox.close(true);
		sent.close(true);
		all.close(true);
		garbage.close(true);
		//imp.close(true);
		draft.close(true);
		trash.close(true);
		store.close();
	}
}

/*
 * ���綥�q�ϥ� = �C�XGmail�Ҧ��l�ؿ��W�� Folder[] folders =
 * store.getDefaultFolder().list("*"); for (javax.mail.Folder folder : folders)
 * { if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
 * System.out.println(folder.getFullName() + " : " + folder.getMessageCount());
 * } }
 */