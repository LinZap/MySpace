package UI;

import java.awt.dnd.DropTarget;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;

import org.dom4j.DocumentException;
import Data.Xml_files;
import File_processing.*;
import System.System_parameters;

public class UI_drive extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	public static JPanel ui_dive;
	public static Dialog_Doing dg_doing = new Dialog_Doing();
	private JPanel top_panel, act_panel, space_panel, center_panel, tree_panel,
			table_panel, path_panel;
	private JButton btn_add_act, btn_set, btn_add_files, btn_add_dir, btn_send,
			btn_download, btn_delete, btn_syn_dir, btn_list_act;
	public static JButton btn_receive;
	private JComboBox<String> cbox_sort, cbox_file;
	private TreeView treeview;
	public static Map<String, Object> map;
	private JScrollPane table_js, tree_js;
	public static Model_Drive Model_Drive;
	public static JLabel lable_path, used_space_label,used_space_label2;
	public static int one_index = -1;
	public static JTable table;
	public static Object[] columnTitle = { " ", "  ", "   ", "���D", "���A", "�W�Ǥ��" };
	public static Object[][] table_data;

	public UI_drive() throws DocumentException {

		ui_dive = this;
		// (�B�ʱƪ� arg1 = ����覡 , arg2 = �������� , arg3 = ��������)
		new BoxLayout(ui_dive, BoxLayout.Y_AXIS);

		// /////////////////////// top_panel

		top_panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		top_panel.setPreferredSize(new Dimension(600, 40));

		// //////////// act panel
		act_panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		act_panel.setPreferredSize(new Dimension(200, 50));

		JLabel img_label = new JLabel(new ImageIcon("image\\ico_dive.fw.png"));
		JLabel root_act = new JLabel(System_parameters.root_act.split("@")[0]);
		root_act.setFont(new Font("�L�n������", Font.BOLD, 14));

		btn_add_act = new JButton(new ImageIcon("image\\add2.fw.png"));
		btn_list_act = new JButton(new ImageIcon("image\\down.fw.png"));

		btn_add_act.setPreferredSize(new Dimension(20, 17));
		btn_list_act.setPreferredSize(new Dimension(16, 17));

		act_panel.add(img_label);
		act_panel.add(root_act);
		act_panel.add(btn_list_act);
		act_panel.add(btn_add_act);

		// ///////////// act panel
		top_panel.add(act_panel);

		// //////////// space_panel
		space_panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		space_panel.setPreferredSize(new Dimension(390, 30));

		used_space_label = new JLabel("");
		used_space_label.setPreferredSize(new Dimension(150, 30));

		used_space_label2 = new JLabel("");
		used_space_label2.setPreferredSize(new Dimension(150, 30));
		
		btn_set = new JButton(new ImageIcon("image\\set2.fw.png"));
		btn_set.setPreferredSize(new Dimension(30, 25));

		btn_receive = new JButton(new ImageIcon("image\\ceive.fw.png"));
		btn_receive.setPreferredSize(new Dimension(30, 25));

		space_panel.add(used_space_label);
		space_panel.add(used_space_label2);
		space_panel.add(btn_receive);
		space_panel.add(btn_set);
		// ////////////space_panel
		top_panel.add(space_panel);

		// /////////////////////////center_panel

		center_panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		center_panel.setPreferredSize(new Dimension(600, 35));

		btn_add_files = new JButton("  �s�W", new ImageIcon(
				"image\\add_files.fw.png"));
		btn_add_files.setPreferredSize(new Dimension(125, 25));

		btn_add_dir = new JButton(new ImageIcon("image\\adddir.fw.png"));
		btn_add_dir.setPreferredSize(new Dimension(30, 25));

		btn_syn_dir = new JButton(new ImageIcon("image\\syn.fw.png"));
		btn_syn_dir.setPreferredSize(new Dimension(30, 25));

		btn_download = new JButton(new ImageIcon("image\\download.fw.png"));
		btn_download.setPreferredSize(new Dimension(30, 25));

		btn_delete = new JButton(new ImageIcon("image\\del.fw.png"));
		btn_delete.setPreferredSize(new Dimension(30, 25));

		btn_send = new JButton(new ImageIcon("image\\send.fw.png"));
		btn_send.setPreferredSize(new Dimension(30, 25));

		cbox_file = new JComboBox<String>();
		cbox_file.setPreferredSize(new Dimension(100, 24));
		cbox_file.setVisible(false);

		center_panel.add(btn_add_files);

		center_panel.add(btn_add_dir);
		center_panel.add(btn_syn_dir);
		center_panel.add(btn_delete);
		center_panel.add(btn_download);
		center_panel.add(btn_send);
		center_panel.add(cbox_file);

		// /////////////////////////center_panel

		// /////////////////////////table_panel

		table_panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		table_panel.setPreferredSize(new Dimension(460, 270));

		// /////////////////////////path_panel
		path_panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		path_panel.setPreferredSize(new Dimension(460, 20));

		lable_path = new JLabel("MySpace");
		lable_path.setFont(new Font("�L�n������", Font.PLAIN, 12));
		lable_path.setPreferredSize(new Dimension(350, 20));

		cbox_sort = new JComboBox<String>();
		cbox_sort.addItem("�̤W�Ǥ���Ƨ�");
		cbox_sort.setPreferredSize(new Dimension(100, 20));

		path_panel.add(lable_path);
		path_panel.add(cbox_sort);
		// /////////////////////////path_panel

		// 1.bulid table
		table = new JTable();
		table_js = new JScrollPane(table);
		table_js.setPreferredSize(new Dimension(450, 240));

		// 2.bulid data
		Xml_files xf = new Xml_files();
		map = xf.bulid_files();
		bulid_tableData();
		Model_Drive = new Model_Drive(table_data, columnTitle);
		table.setModel(Model_Drive);

		// 3.feeling
		setTablefeeling();
		// /////////////////////////table_panel
		table_panel.add(path_panel);
		table_panel.add(table_js);

		// //////////////////////tree_panel
		tree_panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		tree_panel.setPreferredSize(new Dimension(120, 270));
		// ��
		treeview = new TreeView();
		tree_js = treeview.getview();
		tree_js.setPreferredSize(new Dimension(120, 270));
		// /////////////////////////tree_panel
		tree_panel.add(tree_js);

		add(top_panel);
		add(center_panel);
		add(tree_panel);
		add(table_panel);

		TextDropTargetListener drop_listener = new TextDropTargetListener();
		drop_listener.setJTree(treeview.getTree());
		// �I�s��Ԩƥ�,�~���i�H�즲���
		new DropTarget(ui_dive, drop_listener);
		// ��Jtree�Ƕi�h�A�W�Ǹ�Ƨ��n�ϥ�

		// �I�s�ƹ��I���ƥ�
		JTable_Mouse_Listener table_listener = new JTable_Mouse_Listener(
				btn_download, btn_delete, cbox_file, Model_Drive);
		table.addMouseListener(table_listener);

		// ���U���s�ƥ�
		btn_add_act.addActionListener(this);
		btn_set.addActionListener(this);
		btn_add_files.addActionListener(this);
		btn_add_dir.addActionListener(this);
		btn_syn_dir.addActionListener(this);
		btn_send.addActionListener(this);
		btn_download.addActionListener(this);
		btn_delete.addActionListener(this);
		btn_receive.addActionListener(this);
		btn_list_act.addActionListener(this);

	}

	// "checkBox" " star" "icon" "FileName" "status"
	// "savedate" };
	// �NArrayList �ѪR���G���}�C���
	public static void bulid_tableData() {
		// V0.3.0
		@SuppressWarnings("unchecked")
		ArrayList<Object> files = (ArrayList<Object>) map
				.get(System_parameters.tree_path);

		if (files != null) {

			table_data = new Object[files.size()][8];

			for (int i = 0; i < files.size(); i++) {

				Object[] one_file = (Object[]) files.get(i);

				table_data[i][0] = (boolean) one_file[14];
				table_data[i][1] = getStar(one_file[10].toString());
				table_data[i][2] = System_parameters.getFileIcon(one_file[4]
						.toString());
				table_data[i][3] = one_file[2];
				table_data[i][4] = getStatus(one_file[13]);
				System.out.println("bulid tableData in status:"
						+ table_data[i][4]);
				table_data[i][5] = one_file[5];
				// �������
				// �����B�� ArrayList�� index
				table_data[i][6] = i;
				table_data[i][7] = one_file[4];
			}

		} else
			table_data = null;

	}

	private static ImageIcon getStar(String star) {
		if (Integer.parseInt(star) == 1)
			return new ImageIcon("image\\star.fw.png");
		else
			return new ImageIcon("image\\nostar.fw.png");
	}

	private static ImageIcon getStatus(Object img) {
		if (img != null)
			return new ImageIcon(img.toString());
		else
			return null;
	}

	public static void setTablefeeling() {

		table.getColumn(columnTitle[0]).setMinWidth(27);
		table.getColumn(columnTitle[0]).setMaxWidth(27);
		table.getColumn(columnTitle[0]).setResizable(false);

		table.getColumn(columnTitle[1]).setMinWidth(25);
		table.getColumn(columnTitle[1]).setMaxWidth(25);
		table.getColumn(columnTitle[1]).setResizable(false);

		table.getColumn(columnTitle[2]).setMinWidth(25);
		table.getColumn(columnTitle[2]).setMaxWidth(25);
		table.getColumn(columnTitle[2]).setResizable(false);

		table.getColumn(columnTitle[3]).setMinWidth(180);
		table.getColumn(columnTitle[3]).setMaxWidth(180);
		table.getColumn(columnTitle[3]).setResizable(false);

		table.getColumn(columnTitle[4]).setMinWidth(108);
		table.getColumn(columnTitle[4]).setMaxWidth(108);
		table.getColumn(columnTitle[4]).setResizable(false);

		table.getColumn(columnTitle[5]).setResizable(false);
		table.setRowHeight(26);

		table.setShowVerticalLines(false);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btn_download) {

			// �T�w�C���ɮת��A�����O���b�B�z��
			if (check_one_ok())

				for (int i = 0; i < Model_Drive.getRowCount(); i++) {

					File sel_file = new File("C:\\MySpace\\" + table_data[i][3]
							+ "." + table_data[i][7]);

					if (!sel_file.exists()) {

						if ((boolean) Model_Drive.getValueAt(i, 0)) {

							// �barraylist����m
							int index = (int) table_data[i][6];
							Download_Processing dp = new Download_Processing(
									index, System_parameters.tree_path);
							dp.start();

						}

					} else {
						if ((boolean) Model_Drive.getValueAt(i, 0)) {

							int index = (int) table_data[i][6];

							@SuppressWarnings("unchecked")
							ArrayList<Object> files = (ArrayList<Object>) UI_drive.map
									.get(System_parameters.tree_path);
							Object[] file = (Object[]) files.get(index);

							// ����ɮת���
							long modified = Long.parseLong(file[11].toString());

							// ���ݸ��s
							if (sel_file.lastModified() < modified) {
								// ����U��
								Download_Processing dp = new Download_Processing(
										index, System_parameters.tree_path);
								dp.start();
							}
							// ���a���s
							else {
								// Nothing
							}
						}
					}

					Model_Drive.setValueAt(false, i, 0);
				}
			else
				JOptionPane.showMessageDialog(UI_drive.ui_dive,
						"�е����ɮ׳B�̧�����A�b����U��!", "�ɮץ��b�B�z��",
						JOptionPane.ERROR_MESSAGE);

		}

		else if (e.getSource() == btn_add_files) {

			File_Dialog dialog = new File_Dialog();
			dialog.show();

		}

		// click_add dir

		else if (e.getSource() == btn_add_dir) {

			new Dialog_Dir_Name(treeview.get_path(), treeview.getTree());

		}

		else if (e.getSource() == btn_delete) {

			// �߰ݽT�{�R��
			int msg = JOptionPane.showConfirmDialog(this, "�T�w�R���ɮ�?", "�T�{�R��",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (msg == 0) {
				for (int i = 0; i < Model_Drive.getRowCount(); i++) {
					if ((boolean) Model_Drive.getValueAt(i, 0)) {

						

						Delete_Processing dp = new Delete_Processing(
								System_parameters.tree_path, i);
						dp.start();

					}
					Model_Drive.setValueAt(false, i, 0);
				}
			}

		}

		else if (e.getSource() == btn_syn_dir) {

			// �߰ݬO�_�P�B��Ƨ�
			int msg = JOptionPane.showConfirmDialog(this,
					"�Y�N�P�B��Ӹ�Ƨ��A�]�t��Ƨ������Ҧ��ɮ׻P��Ƨ�?", "�T�{�P�B",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (msg == 0) {
				if (check_ok()) {
					Dir_Synchronized ds = new Dir_Synchronized(
							treeview.getTree());
					ds.start();
				} else
					JOptionPane.showMessageDialog(UI_drive.ui_dive,
							"�е����ɮ׳B�̧�����A�b����U��!", "�ɮץ��b�B�z��",
							JOptionPane.ERROR_MESSAGE);

			}

		}

		else if (e.getSource() == btn_receive) {

			dg_doing.setVisible(true);
			btn_receive.setIcon(new ImageIcon("image\\ceive.fw.png"));

		}

		else if (e.getSource() == btn_set) {

			UI_set.ui_set.setVisible(true);

		}

		else if (e.getSource() == btn_send) {

			for (int i = 0; i < Model_Drive.getRowCount(); i++) {

				if ((boolean) Model_Drive.getValueAt(i, 0)) {

					// �barraylist����m
					int index = (int) table_data[i][6];

					System.out.println("�barraylist�������ެO " + index);

					// �}�Ҷǰe�ɮ׸߰ݸ��|����
					new Dialog_SendFile(System_parameters.tree_path, index);

				}
			}

		} else if (e.getSource() == btn_add_act) {

			new Dialog_AddAct();

		} else if (e.getSource() == btn_list_act) {
			try {
				new Dialog_ListAct();
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
		}

	}
	
	private boolean check_one_ok() {

		for (int i = 0; i < Model_Drive.getRowCount(); i++) {
			
			if((boolean) Model_Drive.getValueAt(i, 0))
				if (Model_Drive.getValueAt(i, 4) != null)return false;
			
		}
		return true;
	}
	
	private boolean check_ok() {

		for (int i = 0; i < Model_Drive.getRowCount(); i++) {
			if (Model_Drive.getValueAt(i, 4) != null)
				return false;
		}
		return true;
	}

}