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
	
	
	// 在arraylist中的索引
	private int flag;
	// 從arraylist中抓出資料的路徑
	private String in_flag_path;
	// 寄件人帳號,密碼,欲寄送的檔案名稱,檔案大小,信件內容,現在的時間
	private String act, psd, fileName,size,body="",now;
	// 寄送到哪個位置
	private Address[] address;
	// 信件主旨，也就是請求
	private final String subject = "[MySpace]Request";
	
	
	
	public Share_File(String act, String psd, String to, String path, int index ) throws AddressException{
		
		this.act= act;
		this.psd = psd;
		in_flag_path = path;
		flag = index;
		address = new Address[1];
		address[0] = new InternetAddress(to);
		
		//現在時間
		Date d = new Date();
		SimpleDateFormat fm1 = new SimpleDateFormat("yyyyMMddHHmmss");
		now = fm1.format(d);
		
		@SuppressWarnings("unchecked")
		ArrayList<Object> files = (ArrayList<Object>) UI_drive.map.get(in_flag_path);
		Object[] file = (Object[]) files.get(flag);
		
		//取得檔名
		fileName = file[2].toString();
		//取得檔案大小
		size = file[3].toString();
		//設定body
		body+= fileName + "," + size + "," + now;

		
		
		//新增到資料庫
		
		Object[] data = new Object[7];
		// 檔名 0
		data[0] = fileName;
		// 大小 1
		data[1] = size;
		// 從何而來 2 
		data[2] = address;
		// 發送[MySpace]Send了沒 3 
		data[3] = false;
		// 檔案路徑 4 
		data[4] = in_flag_path;
		// 檔案資料庫索引 5 
		System.out.println("新增到資料庫的flag是 "+flag);
		data[5] = String.valueOf(flag);
		
		// 在ui中的index 6  
		// 新增到介面中顯示
		data[6] = add_req(data);
		
		//放入資料庫
		Dialog_Doing.send.put(now, data);

		
		System.out.println("新增資料到資料庫 , 路徑"+ now);
		
		
	}
	
	public void run() {


		//傳送[MySpace]Requset
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
	
	// 新增到資料庫
	private int add_req(Object[] request) {
		
		Address[] a = (Address[]) request[2];
		
		String show = " 正在準備寄送一個檔案到" + a[0] + "，檔案名稱："
				+ request[0] + " ，檔案大小：" + request[1] + "byte";
						
		
		
		Dialog_Doing.liatmodel.addElement(show);
		int index = Dialog_Doing.liatmodel.size();
		
		return index - 1;

	}

	
	
}
