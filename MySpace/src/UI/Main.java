package UI;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.*;
import System.Favorites_Link;
import System.System_parameters;
public class Main {
	public static void main(String[] args) throws Exception {

		
		JFrame.setDefaultLookAndFeelDecorated(true);
		UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");

		
		UI_main ui_main = new UI_main();
		ui_main.setTitle(System_parameters.system_title);	
		Image img = Toolkit.getDefaultToolkit().getImage("image\\cat3.fw.png");
		ui_main.setIconImage(img);
		ui_main.setDefaultCloseOperation(3);
		ui_main.setSize(646, 432);
		ui_main.setLocationRelativeTo(null);
		ui_main.setVisible(true);
		
		

		Favorites_Link link = new Favorites_Link();
		link.create_Favorite_Link();

	}	
}
