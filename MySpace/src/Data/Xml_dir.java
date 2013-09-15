package Data;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.dom4j.*;
import org.dom4j.io.*;
import System.System_parameters;

public class Xml_dir {

	private File f;
	private SAXReader reader = new SAXReader();
	private Document d ;
	private DefaultMutableTreeNode top;
	private TreePath p;
	private TreeNode[] node;
	private static Element root;
	private String dir_name,new_node_name;
	private DefaultMutableTreeNode n;
	private JTree tree;
	
	
	public Xml_dir(DefaultMutableTreeNode top) throws DocumentException {
		f = new File(System_parameters.get_db_path(1));
		d =reader.read(f);
		root = d.getRootElement();
		this.top = top;
	}
	
	public Xml_dir() throws DocumentException{
		f = new File(System_parameters.get_db_path(1));
		d =reader.read(f);	
		root = d.getRootElement();
	}

	public DefaultMutableTreeNode bulidTree() {
		bulid_leaf(top, root.elements());
		return top;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void bulid_leaf(DefaultMutableTreeNode node, List list) {
		List<Element> p = new ArrayList<Element>();
		p = list;
		for (int i = 0; i < p.size(); i++) {
			String s = p.get(i).attribute("name").getText();
			DefaultMutableTreeNode node_leaf = new DefaultMutableTreeNode(s);
				node.add(node_leaf);
				List ele_leaf = p.get(i).elements();
			bulid_leaf(node_leaf, ele_leaf);
		}
	}
	
	//新增一個節點 new_node， 在 target_node下
	public void add_a_dir_node(TreeNode[] treeNodes ,String new_node) throws IOException{
		
		// 路徑 索引
		int index=1;
		// 目前所選路徑
		node = treeNodes;
		// 新增名稱
		new_node_name = new_node;
		// 開始解析
		add_target(index,root);
		
	}

	//解析節點
	@SuppressWarnings("unchecked")
	private void add_target(int index,Element inee) throws IOException{
	if(index == node.length){			
			//寫入XML內
			Element new_dir = inee.addElement("dir");
			new_dir.addAttribute("name",new_node_name);
			save_xml();
		}
		else{
		List<Element> ele = new ArrayList<Element>();
		ele = inee.elements();
		for (int i = 0; i < ele.size(); i++) {
			if(ele.get(i).attribute("name").getText().equals(node[index].toString())){
				index++;
				add_target(index,ele.get(i));
				break;
			}
			
		}
	  }
		
		
		
	}
	
	
/*
 * 	p 目前所選的路徑
 *  dir_name 檔案名稱
 *  n 目前選擇的節點
 *  tree 系統的JTree
 * 
 * */	
	public void add_dir(TreePath p, String dir_name,DefaultMutableTreeNode n,JTree tree) throws IOException {
		
		this.p = p;
		this.dir_name = dir_name;
		this.n = n;
		this.tree = tree;
		int index=1;
		serach_node(index,root);
	}

	
	@SuppressWarnings("unchecked")
	private void serach_node(int index,Element inee) throws IOException{
		
		if(index == p.getPathCount()){
			
	
			
			//寫入XML內
			Element new_dir = inee.addElement("dir");
			new_dir.addAttribute("name",dir_name);
			save_xml();
			
			DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(dir_name);	
			n.add(new_node);
			tree.setSelectionPath(new TreePath(new_node.getPath())); 
			tree.updateUI();
			
		}
		
		else{
			
		List<Element> ele = new ArrayList<Element>();
		ele = inee.elements();
		for (int i = 0; i < ele.size(); i++) {
			
			if(ele.get(i).attribute("name").getText().equals(p.getPathComponent(index).toString())){
				index++;
				System.out.println("index =  "+index);
				serach_node(index,ele.get(i));
				break;
			}
			
		}
	  }
		
	}
	

	
	
	private void save_xml() throws IOException {
		XMLWriter writer = new XMLWriter(new FileOutputStream(f),
				OutputFormat.createPrettyPrint());
		writer.write(d);
		writer.close();
	}
	
}
