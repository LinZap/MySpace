package Net;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.TimerTask;

import javax.mail.MessagingException;

import System.System_parameters;

public class Database_backup extends TimerTask  {

	public void run() {

		
		System.out.println("�}�l��Ʈw�����T�{�A���ק�|�ƥ�!");
		
		// ��Ʈw�ɮ�
		File[] db_file = { new File(System_parameters.get_db_path(0)),
				new File(System_parameters.get_db_path(1)),
				new File(System_parameters.get_db_path(2)) };

		// �j���ˬd��Ʈw����
		for (int i = 0; i < db_file.length; i++) {

			// ���p��Ʈw�w�g�Q�ק�L�A�h�R�����ݸ�Ʈw��W��
			if (db_file[i].lastModified() > System_parameters.db_modify[i]) {

				try {
					// �����̷s���ק�ɶ�
					System_parameters.db_modify[i] = db_file[i].lastModified();

					// �R����Ʈw
					IMAP_Delete.do_delete(
							System_parameters.root_act,
							System_parameters.root_psd,
							System_parameters.db_search[i]);
				
					// �H�X��Ʈw
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
