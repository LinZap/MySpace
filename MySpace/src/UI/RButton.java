package UI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RButton extends JButton {
	private static final long serialVersionUID = 39082560987930759L;
	public static Color BUTTON_COLOR1 = new Color(255, 250, 250);
	public static Color BUTTON_COLOR2 = new Color(50, 50, 100);

	// public static final Color BUTTON_COLOR1 = new Color(125, 161, 237);
	// public static final Color BUTTON_COLOR2 = new Color(91, 118, 173);
	public static final Color BUTTON_FOREGROUND_COLOR = Color.WHITE;
	private boolean hover;

	private ImageIcon a;
	private ImageIcon b;

	public RButton(String s,ImageIcon ico) {
		this.setText(s);
		this.setIcon(ico);
		basic_set();
	}
	public RButton(ImageIcon ico) {
		this.setIcon(ico);
		basic_set();
	}

	private void basic_set(){
		
		setFont(new Font(" ", Font.PLAIN, 12));
		setBorderPainted(false);
		setForeground(BUTTON_COLOR2);
		setFocusPainted(false);
		setContentAreaFilled(false);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (b != null)
					setIcon(b);

				setForeground(BUTTON_FOREGROUND_COLOR);
				hover = true;
				repaint();
			} // 當滑鼠經過時改變字的顏色

			@Override
			public void mouseExited(MouseEvent e) {
				if (a != null)
					setIcon(a);

				setForeground(BUTTON_COLOR2);
				hover = false;
				repaint();
			} // 滑鼠按下時的前景顏色
		});
		
	}
	
	public void MyDrive_SetIcon(ImageIcon a, ImageIcon b) {

		this.setIcon(a);

		this.a = a;
		this.b = b;
	}

	public void MyDrive_Setsize(int x, int y) {
		this.setPreferredSize(new Dimension(x, y));
	}

	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g.create();
		int h = getHeight();
		int w = getWidth();
		float tran = 1F;

		if (!hover) {
			tran = 0.3F;
		}
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		GradientPaint p1;// 外邊線
		GradientPaint p2;// 內邊線

		if (getModel().isPressed()) {
			p1 = new GradientPaint(0, 0, new Color(255, 250, 250), 0, h - 1,
					new Color(131, 139, 139));
			////////////////////////////////////////////////////
			p2 = new GradientPaint(0, 1, new Color(50, 50, 100), 0, h - 3,
					new Color(131, 139, 139));
			BUTTON_COLOR2 = new Color(0, 0, 0);
		} else {
			p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, h - 1,
					new Color(0, 0, 0));
			///////////////////////////////////////////////////////////
			p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0,
					h - 3, new Color(0, 0, 0, 50));
			BUTTON_COLOR2 = new Color(50, 50, 100);

		}

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				tran));
		/////////////////////////////////////////20
		RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1,
				h - 1, 20, 20);
		Shape clip = g2d.getClip();
		g2d.clip(r2d);
		GradientPaint gp = new GradientPaint(0.0F, 0.0F, BUTTON_COLOR1, 0.0F,
				h, BUTTON_COLOR2, true);// 漸層
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);
		g2d.setClip(clip);
		g2d.setPaint(p1);
		g2d.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);
		g2d.setPaint(p2);
		g2d.drawRoundRect(1, 1, w - 2, h - 2, 18, 18); // ←這樣能造成按下時的突出效果..*/
		g2d.dispose();
		super.paintComponent(g);
	}
}