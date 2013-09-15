package Net;

import java.util.Properties;
import javax.mail.*;

import com.sun.mail.imap.IMAPFolder;
/*
 * 這個副程式主要負責收信 
 * 然後總和這些信件的大小並回傳，以進行判斷
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
	// 收所有信件的表頭，並回傳他們總大小，以計算該帳號的已使用空間
	//計算使用空間大小
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
