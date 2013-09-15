package UI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.tree.*;

import org.dom4j.DocumentException;

import Data.Xml_dir;

public class Dialog_Dir_Name extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField t;
	private String dir_name;
	private JDialog dialog;
	private TreePath p;
	private DefaultMutableTreeNode n;
	private JLabel txt2;
	private JTree tree;

	public Dialog_Dir_Name(TreePath p, JTree tree) {

		super(UI_main.main_frame, "�s�W�ؿ�", true);
		this.setSize(320, 200);
		this.setLocationRelativeTo(null);

		dialog = this;

		this.p = p;
		this.tree = tree;

		n = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		JPanel m = new JPanel();
		BoxLayout bout = new BoxLayout(m, BoxLayout.Y_AXIS);
		m.setLayout(bout);

		JPanel hint = new JPanel(new FlowLayout());
		JLabel txt0 = new JLabel("--- �إߤ@�ӥؿ� ---");
		hint.add(txt0);

		JPanel act = new JPanel(new FlowLayout());
		JLabel txt1 = new JLabel("�ؿ��W�١G");
		t = new JTextField(15);
		txt2 = new JLabel("");

		act.add(txt1);
		act.add(t);
		act.add(txt2);

		JPanel btn = new JPanel(new FlowLayout());
		JButton ok = new JButton("�T�w");
		JButton no = new JButton("����");

		ok.addActionListener(action1);
		no.addActionListener(action2);

		// �N����[�JDialog�M�Ϊ�JPanel
		btn.add(ok);
		btn.add(no);
		m.add(hint);
		m.add(act);
		m.add(btn);
		dialog.add(m);
		dialog.setVisible(true);

	}

	ActionListener action1 = new ActionListener() {
		public void actionPerformed(ActionEvent e) {

			if (!t.getText().isEmpty()) {
				dir_name = t.getText().toString();

				// �ˬd�ɦW�O�_������
				// �p�G�P�ؿ��U�S���ۦP�W�٪��ؿ�

				if (check_same())
					txt2.setText("�W�٭��ơA���s�R�W!!");

				else {

					txt2.setText(""); // �g�Jxml
					try {
						Xml_dir d = new Xml_dir();

						// p �ثe�ҿ諸���| // dir_name �ɮצW�� // n �ثe��ܪ��`�I // tree
						// �t�Ϊ�JTree

						d.add_dir(p, dir_name, n, tree);

					} catch (DocumentException | IOException e1) {
						e1.printStackTrace();
						System.out.println("�g�J����");
					}

					dialog.dispose();

				}

			}
		}
	};

	// �ˬd�ɦW�O�_������
	private boolean check_same() {
		boolean same = false;
		for (int i = 0; i < n.getChildCount(); i++)
			if (n.getChildAt(i).toString().equals(dir_name)) {
				same = true;
				break;
			}
		return same;
	}

	// ��ܡu�����v�N����Dialog
	ActionListener action2 = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			dialog.dispose();
		}
	};

	public String getDirName() {
		return dir_name;
	}

}
