package Net;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.SubjectTerm;
import javax.swing.JOptionPane;

import System.System_parameters;
import UI.UI_act;

import com.sun.mail.imap.IMAPFolder;

public class Verification {

	public static boolean verification(String act, String psd)
			throws MessagingException, UnsupportedEncodingException, InterruptedException {

		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getInstance(props, null);
		Store store = session.getStore();

		try {

			store.connect("imap.gmail.com", act, psd);
		}

		catch (Exception e) {
			JOptionPane.showConfirmDialog(UI_act.ui_act,
					"�b���αK�X���~�A�Э��s�ˬd�æb�դ@��!!", "���~",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
			IMAPFolder inbox = (IMAPFolder) store.getFolder("Inbox");
			inbox.open(Folder.READ_WRITE);
			// ��OR�j�M�A�N�����ɦW���������A����o�Ƕl��
			SubjectTerm s = new SubjectTerm("[MySpace]Sub_Act");
			// �L�o�X�l��
			Message[] msg = inbox.search(s);

			// �p�G������ѧO�l��
			if (msg.length > 0) {

				//��ѫH��D���r�� [MySpace]Sub_Act + �ť� + ���ݪ��b�� 
				String[] part_of = msg[0].getSubject().split(" ");
				
				// �q���ϥΪ̳Q���ӱb���[�J�L  part_of[1]
				JOptionPane.showConfirmDialog(UI_act.ui_act, "�z�s�W���b���w�g�Q "
						+ part_of[1] + " �[�J�L�A�L�k�A�Q�[�J...", "�Ŷ�����Q���ƥ[�J!!",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
			
			// �S������ѧO�l��A���W�ۤv�H�e�@���ѧO�l��i�h
			else {
				
				//�ѧO�r��A�]�w�䬰�H��subject
				String subject = "[MySpace]Sub_Act " + System_parameters.root_act;
				Address[] to = { new InternetAddress(act) };
				Send_Msg smg = new Send_Msg(act, psd, " ", subject, to);
				smg.start();
				return true;
			}

		

	}

}
