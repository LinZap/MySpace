package UI;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
/**
 * 
 * 常駐程式，測試階段，尚未實作
 * 
 * */
public class Daemon {
	// icon object
	private TrayIcon trayIcon;
	// SystemTray object
	private SystemTray tray = null;
	// 取得圖示,如果找不到圖,在系統列上會是空白的
	private Image image = Toolkit.getDefaultToolkit().getImage("image\\cat3.fw.png");//圖要放在資料夾
	// 跳出式選單
	private PopupMenu popup = new PopupMenu();// 彈出式菜單
	private MenuItem openItem = null;
	private MenuItem optionItem = null;
	private MenuItem aboutItem = null;
	private MenuItem exitItem = null;

	public Daemon() {
		// 檢查OS是否支援SystemTray
		if (SystemTray.isSupported()) {

			// 每個Java程式只能有一個SystemTray實體
			tray = SystemTray.getSystemTray();

			// 設定選單
			setMenu();

			// 設定trayIcon(圖片, 滑鼠指上去的Tip訊息, 選單)
			trayIcon = new TrayIcon(image, "MySpace", popup);

			// 設定圖示自動變更尺寸
			trayIcon.setImageAutoSize(true);

			try {
				// 把trayIcon加入tray中
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println("TrayIcon could not be added.");
			}

			// 設定事件
			setEvent();
		} else {
			JOptionPane.showMessageDialog(null, "SystemTray not support!");
		}
	}

	private void setMenu() {
		// 加入選單
		openItem = new MenuItem("開啟系統主界面");
		optionItem = new MenuItem("選項");
		aboutItem = new MenuItem("關於本程式");
		exitItem = new MenuItem("離開");

		popup.add(openItem);
		popup.add(optionItem);
		popup.add(aboutItem);
		// 加入分隔線
		popup.addSeparator();
		popup.add(exitItem);
	}

	private void setEvent() {
		// 設定滑鼠事件監聽器
		MouseListener mouseListener = new MouseListener() {
			// 點擊
			public void mouseClicked(MouseEvent e) {
				System.out.println("click");//開起主程式
			}
			// 進入
			public void mouseEntered(MouseEvent e) {
			}
			// 離開
			public void mouseExited(MouseEvent e) {
			}
			//按2下
			public void mousePressed(MouseEvent e) { 
	            
				System.out.println("mousePressed");//開起主程式
			}
			//放開
			public void mouseReleased(MouseEvent e) {
			}
		};

		// openItem按鈕事件
		ActionListener openListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "開啟系統主界面");//GGC也有
			}
		};

		// optionItem按鈕事件
		ActionListener optionListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "開啟選項");//
			}
		};

		// aboutItem按鈕事件
		ActionListener aboutListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null,"哈瞜我是迪西");//關於本程式
			}
		};

		// aboutItem按鈕事件
		ActionListener exitListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);//離開
			}
		};

		// 加入事件
		openItem.addActionListener(openListener);
		optionItem.addActionListener(optionListener);
		aboutItem.addActionListener(aboutListener);
		exitItem.addActionListener(exitListener);
		trayIcon.addMouseListener(mouseListener);
	}

}
