package File_processing;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Merge {

	public synchronized static File File_Merge(String id, String[] store_name)  {

		File name = null ;
		File[] store_name2 = null;
		try {
			int num = store_name.length;
			name = new File(id+".zip");
			store_name2 = new File[store_name.length];
			for(int i=0;i<store_name.length;i++)
				store_name2[i]=new File(store_name[i]);
			FileOutputStream fos = new FileOutputStream(name,true);
			FileInputStream[] fis = new FileInputStream[num];
			
			for (int i = 0; i < num; i++) {
				fis[i] = new FileInputStream(store_name2[i]);
				int j = 0;
				while ((j = fis[i].read()) != -1) {
					fos.write(j);
				}
				
				fis[i].close();
			}
			 
			fos.close();
		} catch (IOException e) {e.printStackTrace();}	
		
		for(File f:store_name2)f.delete();
		
		return name;
		
	}
}