package File_processing;

import java.io.File;
import java.io.IOException;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.dom4j.DocumentException;

import Data.Xml_dir;

// 檔案上傳之前，會先通過這支副程式
// 檔案若是資料夾，會處理成樹狀並儲存到[MySpace]dir.xml裡
// 並且記錄資料夾內  所有檔案  所對應在  資料夾裡的位置
// 如果檔案不屬於資料夾，則直接上船

public class File_resolve extends Thread {

	// 欲上船的檔案
	private File f;
	// 系統的 jtree
	private JTree tree;
	// 系統目前的jtree目前所選的節點
	private DefaultMutableTreeNode node;

	public File_resolve(File f, JTree tree) {

		this.f = f;
		this.tree = tree;
		node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
	}

	
	
	// 呼叫這個方法執行解析
	public void run(){
		try {
			resolve(f, node);
		} catch (DocumentException | IOException | InterruptedException e) {
		
			e.printStackTrace();
		}
	}
	
	// 檢查 n 節點下，檔名是否有重複
	private void check_same(File dir,DefaultMutableTreeNode n,int index,String name) {
	
		boolean pow = false;
		
		// 檢查所選節點下，是否有相同名稱
		for (int i = 0; i < n.getChildCount(); i++)
			if (n.getChildAt(i).toString().equals(name)) pow = true;
			
		// 如果所選節點下有相同名稱，加上一個 (1) 或 (2)... 再執行一次檢查 
		if(pow){
			name = dir.getName() + "("+index+")";
			index+=1;
			check_same(dir,n,index,name);
		}
		
		// 如果所選節點下，沒有相同名稱節點，就把結果設定進去
		else
			node_name = name;
		
		
	}
	
	private String  node_name ;
	private void resolve(File f, DefaultMutableTreeNode n)
			throws DocumentException, IOException, InterruptedException {

		// 如果檔案屬於資料夾
		if (f.isDirectory()) {
			
			//欲新增的節點名稱，就是資料夾名稱
			node_name = f.getName();
			
			// 遞迴處理檔案名稱在選擇節點下，發成重複的情形
			check_same(f,n,1,f.getName());
		
			
			// 新增一個節點，屬於目前這個資料夾
			DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(
					node_name);
			// 並且新增到目前所選的節點下
			n.add(new_node);
			// 更新jtree的介面顯示
			tree.updateUI();

			Xml_dir xd = new Xml_dir();
			xd.add_a_dir_node(n.getPath(), node_name);

			// 將資料夾內的檔案 遞回 進行分析
			File[] file_list = f.listFiles();
			for (int i = 0; i < file_list.length; i++) {
				resolve(file_list[i], new_node);
			}

			
			
		// 如果檔案不屬於資料夾
		} else {

			// 將TreeNode 陣列 解析成 路徑 格式
			TreeNode[] e = n.getPath();
			String _path = "[";
			for (int i = 0; i < e.length - 1; i++) {
				_path += e[i] + ", ";
			}
			_path += e[e.length - 1] + "]";
			System.out.println(f.getName() + " : " + _path);

			// 檔案不屬於資料夾，直接上傳
			// 參數1 檔案
			// 參數2 檔案所在路徑
			Upload_Processing up = new Upload_Processing(f, _path);
			//如果空間不足，則不上傳
			if(!up.fail)
				up.start();
			
			
		}

	}

}
