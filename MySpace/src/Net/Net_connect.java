package Net;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;



public class Net_connect {
	
	/* connect �@���R�A��k
	 * �ϥ�IAMP���H���s�u�A�����b���B�K�X�A�i�H�^�ǥH�U�����~�򩹫�u�@
	 *   Store: �x�s�l���Ƨ�������A���|�����w���o���Ӹ�Ƨ�
	 * �p�G�^�Ǭ�null�h�N�� �b�K �� ���� ���~�A���ɦA�M�w����u�@
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
	
	/* disconnect �@���R�A��k
	 * �s�u������A����Store����A�Ѷ}�s�u
	 * */
	public static void disconnect(){
		
	}
	
	
}
