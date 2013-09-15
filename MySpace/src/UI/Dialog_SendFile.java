package UI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.mail.internet.AddressException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import File_processing.Share_File;
import System.System_parameters;

public class Dialog_SendFile extends JDialog {
	private static final long serialVersionUID = 1L;
	private JTextField t;
	private String to, in_flag_path;
	private JDialog dialog;
	private JLabel txt2;
	private int index;

	public Dialog_SendFile(String in_flag_path, int index) {

		super(UI_main.main_frame, "傳送檔案", true);
		this.setSize(320, 200);
		this.setLocationRelativeTo(null);
		this.in_flag_path = in_flag_path;
		this.index = index;

		dialog = this;

		JPanel m = new JPanel();
		BoxLayout bout = new BoxLayout(m, BoxLayout.Y_AXIS);
		m.setLayout(bout);

		JPanel hint = new JPanel(new FlowLayout());
		JLabel txt0 = new JLabel("--- 輸入對方MySpace的主要帳號 ---");
		hint.add(txt0);

		JPanel act = new JPanel(new FlowLayout());
		JLabel txt1 = new JLabel("帳號：");
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
				//紀錄傳送位置
				to = t.getText().toString();

				// 寄件人地址
				// 密碼
				// 寄到哪裡
				// 檔案在arraylist路徑位置
				// 檔案在arraylist索引

				try {
					//傳送請求
					Share_File sf = new Share_File(System_parameters.root_act,
							System_parameters.root_psd, to, in_flag_path, index);
					sf.start();
				} catch (AddressException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				txt2.setText("");
				// 寫入xml
				dialog.dispose();

			}
		}
	};
	// 選擇「取消」就關閉Dialog
	ActionListener action2 = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			dialog.dispose();
		}
	};

}
