package File_processing;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import Net.Send_Msg;
import UI.Dialog_Doing;
import UI.UI_drive;

public class Share_File extends Thread {
	
	
	// �barraylist��������
	private int flag;
	// �qarraylist����X��ƪ����|
	private String in_flag_path;
	// �H��H�b��,�K�X,���H�e���ɮצW��,�ɮפj�p,�H�󤺮e,�{�b���ɶ�
	private String act, psd, fileName,size,body="",now;
	// �H�e����Ӧ�m
	private Address[] address;
	// �H��D���A�]�N�O�ШD
	private final String subject = "[MySpace]Request";
	
	
	
	public Share_File(String act, String psd, String to, String path, int index ) throws AddressException{
		
		this.act= act;
		this.psd = psd;
		in_flag_path = path;
		flag = index;
		address = new Address[1];
		address[0] = new InternetAddress(to);
		
		//�{�b�ɶ�
		Date d = new Date();
		SimpleDateFormat fm1 = new SimpleDateFormat("yyyyMMddHHmmss");
		now = fm1.format(d);
		
		@SuppressWarnings("unchecked")
		ArrayList<Object> files = (ArrayList<Object>) UI_drive.map.get(in_flag_path);
		Object[] file = (Object[]) files.get(flag);
		
		//���o�ɦW
		fileName = file[2].toString();
		//���o�ɮפj�p
		size = file[3].toString();
		//�]�wbody
		body+= fileName + "," + size + "," + now;

		
		
		//�s�W���Ʈw
		
		Object[] data = new Object[7];
		// �ɦW 0
		data[0] = fileName;
		// �j�p 1
		data[1] = size;
		// �q��Ө� 2 
		data[2] = address;
		// �o�e[MySpace]Send�F�S 3 
		data[3] = false;
		// �ɮ׸��| 4 
		data[4] = in_flag_path;
		// �ɮ׸�Ʈw���� 5 
		System.out.println("�s�W���Ʈw��flag�O "+flag);
		data[5] = String.valueOf(flag);
		
		// �bui����index 6  
		// �s�W�줶�������
		data[6] = add_req(data);
		
		//��J��Ʈw
		Dialog_Doing.send.put(now, data);

		
		System.out.println("�s�W��ƨ��Ʈw , ���|"+ now);
		
		
	}
	
	public void run() {


		//�ǰe[MySpace]Requset
		Send_Msg smsg = null;
		try {
			smsg = new Send_Msg(act, psd, body, subject, address);
		} catch (UnsupportedEncodingException | MessagingException
				| InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		smsg.start();
		
		
	}
	
	// �s�W���Ʈw
	private int add_req(Object[] request) {
		
		Address[] a = (Address[]) request[2];
		
		String show = " ���b�ǳƱH�e�@���ɮר�" + a[0] + "�A�ɮצW�١G"
				+ request[0] + " �A�ɮפj�p�G" + request[1] + "byte";
						
		
		
		Dialog_Doing.liatmodel.addElement(show);
		int index = Dialog_Doing.liatmodel.size();
		
		return index - 1;

	}

	
	
}
