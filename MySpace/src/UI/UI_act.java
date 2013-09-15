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
	private Object[] columnTitle = { "Root", "帳戶" };
	private JScrollPane js;

	public UI_act() throws DocumentException {
		ui_act = this;
		this.setLayout(new FlowLayout(1, 0, 0));
		this.setBackground(Color.white);

		// title (ICON 洨圖片 + 文字)
		JPanel jp = new JPanel(new FlowLayout(1, 0, 12));
		jp.setBackground(Color.white);
		JLabel img_label = new JLabel(new ImageIcon("ico_act.fw.png"));
		JLabel txt = new JLabel("   管理帳戶");
		txt.setFont(new Font("微軟正黑體", Font.PLAIN, 16));

		jp.add(img_label);
		jp.add(txt);

		// 新增按鈕+按鈕上的圖片
		add = new JButton(new ImageIcon("add.fw.png"));
		back = new JButton(new ImageIcon("back.fw.png"));

		// 設定按鈕大小
		add.setPreferredSize(new Dimension(25, 25));
		back.setPreferredSize(new Dimension(25, 25));

		// 新增按鈕事件
		add.addActionListener(this);
		back.addActionListener(this);

		// 設定按鈕大小
		add.setPreferredSize(new Dimension(25, 25));
		back.setPreferredSize(new Dimension(25, 25));
		new ImageIcon("gar.fw.png");

		// 呼叫自訂物件，xml_rw，並呼叫其方法read_account回傳一個2維的物件陣列，其內容為資料庫所有帳密
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

		// 按下「+」新增帳戶要做的動作
		if (e.getSource() == add) {

			dialog = new JDialog(UI_main.main_frame, "新增帳戶", true);
			dialog.setSize(320, 200);
			// dialog.setDefaultCloseOperation(3);
			dialog.setLocationRelativeTo(this.getParent());
			JPanel m = new JPanel();
			BoxLayout bout = new BoxLayout(m, BoxLayout.Y_AXIS);
			m.setLayout(bout);

			JPanel hint = new JPanel(new FlowLayout());
			JLabel txt0 = new JLabel("--- 擴充你的空間 ---");
			hint.add(txt0);

			JPanel act = new JPanel(new FlowLayout());
			JLabel txt1 = new JLabel("輸入帳號：");
			t = new JTextField(15);

			act.add(txt1);
			act.add(t);

			JPanel psd = new JPanel(new FlowLayout());
			JLabel txt2 = new JLabel("輸入密碼：");
			p = new JPasswordField(15);
			psd.add(txt2);
			psd.add(p);

			JPanel btn = new JPanel(new FlowLayout());
			JButton ok = new JButton("確定");
			JButton no = new JButton("取消");

			// 選擇「確定」就執行副程式xml_rw中的add_account方法
			// 並將表格內容重新載入，需使用xml_rw中的read_account方法更新內容
			// 再來使用invalidate重新繪製表格，最後關閉Dialog
			ActionListener action1 = new ActionListener() {

				// 驗證無誤後放入XML中，設定排程檢查總容量
				// 新增帳號之前，提醒使用者會刪除本系統以外的郵件
				// 把登入的Root帳號寫入第一支帳戶中

				public void actionPerformed(ActionEvent e) {
					if (t.getText() != null && p.getPassword().length > 0) {

						char[] psd = p.getPassword();
						String p = "";
						for (char c : psd)
							p += c;
						p = p.trim();
						String act = t.getText().trim();

						
							// 驗證成功
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
											"寫入資料庫錯誤，請重新檢查並在試一次!!", "錯誤",
											JOptionPane.WARNING_MESSAGE);
								}
							} catch (HeadlessException | MessagingException
									| InterruptedException e1) {
								JOptionPane.showConfirmDialog(
										UI_act.ui_act,
										"帳號或密碼錯誤，請重新檢查並在試一次!!", "錯誤",
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

			// 選擇「取消」就關閉Dialog
			ActionListener action2 = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.dispose();
				}
			};
			no.addActionListener(action2);

			// 將元件加入Dialog專用的JPanel
			btn.add(ok);
			btn.add(no);
			m.add(hint);
			m.add(act);
			m.add(psd);
			m.add(btn);
			dialog.add(m);

			dialog.setVisible(true);

		}

		// 按下返回要執行的動作
		if (e.getSource() == back) {

			ui_act.setVisible(false);
			// UI_core.ui_core.setVisible(true);

		}

	}

}
