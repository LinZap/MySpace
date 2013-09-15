package File_processing;

import java.io.File;
import java.io.IOException;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.dom4j.DocumentException;

import Data.Xml_dir;

// �ɮפW�Ǥ��e�A�|���q�L�o��Ƶ{��
// �ɮ׭Y�O��Ƨ��A�|�B�z���𪬨��x�s��[MySpace]dir.xml��
// �åB�O����Ƨ���  �Ҧ��ɮ�  �ҹ����b  ��Ƨ��̪���m
// �p�G�ɮפ��ݩ��Ƨ��A�h�����W��

public class File_resolve extends Thread {

	// ���W��ɮ�
	private File f;
	// �t�Ϊ� jtree
	private JTree tree;
	// �t�Υثe��jtree�ثe�ҿ諸�`�I
	private DefaultMutableTreeNode node;

	public File_resolve(File f, JTree tree) {

		this.f = f;
		this.tree = tree;
		node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
	}

	
	
	// �I�s�o�Ӥ�k����ѪR
	public void run(){
		try {
			resolve(f, node);
		} catch (DocumentException | IOException | InterruptedException e) {
		
			e.printStackTrace();
		}
	}
	
	// �ˬd n �`�I�U�A�ɦW�O�_������
	private void check_same(File dir,DefaultMutableTreeNode n,int index,String name) {
	
		boolean pow = false;
		
		// �ˬd�ҿ�`�I�U�A�O�_���ۦP�W��
		for (int i = 0; i < n.getChildCount(); i++)
			if (n.getChildAt(i).toString().equals(name)) pow = true;
			
		// �p�G�ҿ�`�I�U���ۦP�W�١A�[�W�@�� (1) �� (2)... �A����@���ˬd 
		if(pow){
			name = dir.getName() + "("+index+")";
			index+=1;
			check_same(dir,n,index,name);
		}
		
		// �p�G�ҿ�`�I�U�A�S���ۦP�W�ٸ`�I�A�N�⵲�G�]�w�i�h
		else
			node_name = name;
		
		
	}
	
	private String  node_name ;
	private void resolve(File f, DefaultMutableTreeNode n)
			throws DocumentException, IOException, InterruptedException {

		// �p�G�ɮ��ݩ��Ƨ�
		if (f.isDirectory()) {
			
			//���s�W���`�I�W�١A�N�O��Ƨ��W��
			node_name = f.getName();
			
			// ���j�B�z�ɮצW�٦b��ܸ`�I�U�A�o�����ƪ�����
			check_same(f,n,1,f.getName());
		
			
			// �s�W�@�Ӹ`�I�A�ݩ�ثe�o�Ӹ�Ƨ�
			DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(
					node_name);
			// �åB�s�W��ثe�ҿ諸�`�I�U
			n.add(new_node);
			// ��sjtree���������
			tree.updateUI();

			Xml_dir xd = new Xml_dir();
			xd.add_a_dir_node(n.getPath(), node_name);

			// �N��Ƨ������ɮ� ���^ �i����R
			File[] file_list = f.listFiles();
			for (int i = 0; i < file_list.length; i++) {
				resolve(file_list[i], new_node);
			}

			
			
		// �p�G�ɮפ��ݩ��Ƨ�
		} else {

			// �NTreeNode �}�C �ѪR�� ���| �榡
			TreeNode[] e = n.getPath();
			String _path = "[";
			for (int i = 0; i < e.length - 1; i++) {
				_path += e[i] + ", ";
			}
			_path += e[e.length - 1] + "]";
			System.out.println(f.getName() + " : " + _path);

			// �ɮפ��ݩ��Ƨ��A�����W��
			// �Ѽ�1 �ɮ�
			// �Ѽ�2 �ɮשҦb���|
			Upload_Processing up = new Upload_Processing(f, _path);
			//�p�G�Ŷ������A�h���W��
			if(!up.fail)
				up.start();
			
			
		}

	}

}
