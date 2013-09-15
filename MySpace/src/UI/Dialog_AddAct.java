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

		super(UI_main.main_frame, "擴充空間", true);
		setSize(340, 260);
		setLocationRelativeTo(null);
		JPanel m = new JPanel();
		BoxLayout bout = new BoxLayout(m, BoxLayout.Y_AXIS);
		m.setLayout(bout);

		JPanel hint = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 3));
		JLabel txt0 = new JLabel("輸入您新註冊的Gmail帳戶");
		JLabel txt3 = new JLabel("注意 ! 該信箱中  非本系統  的郵件將會被全部刪除 !");
		
		txt3.setForeground(Color.red);
		hint.add(txt0);
		hint.add(txt3);

		
		JPanel act = new JPanel(new FlowLayout());
		JLabel txt1 = new JLabel("帳號：");
		t = new JTextField(15);

		act.add(txt1);
		act.add(t);

		JPanel psd = new JPanel(new FlowLayout());
		JLabel txt2 = new JLabel("密碼：");
		p = new JPasswordField(15);
		psd.add(txt2);
		psd.add(p);

		JPanel btn = new JPanel(new FlowLayout());
		add = new JButton("確定");
		no = new JButton("取消");

		add.addActionListener(this);
		no.addActionListener(this);

		// 將元件加入Dialog專用的JPanel
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
		// 選擇「確定」就執行副程式xml_rw中的add_account方法
		// 並將表格內容重新載入，需使用xml_rw中的read_account方法更新內容
		// 再來使用invalidate重新繪製表格，最後關閉Dialog
		// 驗證無誤後放入XML中，設定排程檢查總容量
		// 新增帳號之前，提醒使用者會刪除本系統以外的郵件
		// 把登入的Root帳號寫入第一支帳戶中
		if (e.getSource() == add) {
			this.setTitle("擴充空間 - 請稍候...");
			if (t.getText() != null && p.getPassword().length > 0) {
				
				
				
				char[] psd = p.getPassword();
				String p = "";
				for (char c : psd)
					p += c;
				p = p.trim();
				String act = t.getText().trim();

				try {
					// 驗證成功
					try {
						
						if (Verification.verification(act, p)) {
							
							Xml_act xml = new Xml_act();
							xml.add_account(act, p);
							// columnContext = xml.read_account();
							
							// 清空非本系統的信件
							Mail_Clear mc = new Mail_Clear(act, p);
							mc.start();
							
							JOptionPane.showConfirmDialog(UI_act.ui_act,
									"空間成功擴充!!", "成功", JOptionPane.PLAIN_MESSAGE);
							this.setTitle("擴充空間 ");
							dispose();
						}
					} catch (HeadlessException | InterruptedException e1) {
						JOptionPane.showConfirmDialog(UI_act.ui_act, "系統連線出現錯誤，請在試一次或檢查網路連線情況...",
								"連線錯誤", JOptionPane.WARNING_MESSAGE);
					}

				} catch (DocumentException | IOException | MessagingException e1) {
					JOptionPane.showConfirmDialog(UI_act.ui_act, "系統連線出現錯誤，請在試一次或檢查網路連線情況...",
							"連線錯誤", JOptionPane.WARNING_MESSAGE);
				}
				this.setTitle("擴充空間 ");

			}

		}

		else if (e.getSource() == no) {
			this.setTitle("擴充空間");
			dispose();
		}

	}
}
