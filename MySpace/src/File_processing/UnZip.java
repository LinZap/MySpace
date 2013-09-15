package File_processing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class UnZip {
	
	// ¸ÑÀ£ÁY(À£ÁYÀÉ,¦s©ñ¸ô®|)
	



	
	public static synchronized String unZip(String zip,String path) throws Exception {

		try {
			ZipFile zipFile = new ZipFile(zip);
			Enumeration<?> e = zipFile.getEntries();
			ZipEntry zipEntry = null;
			
			createDirectory(path, "");
			
			while (e.hasMoreElements()) {
				zipEntry = (ZipEntry) e.nextElement();
				System.out.println("unziping " + zipEntry.getName());
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File(path + File.separator + name);
					f.mkdir();
				} else {
					String fileName = zipEntry.getName();
					fileName = fileName.replace('\\', '/');

					if (fileName.indexOf("/") != -1) {
						createDirectory(path, fileName.substring(0,
								fileName.lastIndexOf("/")));
						fileName = fileName.substring(
								fileName.lastIndexOf("/") + 1,
								fileName.length());
					}

					File f = new File(path + File.separator
							+ zipEntry.getName());

					f.createNewFile();
					InputStream in = zipFile.getInputStream(zipEntry);
					FileOutputStream out = new FileOutputStream(f);

					byte[] by = new byte[1024];
					int c;
					while ((c = in.read(by)) != -1) {
						out.write(by, 0, c);
					}
					
					zipFile.close();
					out.close();
					in.close();
					
				}
			}

		} catch (Exception ex) {
			System.out.println("unZip¿ù»~: "+ex.getMessage());
		}
		return path;

	}

	private static void createDirectory(String directory, String subDirectory) {
		String dir[];
		File fl = new File(directory);
		try {
			if (subDirectory == "" && fl.exists() != true)
				fl.mkdir();
			else if (subDirectory != "") {
				dir = subDirectory.replace('\\', '/').split("/");
				for (int i = 0; i < dir.length; i++) {
					File subFile = new File(directory + File.separator + dir[i]);
					if (subFile.exists() == false)
						subFile.mkdir();
					directory += File.separator + dir[i];
				}
			}
		} catch (Exception ex) {
			System.out.println("createDirectory¿ù»~: "+ex.getMessage());
		}
	}
}
