package UI;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
public class UI_waiting extends JPanel{
	public static JPanel ui_waiting;
	public static JLabel status;
	private static final long serialVersionUID = 1L;
	public UI_waiting(){
		UI_waiting.ui_waiting = this;
		int width = 300,height = 300;
		JPanel first = new JPanel();
		first.setPreferredSize(new Dimension(width, height));
		new BoxLayout(first,BoxLayout.Y_AXIS);
		JLabel img = new JLabel(new ImageIcon("image\\loader.gif"));
		status= new JLabel("載入中...");	
		status.setFont(new Font("微軟正黑體", Font.PLAIN, 17));
		first.add(Box.createRigidArea(new Dimension(width, 65)));
		first.add(img);
		first.add(Box.createRigidArea(new Dimension(width, 40)));
		first.add(status);	
		add(first);	
	}
	
}
