package UI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.dom4j.DocumentException;

import Data.Xml_files;
import System.System_parameters;

public class Dialog_File_Rename extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField t;
	private JDialog dialog;
	private String file_name, id;

	public Dialog_File_Rename(Model_Drive model) {

		super(UI_main.main_frame, "���s�R�W", true);
		this.setSize(320, 200);
		this.setLocationRelativeTo(null);
		dialog = this;


		JPanel m = new JPanel();
		BoxLayout bout = new BoxLayout(m, BoxLayout.Y_AXIS);
		m.setLayout(bout);

		JPanel hint = new JPanel(new FlowLayout());
		JLabel txt0 = new JLabel("--- ���s�R�W ---");
		hint.add(txt0);

		JPanel act = new JPanel(new FlowLayout());
		JLabel txt1 = new JLabel("�ɮצW�١G");
		t = new JTextField(15);

		if (UI_drive.one_index > -1) {

			// ���o�ɮ�ID
			@SuppressWarnings("unchecked")
			ArrayList<Object> files = (ArrayList<Object>) UI_drive.map
					.get(System_parameters.tree_path);
			Object[] file = (Object[]) files.get(UI_drive.one_index);
			id = file[0].toString();

			// ���o�ɮצW��
			file_name = model.getValueAt(UI_drive.one_index, 2).toString();
			t.setText(file_name);
		}

		act.add(txt1);
		act.add(t);

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
		public void actionPerformed(ActionEvent arg0) {

			String new_name = t.getText();

			if (!new_name.isEmpty()) {

				// �ק� XML & Table & ArrayList

				try {

					Xml_files xf = new Xml_files();
					if (xf.rename_FileToXml(id, new_name)) {
						xf.rename_FileToList(UI_drive.one_index, new_name);
						xf.rename_FileToTable(UI_drive.one_index, new_name);
					}
					else{
						System.out.println("���s�R�W����!!");
					}

				} catch (DocumentException | IOException e) {
					e.printStackTrace();
				}
				dialog.dispose();	
			}

		}
	};

	ActionListener action2 = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			dialog.dispose();
		}
	};

}
