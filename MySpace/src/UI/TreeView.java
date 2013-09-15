package UI;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.dom4j.DocumentException;

import Data.Xml_dir;
import System.System_parameters;

public class TreeView {
	
	private JTree tree;
	private JScrollPane treeView;
	private String new_path;
	private TreePath p;
	public static DefaultMutableTreeNode node;
	
	
	// 讀取xml資料
	public TreeView() {
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("MySpace");
		try {
			
			Xml_dir xdir = new Xml_dir(top);
			top = xdir.bulidTree();
			
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		
		tree = new JTree(top);
		tree.addTreeSelectionListener(getSelectionListener());
		treeView = new JScrollPane(tree);

		MyRenderer m = new MyRenderer();
		tree.setCellRenderer(m);
		
		// 默認指向
		node = top;
		
		
		tree.setSelectionPath(new TreePath(top.getPath())); 
	
	}
	
	
	
	public JScrollPane getview() {
		return treeView;
	}

	
	public JTree getTree(){
		return tree;
	}

	
	// 目前 JTree 所選的路徑 (TreePath)
	public TreePath get_path(){
		return p;
	}
	
	
	private TreeSelectionListener getSelectionListener() {
		TreeSelectionListener c = new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				// 選擇指向
				node = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();	
				
				TreePath o = p;
				// change
				p = tree.getSelectionPath();
				
				//以防止失去選擇點時，造成系統不知道要載入哪個資料
				if(p==null)p=o;
				
				
				// 設定分類資料夾總路徑
				System_parameters.tree_path = p.toString();
				System.out.println(p.toString());
				
				
				
				// 變更表格
				UI_drive.bulid_tableData();
				UI_drive.Model_Drive.setDataVector(UI_drive.table_data,UI_drive.columnTitle);
				UI_drive.table.setModel(UI_drive.Model_Drive);
				UI_drive.setTablefeeling();	
				
				
				
				
				
				new_path = "";		
				
				for (int i = 0; i < p.getPathCount(); i++) {
					new_path += p.getPathComponent(i);
					if (i != p.getPathCount() - 1)
						new_path += " > ";
				}
				UI_drive.lable_path.setText(new_path);
			}
		};
		return c;
	}

}

class MyRenderer extends DefaultTreeCellRenderer {
	ImageIcon root = new ImageIcon("image\\root.fw.png");
	ImageIcon dir = new ImageIcon("image\\dir.fw.png");
	ImageIcon box = new ImageIcon("image\\receivebox.fw.png");
	
	private static final long serialVersionUID = 1L;
	
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		
		if (row == 0)
			setIcon(root);
		else if(value.toString().equals("檔案接收櫃"))
			setIcon(box);
		else
			setIcon(dir);

		return this;
	}

}
