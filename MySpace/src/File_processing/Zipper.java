package File_processing;


/**
 * �o��Ƶ{���D�n�t�d���Y�P�����Y
 * 
 * */
import java.io.*;
import org.apache.tools.zip.*;
import Data.Obj_File;


public class Zipper {

	private static ZipOutputStream zos;


	//�������Y
	public synchronized static String makeZip(Obj_File f) throws IOException, FileNotFoundException {
		String zipFile_name = f.id + ".zip";
		zos = new ZipOutputStream(new FileOutputStream(zipFile_name));
		recurseFiles(f);
		zos.close();
		return zipFile_name;
	}
	
	//���Y���l�u�@�A�t�d�B�z��Ƨ����h���ɮת����Y
	private static  void  recurseFiles(File file) throws IOException,
			FileNotFoundException {
		if (file.isDirectory()) {

			String[] fileNames = file.list();
			if (fileNames != null) {
				for (int i = 0; i < fileNames.length; i++) {
					recurseFiles(new File(file, fileNames[i]));
				}
			}
		} else {
			byte[] tmp = new byte[1];
			ZipEntry zipEntry = new ZipEntry(file.getName());
			zos.putNextEntry(zipEntry);
			zos.setEncoding("utf-8");
			
			
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fin);

			while (in.read(tmp)!=-1) {
				zos.write(tmp);
			}
	
			fin.close();
			in.close();
			zos.closeEntry();
		}
	}

	


}
