package UI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.swing.*;
import javax.swing.table.*;
import org.dom4j.*;

import Data.Xml_act;
import Net.Verification;

public class UI_act extends JPanel implements ActionListener {
	public static JPanel ui_act;
	private JTable table;
	private static final long serialVersionUID = 1L;
	private JButton add, back;
	private JTextField t;
	private JPasswordField p;
	private JDialog dialog;
	public static Object[][] columnContext;
	private Object[] columnTitle = { "Root", "�b��" };
	private JScrollPane js;

	public UI_act() throws DocumentException {
		ui_act = this;
		this.setLayout(new FlowLayout(1, 0, 0));
		this.setBackground(Color.white);

		// title (ICON �m�Ϥ� + ��r)
		JPanel jp = new JPanel(new FlowLayout(1, 0, 12));
		jp.setBackground(Color.white);
		JLabel img_label = new JLabel(new ImageIcon("ico_act.fw.png"));
		JLabel txt = new JLabel("   �޲z�b��");
		txt.setFont(new Font("�L�n������", Font.PLAIN, 16));

		jp.add(img_label);
		jp.add(txt);

		// �s�W���s+���s�W���Ϥ�
		add = new JButton(new ImageIcon("add.fw.png"));
		back = new JButton(new ImageIcon("back.fw.png"));

		// �]�w���s�j�p
		add.setPreferredSize(new Dimension(25, 25));
		back.setPreferredSize(new Dimension(25, 25));

		// �s�W���s�ƥ�
		add.addActionListener(this);
		back.addActionListener(this);

		// �]�w���s�j�p
		add.setPreferredSize(new Dimension(25, 25));
		back.setPreferredSize(new Dimension(25, 25));
		new ImageIcon("gar.fw.png");

		// �I�s�ۭq����Axml_rw�A�éI�s���kread_account�^�Ǥ@��2��������}�C�A�䤺�e����Ʈw�Ҧ��b�K
		Xml_act xml = new Xml_act();
		columnContext = xml.read_account();

		table = new JTable(columnContext, columnTitle);

		TableColumn nameColumn = table.getColumn(columnTitle[0]);
		nameColumn.setMinWidth(20);
		nameColumn.setMaxWidth(30);

		js = new JScrollPane(table);
		js.setBackground(Color.white);
		js.setPreferredSize(new Dimension(500, 200));

		add(jp);
		add(Box.createRigidArea(new Dimension(39, 0)));
		add(add);
		add(Box.createRigidArea(new Dimension(8, 0)));
		add(back);
		add(js);

	}

	public void actionPerformed(ActionEvent e) {

		// ���U�u+�v�s�W�b��n�����ʧ@
		if (e.getSource() == add) {

			dialog = new JDialog(UI_main.main_frame, "�s�W�b��", true);
			dialog.setSize(320, 200);
			// dialog.setDefaultCloseOperation(3);
			dialog.setLocationRelativeTo(this.getParent());
			JPanel m = new JPanel();
			BoxLayout bout = new BoxLayout(m, BoxLayout.Y_AXIS);
			m.setLayout(bout);

			JPanel hint = new JPanel(new FlowLayout());
			JLabel txt0 = new JLabel("--- �X�R�A���Ŷ� ---");
			hint.add(txt0);

			JPanel act = new JPanel(new FlowLayout());
			JLabel txt1 = new JLabel("��J�b���G");
			t = new JTextField(15);

			act.add(txt1);
			act.add(t);

			JPanel psd = new JPanel(new FlowLayout());
			JLabel txt2 = new JLabel("��J�K�X�G");
			p = new JPasswordField(15);
			psd.add(txt2);
			psd.add(p);

			JPanel btn = new JPanel(new FlowLayout());
			JButton ok = new JButton("�T�w");
			JButton no = new JButton("����");

			// ��ܡu�T�w�v�N����Ƶ{��xml_rw����add_account��k
			// �ñN��椺�e���s���J�A�ݨϥ�xml_rw����read_account��k��s���e
			// �A�Өϥ�invalidate���sø�s���A�̫�����Dialog
			ActionListener action1 = new ActionListener() {

				// ���ҵL�~���JXML���A�]�w�Ƶ{�ˬd�`�e�q
				// �s�W�b�����e�A�����ϥΪ̷|�R�����t�ΥH�~���l��
				// ��n�J��Root�b���g�J�Ĥ@��b�ᤤ

				public void actionPerformed(ActionEvent e) {
					if (t.getText() != null && p.getPassword().length > 0) {

						char[] psd = p.getPassword();
						String p = "";
						for (char c : psd)
							p += c;
						p = p.trim();
						String act = t.getText().trim();

						
							// ���Ҧ��\
							try {
								try {
									if (Verification.verification(act, p)) {
										Xml_act xml = new Xml_act();
										xml.add_account(act, p);
										columnContext = xml.read_account();
									}
								} catch (DocumentException | IOException e1) {
									JOptionPane.showConfirmDialog(
											UI_act.ui_act,
											"�g�J��Ʈw���~�A�Э��s�ˬd�æb�դ@��!!", "���~",
											JOptionPane.WARNING_MESSAGE);
								}
							} catch (HeadlessException | MessagingException
									| InterruptedException e1) {
								JOptionPane.showConfirmDialog(
										UI_act.ui_act,
										"�b���αK�X���~�A�Э��s�ˬd�æb�դ@��!!", "���~",
										JOptionPane.WARNING_MESSAGE);
							}


						table = new JTable(columnContext, columnTitle);
						TableColumn nameColumn = table
								.getColumn(columnTitle[0]);
						nameColumn.setMinWidth(20);
						nameColumn.setMaxWidth(30);
						js.setViewportView(table);
						dialog.dispose();

					}
				}
			};

			ok.addActionListener(action1);

			// ��ܡu�����v�N����Dialog
			ActionListener action2 = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.dispose();
				}
			};
			no.addActionListener(action2);

			// �N����[�JDialog�M�Ϊ�JPanel
			btn.add(ok);
			btn.add(no);
			m.add(hint);
			m.add(act);
			m.add(psd);
			m.add(btn);
			dialog.add(m);

			dialog.setVisible(true);

		}

		// ���U��^�n���檺�ʧ@
		if (e.getSource() == back) {

			ui_act.setVisible(false);
			// UI_core.ui_core.setVisible(true);

		}

	}

}
