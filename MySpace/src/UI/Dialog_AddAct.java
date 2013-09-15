package UI;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.dom4j.DocumentException;

import Data.Xml_act;
import Net.Mail_Clear;
import Net.Verification;

public class Dialog_AddAct extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton add, no;
	private JTextField t;
	private JPasswordField p;


	public Dialog_AddAct() {

		super(UI_main.main_frame, "�X�R�Ŷ�", true);
		setSize(340, 260);
		setLocationRelativeTo(null);
		JPanel m = new JPanel();
		BoxLayout bout = new BoxLayout(m, BoxLayout.Y_AXIS);
		m.setLayout(bout);

		JPanel hint = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 3));
		JLabel txt0 = new JLabel("��J�z�s���U��Gmail�b��");
		JLabel txt3 = new JLabel("�`�N ! �ӫH�c��  �D���t��  ���l��N�|�Q�����R�� !");
		
		txt3.setForeground(Color.red);
		hint.add(txt0);
		hint.add(txt3);

		
		JPanel act = new JPanel(new FlowLayout());
		JLabel txt1 = new JLabel("�b���G");
		t = new JTextField(15);

		act.add(txt1);
		act.add(t);

		JPanel psd = new JPanel(new FlowLayout());
		JLabel txt2 = new JLabel("�K�X�G");
		p = new JPasswordField(15);
		psd.add(txt2);
		psd.add(p);

		JPanel btn = new JPanel(new FlowLayout());
		add = new JButton("�T�w");
		no = new JButton("����");

		add.addActionListener(this);
		no.addActionListener(this);

		// �N����[�JDialog�M�Ϊ�JPanel
		btn.add(add);
		btn.add(no);
		m.add(hint);
		m.add(act);
		m.add(psd);
		m.add(btn);
		add(m);

		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// ��ܡu�T�w�v�N����Ƶ{��xml_rw����add_account��k
		// �ñN��椺�e���s���J�A�ݨϥ�xml_rw����read_account��k��s���e
		// �A�Өϥ�invalidate���sø�s���A�̫�����Dialog
		// ���ҵL�~���JXML���A�]�w�Ƶ{�ˬd�`�e�q
		// �s�W�b�����e�A�����ϥΪ̷|�R�����t�ΥH�~���l��
		// ��n�J��Root�b���g�J�Ĥ@��b�ᤤ
		if (e.getSource() == add) {
			this.setTitle("�X�R�Ŷ� - �еy��...");
			if (t.getText() != null && p.getPassword().length > 0) {
				
				
				
				char[] psd = p.getPassword();
				String p = "";
				for (char c : psd)
					p += c;
				p = p.trim();
				String act = t.getText().trim();

				try {
					// ���Ҧ��\
					try {
						
						if (Verification.verification(act, p)) {
							
							Xml_act xml = new Xml_act();
							xml.add_account(act, p);
							// columnContext = xml.read_account();
							
							// �M�ūD���t�Ϊ��H��
							Mail_Clear mc = new Mail_Clear(act, p);
							mc.start();
							
							JOptionPane.showConfirmDialog(UI_act.ui_act,
									"�Ŷ����\�X�R!!", "���\", JOptionPane.PLAIN_MESSAGE);
							this.setTitle("�X�R�Ŷ� ");
							dispose();
						}
					} catch (HeadlessException | InterruptedException e1) {
						JOptionPane.showConfirmDialog(UI_act.ui_act, "�t�γs�u�X�{���~�A�Цb�դ@�����ˬd�����s�u���p...",
								"�s�u���~", JOptionPane.WARNING_MESSAGE);
					}

				} catch (DocumentException | IOException | MessagingException e1) {
					JOptionPane.showConfirmDialog(UI_act.ui_act, "�t�γs�u�X�{���~�A�Цb�դ@�����ˬd�����s�u���p...",
							"�s�u���~", JOptionPane.WARNING_MESSAGE);
				}
				this.setTitle("�X�R�Ŷ� ");

			}

		}

		else if (e.getSource() == no) {
			this.setTitle("�X�R�Ŷ�");
			dispose();
		}

	}
}
