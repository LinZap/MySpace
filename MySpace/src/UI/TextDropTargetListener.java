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
 * �o���Ƶ{���D�n�t�d�G������ɮש�Զi�{���B�z
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

		// ���� drop
		event.acceptDrop(DnDConstants.ACTION_COPY);
		// drop�ϥΪ������f
		Transferable transferable = event.getTransferable();
		// �s�J ���f�ǨӪ��T��(�i�ण�u1��)
		DataFlavor[] flavors = transferable.getTransferDataFlavors();

		if (flavors.length <= 0)
			System.out.println("�L���ɮ�");
		// ��for�ӧO�B�z
		for (int i = 0; i < flavors.length; i++) {
			DataFlavor d = flavors[i];

			// �ǨӪ��T���P�_�O�_�� FileList(�ɮ����O)
			if (d.equals(DataFlavor.javaFileListFlavor)) {
				// �NDataFlavor�ഫ��File�Φ��A��List�s��
				List<File> fileList = null;
				try {
					fileList = (List<File>) transferable.getTransferData(d);
					for (File fd : fileList) {
						if (fd.exists())
						System.out.println("�����ɮ�: " + fd);
						
						// ����W���ɮ�
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
