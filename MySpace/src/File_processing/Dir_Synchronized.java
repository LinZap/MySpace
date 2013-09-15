package File_processing;

import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import System.System_parameters;
import UI.UI_drive;

public class Dir_Synchronized extends Thread {

	private JTree tree;

	public Dir_Synchronized(JTree tree) {
		this.tree = tree;
	}

	public void run(){
		try {
			bulid_dir();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void bulid_dir() throws InterruptedException {

		// �̫�諸�`�I
		DefaultMutableTreeNode snode = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		// �`�I�����`�I
		TreeNode parent = snode.getParent();

		if (parent == null) {
			
			System.out.println("��ܤF�̳��ݪ�MySpace");
			
			File dir = new File("C:\\MySpace");
			
			//�}�l�P�B��Ƨ��������
			synchronize_file(dir,snode);
			make_child_dir(dir, snode);
			

		} else {

			System.out.println("make dir in MySapce: " + snode.toString());

			// �إ߸�Ƨ��b MySpace���U
			File dir = new File("C:\\MySpace\\" + snode.toString());

			// �p�G��Ƨ����s�b�A�N�إ߷s��Ƨ�
			if (!dir.exists())
				dir.mkdirs();
			
			//�}�l�P�B��Ƨ��������
			synchronize_file(dir,snode);

			make_child_dir(dir, snode);
		}

	}

	private void make_child_dir(File dir, DefaultMutableTreeNode snode) throws InterruptedException {

		if (snode.getChildCount() > 0) {

			for (int i = 0; i < snode.getChildCount(); i++) {

				// ���X�l�`�I
				DefaultMutableTreeNode n = (DefaultMutableTreeNode) snode
						.getChildAt(i);

				System.out.println("make dir under " + dir + " , name is: "
						+ n.toString());

				// �إߤl��Ƨ��b����Ƨ����U
				File child_dir = new File(dir.getAbsolutePath() + "\\"
						+ n.toString());

				// �p�G�l��Ƨ����s�b�A�N�إ�
				if (!child_dir.exists())
					child_dir.mkdirs();
				
				//�}�l�P�B��Ƨ��������
				synchronize_file(child_dir,n);
				
				// ���j �ˬd �� �إ� �l��Ƨ�
				make_child_dir(child_dir, n);
			}

		}

	}

	// �N�`�I �ഫ�� (�i�H�qhashMap���X��ƪ�) ���|�r��
	private String parsePathString(DefaultMutableTreeNode snode) {
		TreeNode[] e = snode.getPath();

		String _path = "[";
		for (int i = 0; i < e.length - 1; i++) {
			_path += e[i] + ", ";
		}
		_path += e[e.length - 1] + "]";

		return _path;
	}

	// �ǤJ�`�I�P��������Ƨ��A���X�`�I�Ҧ��ɮ׸�ơA�}�l�U��
	private void synchronize_file(File dir, DefaultMutableTreeNode snode) throws InterruptedException {

		// ���o�`�I�ҹ�����Map��� ���|
		String in_path = parsePathString(snode);

		// ���o�`�I�������Ҧ��ɮ׸��
		@SuppressWarnings("unchecked")
		ArrayList<Object> files = (ArrayList<Object>) UI_drive.map.get(in_path);

		//�ק窱�A
		ImageIcon ic = new ImageIcon("image\\waiting.fw.png");
		
		if(files != null)
		for(int j =0; j<files.size(); j++){
			Object[] file = (Object[]) files.get(j);
			file[13] = ic;
			if(in_path.equals(System_parameters.tree_path))	
				UI_drive.Model_Drive.setValueAt(ic, j, 4);	
		}

		if(files == null)
			System.out.println(in_path +"�䤣���ɮ׸��");
		else
		// �j��U�� Map���X���Ҧ��ɮ�
		for (int i = 0; i < files.size(); i++) {
			
			// �qmap�����X�ɮצW��
			Object[] file = (Object[]) files.get(i);
			String file_name = file[2].toString();
			
			
			
			
			// �Q�U�����ɮ�
			File target_file = new File(dir.getAbsolutePath() + "\\"
					+ file_name);
			System.out.println("�ǳƤU�����ɮ׬�: "+target_file);
			// �p�G�Q�U�����ɮפw�g�s�b
			if (target_file.exists()) {

				// ����ɮת���
				long modified = Long.parseLong(file[11].toString());

				// ���ݸ��s
				if (target_file.lastModified() < modified) {
					

				
	
					// ����U��
					Download_Processing dp = new Download_Processing(i, in_path);
					// �]�w�x�s�ؼ�
					dp.setTarget(dir);
					// �}�l�U��
					dp.start();
					dp.join();
					
					
					
				}
				// ���a���s
				else {
					// Nothing
				}
			}

			// �p�G�Q�U�����ɮפ��s�b
			else {
				
				
				
				// �U���ɮ�
				Download_Processing dp = new Download_Processing(i, in_path);
				// �]�w�x�s�ؼ�
				dp.setTarget(dir);
				// �}�l�U��				
				dp.start();
				dp.join();
			}

		}

	}

}
