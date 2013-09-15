package Net;

import java.util.Properties;
import javax.mail.*;

import com.sun.mail.imap.IMAPFolder;
/*
 * �o�ӰƵ{���D�n�t�d���H 
 * �M���`�M�o�ǫH�󪺤j�p�æ^�ǡA�H�i��P�_
 * 
 **/
public class Used_space {
	private String act, psd;
	private IMAPFolder folder;
	private Store store;

	public Used_space(String act, String psd) {
		this.act = act + "@gmail.com";
		this.psd = psd;
	}
	// ���Ҧ��H�󪺪��Y�A�æ^�ǥL���`�j�p�A�H�p��ӱb�����w�ϥΪŶ�
	//�p��ϥΪŶ��j�p
	public int count() throws MessagingException {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);
		store = session.getStore();
		store.connect("imap.gmail.com", act, psd);
		folder = (IMAPFolder) store.getFolder("Inbox");
		folder.open(Folder.READ_ONLY);
		int sum = 0;
		Message messages[] = folder.getMessages();
		
		for (Message m : messages) 
			sum += m.getSize();	
		//System.out.println("used:"+folde);
		folder.close(true);
		store.close();
		return sum;
	}
}
