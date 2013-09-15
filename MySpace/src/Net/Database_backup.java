package Net;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.TimerTask;

import javax.mail.MessagingException;

import System.System_parameters;

public class Database_backup extends TimerTask  {

	public void run() {

		
		System.out.println("開始資料庫版本確認，有修改會備份!");
		
		// 資料庫檔案
		File[] db_file = { new File(System_parameters.get_db_path(0)),
				new File(System_parameters.get_db_path(1)),
				new File(System_parameters.get_db_path(2)) };

		// 迴圈檢查資料庫版本
		for (int i = 0; i < db_file.length; i++) {

			// 假如資料庫已經被修改過，則刪除遠端資料庫後上傳
			if (db_file[i].lastModified() > System_parameters.db_modify[i]) {

				try {
					// 紀錄最新的修改時間
					System_parameters.db_modify[i] = db_file[i].lastModified();

					// 刪除資料庫
					IMAP_Delete.do_delete(
							System_parameters.root_act,
							System_parameters.root_psd,
							System_parameters.db_search[i]);
				
					// 寄出資料庫
					Database_Mailer dm = new Database_Mailer();
					dm.do_send(System_parameters.get_db_path(i));
					dm.start();
					dm.join();

				} catch (MessagingException | InterruptedException
						| UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
