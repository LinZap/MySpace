package UI;

import java.awt.Container;
import javax.swing.*;

/**
 * �o��class�O�Ҧ���������
 * �t�d��@�D�n��JFrame�P����Container
 * 
 * �p���@��
 * �Ҧ������@�v�����~�� "JPanel" ���g�U�۪� "�ƪ�" �B "���e" �P "�\��"
 * 
 * 
 * ���������ݭn�����ɡG 
 * 1. ���N�ثe��ܪ�JPanel���� setVisible(false)
 * 2. �N�n��ܪ������[�JContainer �M��ե� setVisible(true)�@������
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