package UI;

import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JTree;
import File_processing.File_resolve;

/**
 * 
 * 這隻副程式主要負責：能夠把檔案拖拉進程式處理
 * 
 * */
public class TextDropTargetListener implements DropTargetListener {

	private JTree tree;
	public TextDropTargetListener() {

	}
	
	public void setJTree(JTree tree){
		this.tree = tree;
	}

	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void drop(DropTargetDropEvent event) {

		// 接受 drop
		event.acceptDrop(DnDConstants.ACTION_COPY);
		// drop使用的的接口
		Transferable transferable = event.getTransferable();
		// 存入 接口傳來的訊息(可能不只1個)
		DataFlavor[] flavors = transferable.getTransferDataFlavors();

		if (flavors.length <= 0)
			System.out.println("無效檔案");
		// 用for個別處理
		for (int i = 0; i < flavors.length; i++) {
			DataFlavor d = flavors[i];

			// 傳來的訊息判斷是否為 FileList(檔案類別)
			if (d.equals(DataFlavor.javaFileListFlavor)) {
				// 將DataFlavor轉換成File形式，用List存放
				List<File> fileList = null;
				try {
					fileList = (List<File>) transferable.getTransferData(d);
					for (File fd : fileList) {
						if (fd.exists())
						System.out.println("收到檔案: " + fd);
						
						// 執行上傳檔案
						File_resolve fr = new File_resolve(fd, tree);
						fr.start();
						
						
					}
				} catch (UnsupportedFlavorException | IOException
					 e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub

	}

}
