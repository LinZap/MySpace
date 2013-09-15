package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;


import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class UI_set extends JDialog {

	private static final long serialVersionUID = 1L;
	public static JDialog ui_set;
	
	public UI_set() {
		super(UI_main.main_frame, "設定", false);
		this.setSize(420, 445);
		this.setLocationRelativeTo(null);
		ui_set = this;
		
		this.setLayout(new FlowLayout(1, 0, 0));
		this.setBackground(Color.white);
		
		JPanel jp = new JPanel(new FlowLayout(1, 5,5));
		jp.setPreferredSize(new Dimension(350, 70));
		jp.setBackground(Color.white);
		JLabel img_label = new JLabel(new ImageIcon("image\\ico_set.fw.png"));
		JLabel txt = new JLabel("關於本系統");
		txt.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		jp.add(img_label);
		jp.add(txt);
		
		add(jp);
		
		JPanel jp2 = new JPanel(new FlowLayout(1, 5,5));
		jp2.setPreferredSize(new Dimension(420, 200));
		jp2.setBackground(Color.white);
		
		
		JLabel img_label2 = new JLabel(new ImageIcon("image\\myIcon.fw.png"));
		JLabel txt2 = new JLabel("MySpace V1.0.0");
		txt2.setPreferredSize(new Dimension(350, 25));
		
		txt2.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		jp2.add(img_label2);
		jp2.add(txt2);
		
		add(jp2);
		
		
		JPanel jp3 = new JPanel(new FlowLayout(1, 5,5));
		jp3.setPreferredSize(new Dimension(420, 100));
		jp3.setBackground(Color.white);
	
		JLabel txt3= new JLabel("版本：version 1.0.0  ");
		JLabel txt4= new JLabel("更新時間：2013/05/15 ");
		JLabel txt5= new JLabel("開發人員信箱：soldierzx0705@gmail.com ");
		txt3.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		txt3.setPreferredSize(new Dimension(350, 25));
		txt4.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		txt4.setPreferredSize(new Dimension(350, 25));
		txt5.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		txt5.setPreferredSize(new Dimension(350, 25));
		jp3.add(txt3);
		jp3.add(txt4);
		jp3.add(txt5);
		
		add(jp3);
		
	
	}

	

}
