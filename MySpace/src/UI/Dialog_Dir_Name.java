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

		super(UI_main.main_frame, "新增目錄", true);
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
		JLabel txt0 = new JLabel("--- 建立一個目錄 ---");
		hint.add(txt0);

		JPanel act = new JPanel(new FlowLayout());
		JLabel txt1 = new JLabel("目錄名稱：");
		t = new JTextField(15);
		txt2 = new JLabel("");

		act.add(txt1);
		act.add(t);
		act.add(txt2);

		JPanel btn = new JPanel(new FlowLayout());
		JButton ok = new JButton("確定");
		JButton no = new JButton("取消");

		ok.addActionListener(action1);
		no.addActionListener(action2);

		// 將元件加入Dialog專用的JPanel
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

				// 檢查檔名是否有重複
				// 如果同目錄下沒有相同名稱的目錄

				if (check_same())
					txt2.setText("名稱重複，重新命名!!");

				else {

					txt2.setText(""); // 寫入xml
					try {
						Xml_dir d = new Xml_dir();

						// p 目前所選的路徑 // dir_name 檔案名稱 // n 目前選擇的節點 // tree
						// 系統的JTree

						d.add_dir(p, dir_name, n, tree);

					} catch (DocumentException | IOException e1) {
						e1.printStackTrace();
						System.out.println("寫入失敗");
					}

					dialog.dispose();

				}

			}
		}
	};

	// 檢查檔名是否有重複
	private boolean check_same() {
		boolean same = false;
		for (int i = 0; i < n.getChildCount(); i++)
			if (n.getChildAt(i).toString().equals(dir_name)) {
				same = true;
				break;
			}
		return same;
	}

	// 選擇「取消」就關閉Dialog
	ActionListener action2 = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			dialog.dispose();
		}
	};

	public String getDirName() {
		return dir_name;
	}

}
