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

		// 最後選的節點
		DefaultMutableTreeNode snode = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		// 節點的父節點
		TreeNode parent = snode.getParent();

		if (parent == null) {
			
			System.out.println("選擇了最頂端的MySpace");
			
			File dir = new File("C:\\MySpace");
			
			//開始同步資料夾內的資料
			synchronize_file(dir,snode);
			make_child_dir(dir, snode);
			

		} else {

			System.out.println("make dir in MySapce: " + snode.toString());

			// 建立資料夾在 MySpace底下
			File dir = new File("C:\\MySpace\\" + snode.toString());

			// 如果資料夾不存在，就建立新資料夾
			if (!dir.exists())
				dir.mkdirs();
			
			//開始同步資料夾內的資料
			synchronize_file(dir,snode);

			make_child_dir(dir, snode);
		}

	}

	private void make_child_dir(File dir, DefaultMutableTreeNode snode) throws InterruptedException {

		if (snode.getChildCount() > 0) {

			for (int i = 0; i < snode.getChildCount(); i++) {

				// 取出子節點
				DefaultMutableTreeNode n = (DefaultMutableTreeNode) snode
						.getChildAt(i);

				System.out.println("make dir under " + dir + " , name is: "
						+ n.toString());

				// 建立子資料夾在父資料夾底下
				File child_dir = new File(dir.getAbsolutePath() + "\\"
						+ n.toString());

				// 如果子資料夾不存在，就建立
				if (!child_dir.exists())
					child_dir.mkdirs();
				
				//開始同步資料夾內的資料
				synchronize_file(child_dir,n);
				
				// 遞迴 檢查 並 建立 子資料夾
				make_child_dir(child_dir, n);
			}

		}

	}

	// 將節點 轉換成 (可以從hashMap取出資料的) 路徑字串
	private String parsePathString(DefaultMutableTreeNode snode) {
		TreeNode[] e = snode.getPath();

		String _path = "[";
		for (int i = 0; i < e.length - 1; i++) {
			_path += e[i] + ", ";
		}
		_path += e[e.length - 1] + "]";

		return _path;
	}

	// 傳入節點與對應的資料夾，取出節點所有檔案資料，開始下載
	private void synchronize_file(File dir, DefaultMutableTreeNode snode) throws InterruptedException {

		// 取得節點所對應的Map資料 路徑
		String in_path = parsePathString(snode);

		// 取得節點對應的所有檔案資料
		@SuppressWarnings("unchecked")
		ArrayList<Object> files = (ArrayList<Object>) UI_drive.map.get(in_path);

		//修改狀態
		ImageIcon ic = new ImageIcon("image\\waiting.fw.png");
		
		if(files != null)
		for(int j =0; j<files.size(); j++){
			Object[] file = (Object[]) files.get(j);
			file[13] = ic;
			if(in_path.equals(System_parameters.tree_path))	
				UI_drive.Model_Drive.setValueAt(ic, j, 4);	
		}

		if(files == null)
			System.out.println(in_path +"找不到檔案資料");
		else
		// 迴圈下載 Map取出的所有檔案
		for (int i = 0; i < files.size(); i++) {
			
			// 從map內取出檔案名稱
			Object[] file = (Object[]) files.get(i);
			String file_name = file[2].toString();
			
			
			
			
			// 想下載的檔案
			File target_file = new File(dir.getAbsolutePath() + "\\"
					+ file_name);
			System.out.println("準備下載的檔案為: "+target_file);
			// 如果想下載的檔案已經存在
			if (target_file.exists()) {

				// 比較檔案版本
				long modified = Long.parseLong(file[11].toString());

				// 遠端較新
				if (target_file.lastModified() < modified) {
					

				
	
					// 執行下載
					Download_Processing dp = new Download_Processing(i, in_path);
					// 設定儲存目標
					dp.setTarget(dir);
					// 開始下載
					dp.start();
					dp.join();
					
					
					
				}
				// 本地較新
				else {
					// Nothing
				}
			}

			// 如果想下載的檔案不存在
			else {
				
				
				
				// 下載檔案
				Download_Processing dp = new Download_Processing(i, in_path);
				// 設定儲存目標
				dp.setTarget(dir);
				// 開始下載				
				dp.start();
				dp.join();
			}

		}

	}

}
