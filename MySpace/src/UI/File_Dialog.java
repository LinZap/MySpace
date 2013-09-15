package UI;

import java.io.File;
import java.io.IOException;

import org.dom4j.DocumentException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import File_processing.Upload_Processing;
import System.System_parameters;

public class File_Dialog {

	private File f;

	public void show() {

		Display display = new Display();
		final Shell shell = new Shell(display);
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		String[] filterNames = new String[] { "All Files (*)" };
		String[] filterExtensions = new String[] { "*" };
		String filterPath = "C:\\MySpace";
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filterPath);
		String s = dialog.open();
		f = (s != null) ? new File(s) : null;
		if (f != null) {
			try {
				Upload_Processing up = new Upload_Processing(f,
						System_parameters.tree_path);
				if(!up.fail)
				up.start();
			
				
			} catch (DocumentException | IOException e1) {
				e1.printStackTrace();
			}
		}
		shell.close();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}
}
