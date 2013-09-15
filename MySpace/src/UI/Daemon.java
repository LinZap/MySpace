package UI;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
/**
 * 
 * �`�n�{���A���ն��q�A�|����@
 * 
 * */
public class Daemon {
	// icon object
	private TrayIcon trayIcon;
	// SystemTray object
	private SystemTray tray = null;
	// ���o�ϥ�,�p�G�䤣���,�b�t�ΦC�W�|�O�ťժ�
	private Image image = Toolkit.getDefaultToolkit().getImage("image\\cat3.fw.png");//�ϭn��b��Ƨ�
	// ���X�����
	private PopupMenu popup = new PopupMenu();// �u�X�����
	private MenuItem openItem = null;
	private MenuItem optionItem = null;
	private MenuItem aboutItem = null;
	private MenuItem exitItem = null;

	public Daemon() {
		// �ˬdOS�O�_�䴩SystemTray
		if (SystemTray.isSupported()) {

			// �C��Java�{���u�঳�@��SystemTray����
			tray = SystemTray.getSystemTray();

			// �]�w���
			setMenu();

			// �]�wtrayIcon(�Ϥ�, �ƹ����W�h��Tip�T��, ���)
			trayIcon = new TrayIcon(image, "MySpace", popup);

			// �]�w�ϥܦ۰��ܧ�ؤo
			trayIcon.setImageAutoSize(true);

			try {
				// ��trayIcon�[�Jtray��
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println("TrayIcon could not be added.");
			}

			// �]�w�ƥ�
			setEvent();
		} else {
			JOptionPane.showMessageDialog(null, "SystemTray not support!");
		}
	}

	private void setMenu() {
		// �[�J���
		openItem = new MenuItem("�}�Ҩt�ΥD�ɭ�");
		optionItem = new MenuItem("�ﶵ");
		aboutItem = new MenuItem("���󥻵{��");
		exitItem = new MenuItem("���}");

		popup.add(openItem);
		popup.add(optionItem);
		popup.add(aboutItem);
		// �[�J���j�u
		popup.addSeparator();
		popup.add(exitItem);
	}

	private void setEvent() {
		// �]�w�ƹ��ƥ��ť��
		MouseListener mouseListener = new MouseListener() {
			// �I��
			public void mouseClicked(MouseEvent e) {
				System.out.println("click");//�}�_�D�{��
			}
			// �i�J
			public void mouseEntered(MouseEvent e) {
			}
			// ���}
			public void mouseExited(MouseEvent e) {
			}
			//��2�U
			public void mousePressed(MouseEvent e) { 
	            
				System.out.println("mousePressed");//�}�_�D�{��
			}
			//��}
			public void mouseReleased(MouseEvent e) {
			}
		};

		// openItem���s�ƥ�
		ActionListener openListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "�}�Ҩt�ΥD�ɭ�");//GGC�]��
			}
		};

		// optionItem���s�ƥ�
		ActionListener optionListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "�}�ҿﶵ");//
			}
		};

		// aboutItem���s�ƥ�
		ActionListener aboutListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null,"���y�ڬO�}��");//���󥻵{��
			}
		};

		// aboutItem���s�ƥ�
		ActionListener exitListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);//���}
			}
		};

		// �[�J�ƥ�
		openItem.addActionListener(openListener);
		optionItem.addActionListener(optionListener);
		aboutItem.addActionListener(aboutListener);
		exitItem.addActionListener(exitListener);
		trayIcon.addMouseListener(mouseListener);
	}

}
