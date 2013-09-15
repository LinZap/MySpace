package UI;

// ��@�ƹ��ƥ󪺤���
// �Ψ����I�ϥΪ̦bJTable�W���U�ئ欰

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

public class JTable_Mouse_Listener implements MouseListener {

	private JButton download, delete;
	private JComboBox<String> cbox;
	private DefaultTableModel model;
	

	public JTable_Mouse_Listener(JButton download, JButton delete,
			JComboBox<String> cbox, DefaultTableModel model) {

		this.download = download;
		this.delete = delete;
		this.cbox = cbox;
		this.model = model;
		
	}

	public void mouseClicked(MouseEvent e) {
		check();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		check();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		check();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void check() {

		boolean have = false;
		int sum= 0;
		UI_drive.one_index = -1;

		for (int i = 0; i < model.getRowCount(); i++) {

			if ((boolean) model.getValueAt(i, 0)) {
				UI_drive.one_index = i;
				have = true;
				sum++;
				//break;
			}

		}
		
		bulid_combobox(sum);
		
		if (have) {
			download.setVisible(true);
			delete.setVisible(true);
			cbox.setVisible(true);
		} else {
			download.setVisible(false);
			delete.setVisible(false);
			cbox.setVisible(false);
		}

	}
	
	//String[] function = { "��L", "���s�R�W", "����", "�W�ǧ�s����", "�ԲӸ�T" };
	private void bulid_combobox(int sum){
		
		cbox.removeAllItems();
		cbox.addItem(" - ��L�ﶵ - ");
		switch(sum){
		case 0:
			break;
			
		case 1:
			cbox.addItem("���s�R�W");
			cbox.addItem("�е��P��");
			cbox.addItem("����");
			cbox.addItem("�ԲӸ�T");
			break;
			
		default:
			cbox.addItem("�е��P��");
			cbox.addItem("����");
		}
		
		cbox.setSelectedIndex(0);
	}
	
}
