package UI;

import java.awt.Dimension;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Dialog_Doing extends JDialog {

	private static final long serialVersionUID = 1L;
	public JDialog dialog;
	public JLabel txt;

	private static JList<String> list;
	public static DefaultListModel<String> liatmodel;
	private JButton btn_hide;
	public static HashMap<String, Object> receive = new HashMap<String, Object>();
	public static HashMap<String, Object> send = new HashMap<String, Object>();

	public Dialog_Doing() {

		super(UI_main.main_frame, "詳細檔案收發訊息", false);
		this.setSize(420, 355);
		this.setLocationRelativeTo(null);
		dialog = this;

		receive.clear();
		send.clear();
		
		JPanel m = new JPanel();
		BoxLayout bout = new BoxLayout(m, BoxLayout.Y_AXIS);
		m.setLayout(bout);

		JPanel jimg = new JPanel(new FlowLayout());
		txt = new JLabel("詳細資訊");
		jimg.add(txt);
		
		liatmodel = new DefaultListModel<String>();
		list = new JList<String>(liatmodel);

		JScrollPane jsp = new JScrollPane(list);
		jsp.setPreferredSize(new Dimension(400, 300));

		jimg.add(jsp);
		
		JPanel btn = new JPanel(new FlowLayout());
		btn_hide = new JButton("隱藏");
		btn_hide.setPreferredSize(new Dimension(190, 25));
		btn.add(btn_hide);
		btn_hide.addActionListener(action1);

		m.add(jimg);
		m.add(btn);
		dialog.add(m);
		dialog.getRootPane().setDefaultButton(null);
	}

	ActionListener action1 = new ActionListener() {

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == btn_hide) {
				dialog.setVisible(false);
				UI_drive.btn_receive.setIcon(new ImageIcon("image\\ceive.fw.png"));
			}

		}
	};

	public static void update_ui() {
		list.updateUI();
	}

	public static void add_element(String s) {
		liatmodel.addElement(s);
	}

	public static void remove_element(int s) {
		liatmodel.removeElementAt(s);
	}

}
