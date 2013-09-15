package UI;

import java.awt.*;
import java.awt.event.*;

import javax.mail.MessagingException;
import javax.swing.*;
import Net.Database_Processing;

/**
 * 
 * �o��class�t�d�n�J���� �n�J������4�Ӥ��� �u�{��ICON�v�B�u�b����J���v�B�u�K�X��J���v�B�u�n�J���s�v
 * �n�J�����t�d���ҨϥΪ̨����A��ت��O���F���{���P�_��Ʈw�ƾ� �t�~�n�J�e���|����@�u���ҹq���O�_�s�W�����v���\��
 * 
 * */

public class UI_login extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static JButton btn;

	private JTextField t;
	private JPasswordField p;
	
	public static JPanel ui_login;
	public static JPanel ui_waiting;

	public UI_login() {
		
		ui_waiting = new UI_waiting();
		
		ui_login = this;
		JPanel jp = new JPanel();
		jp.setPreferredSize(new Dimension(400, 460));
		BoxLayout boxlayout2 = new BoxLayout(jp, BoxLayout.Y_AXIS);
		jp.setLayout(boxlayout2);
		jp.setBackground(Color.white);

		
		add(Box.createRigidArea(new Dimension(10, 50)));
		add(jp);
		add(Box.createRigidArea(new Dimension(10, 50)));

		JPanel img_jp = new JPanel(new FlowLayout(1, 0, 20));
		img_jp.setPreferredSize(new Dimension(300, 100));
		img_jp.setBackground(Color.white);

		JLabel icon = new JLabel(new ImageIcon("image\\myIcon.fw.png"));
		img_jp.add(icon);
		jp.add(img_jp);

		
		JPanel form_jp = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
		form_jp.setPreferredSize(new Dimension(300, 160));
		form_jp.setBackground(Color.white);
		
		JLabel txt1 = new JLabel("Gmail �b��");
		t = new JTextField(15);
		
		
		
		form_jp.add(txt1);
		form_jp.add(t);

		JLabel txt2 = new JLabel("Gmail �K�X");
		p = new JPasswordField(15);
		
	
		
		JCheckBox jb = new JCheckBox("�O��n�J��T");
		
		
		form_jp.add(txt2);
		form_jp.add(p);
		
		
		form_jp.add(jb);
		
		
		JPanel inner = new JPanel(new FlowLayout());
		inner.setPreferredSize(new Dimension(0, 20));
		inner.setBackground(Color.white);
		inner.add(form_jp);
		jp.add(inner);

		JPanel btn_jp = new JPanel(new FlowLayout(1, 5, 0));
		btn_jp.setBackground(Color.white);
		
		btn = new JButton("�n�J");
		btn.setFont(new Font("�L�n������", Font.PLAIN, 15));
		btn.addActionListener(this);
		btn_jp.add(btn);
		jp.add(btn_jp);

	}

	public void actionPerformed(ActionEvent e) {
		
		show_ui_waiting();

		if (!t.getText().isEmpty() || p.getPassword().length != 0) {
			
			String act = t.getText().trim();
			char[] psd = p.getPassword();
			
			String p = "";
			
			for (char c : psd) p += c;		
			
				try {
					Database_Processing check = new Database_Processing(act,p.trim());
					check.start();
				} catch (MessagingException e1) {
					e1.printStackTrace();
					hide_ui_waiting();
			}
						
		}
		else{
			JOptionPane.showConfirmDialog(UI_drive.ui_dive, "�b���αK�X��줣�i����",
					"���~", JOptionPane.WARNING_MESSAGE);	
			hide_ui_waiting();
			
		}

	}



	
	private void show_ui_waiting(){
		
		ui_login.setVisible(false);
		UI_main.main_Container.add(ui_waiting);
		ui_waiting.setVisible(true);
		
	}
	private void hide_ui_waiting(){
		
		ui_login.setVisible(true);
		UI_main.main_Container.add(ui_waiting);
		ui_waiting.setVisible(false);
		
	}
	
	

}