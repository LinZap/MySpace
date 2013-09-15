package UI;

import java.awt.Container;
import javax.swing.*;

/**
 * 這個class是所有介面的基底
 * 負責實作主要的JFrame與它的Container
 * 
 * 如此一來
 * 所有介面一率直接繼承 "JPanel" 撰寫各自的 "排版" 、 "內容" 與 "功能"
 * 
 * 
 * 當介面彼此需要切換時： 
 * 1. 先將目前顯示的JPanel隱藏 setVisible(false)
 * 2. 將要顯示的介面加入Container 然後調用 setVisible(true)作為切換
 * 
 * */

public class UI_main extends JFrame {

	private static final long serialVersionUID = 1L;
	public static JFrame main_frame;
	public static Container main_Container;
	public static JPanel  UI_login;
	
	public UI_main() {
		main_frame = this;
		main_Container = getContentPane();		
		UI_login = new UI_login();
		main_Container.add(UI_login);
		UI_login.setVisible(true);		
	}
}