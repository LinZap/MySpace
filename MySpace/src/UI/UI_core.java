package UI;


/**
 * 
 * 這個class負責主介面
 * 主介面 有3個主要按鈕 「雲端空間」、「帳戶管理」、「偏好設定」
 * 主畫面負責呼叫3個按鈕各自的介面，是一個導引的介面
 * 
 * */

public class UI_core  {
	/*
	private JButton dive, act, set;
	public static JPanel ui_core, ui_dive, ui_act, ui_set;
	public static ImageIcon img_dive, img_act, img_set, img_line;
	private static final long serialVersionUID = 1L;

	public UI_core() throws DocumentException {
		ui_core = this;
		// 畫面設定
		this.setLayout(new FlowLayout(1, 0, 45));
		this.setBackground(Color.white);

		// 設置按鈕上面的圖片資源
		img_dive = new ImageIcon("dive.fw.png");
		img_act = new ImageIcon("act.fw.png");
		img_set = new ImageIcon("set.fw.png");
		img_line = new ImageIcon("line.fw.png");

		// 基本元件
		JLabel line1 = new JLabel(img_line);
		JLabel line2 = new JLabel(img_line);
		dive = new RButton("雲端檔案", img_dive);
		act = new RButton("管理空間", img_act);
		set = new RButton("偏好設定", img_set);
		dive.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		act.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		set.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		

		// 設置按鈕大小
		dive.setPreferredSize(new Dimension(150, 270));
		act.setPreferredSize(new Dimension(150, 270));
		set.setPreferredSize(new Dimension(150, 270));
		
		 //內容置中，按鈕圖文設置垂直排列
		dive.setHorizontalTextPosition(SwingConstants.CENTER);
		dive.setVerticalTextPosition(SwingConstants.BOTTOM);
		act.setHorizontalTextPosition(SwingConstants.CENTER);
		act.setVerticalTextPosition(SwingConstants.BOTTOM);
		set.setHorizontalTextPosition(SwingConstants.CENTER);
		set.setVerticalTextPosition(SwingConstants.BOTTOM);

		
		// 將元件新增到畫面( 按鈕1 + 間格線 + 按鈕2 + 間隔線 + 按鈕3 )
		add(dive);
		add(line1);
		add(act);
		add(line2);
		add(set);

		// 新增每個按鈕的觸發事件
		dive.addActionListener(this);
		act.addActionListener(this);
		set.addActionListener(this);

		
		//進入主畫面後，立刻實作3個子畫面
		// 實作完後放入main_Container，但暫時先隱藏它們
		// 這些畫面等待3個按鈕的調用
		ui_dive = new UI_drive();
		ui_act = new UI_act();
		ui_set = new UI_set();
		
	
	

	}

	// 這邊實作3個按鈕該負責的功能
	public void actionPerformed(ActionEvent e) {

		// 呼叫UI_dive(雲端空間)介面，並隱藏目前的介面
		if (e.getSource() == dive) {
			setVisible(false);
			UI_main.main_Container.add(ui_dive);
			ui_dive.setVisible(true);
			
		}

		// 呼叫UI_act(帳戶管理)介面，並隱藏目前的介面
		if (e.getSource() == act) {
			setVisible(false);
			UI_main.main_Container.add(ui_act);
			ui_act.setVisible(true);
		
		}

		// 呼叫UI_set(偏好設定)介面，並隱藏目前的介面
		if (e.getSource() == set) {
			setVisible(false);
			UI_main.main_Container.add(ui_set);
			ui_set.setVisible(true);
		
		}

	}*/
}