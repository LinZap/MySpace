package Net;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;



public class Net_connect {
	
	/* connect 一個靜態方法
	 * 使用IAMP收信的連線，給予帳號、密碼，可以回傳以下物件繼續往後工作
	 *   Store: 儲存郵件資料夾的物件，但尚未指定取得哪個資料夾
	 * 如果回傳為null則代表 帳密 或 網路 有誤，此時再決定往後工作
	 * */
	public static Store connect(String act , String psd) {
		
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getInstance(props, null);
		
		try {
			Store store = session.getStore();
			store.connect("imap.gmail.com", act, psd);
			return store;
		} catch (MessagingException e) {
			return null;
		}
	}
	
	/* disconnect 一個靜態方法
	 * 連線結束後，給予Store物件，解開連線
	 * */
	public static void disconnect(){
		
	}
	
	
}
