package File_processing;

/*
 * 這個副程式主要用來切割與合併檔案
 * 傳入的參數有：要處理的檔案與切割數量
 * */
import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;

import Data.Obj_File;

public class Splitter {
		
	public static synchronized File[] split(Obj_File f,File zipfile) throws FileNotFoundException, IOException {
		
		// 建立存放切割的檔案容器(數量)
		File[] sp_file = new File[f.sub_number];
		// 建立把資料寫進容器的串流(多個)
		FileOutputStream[] fos = new FileOutputStream[f.sub_number];
		
		// 建立分割檔案的容器tmp(name.zip( i ).tmp)
		for (int i = 0; i < f.sub_number; i++) {
			// 取得切割檔名
			System.out.println("切割檔名: "+f.store[i][0]);
			sp_file[i] = new File(f.store[i][0]);
			fos[i] = new FileOutputStream(sp_file[i], true);
			
		}

		// 建立輸入檔案的RandomAccessFile，
		// 因為需要用映射的方式對應資料做切割，且這種型態可以記錄位置
		RandomAccessFile Raf = new RandomAccessFile(zipfile , "rw");
		// 建立其Channel，因為需知道資料大小、且使用map()方法映入資料。
		FileChannel fChannel = Raf.getChannel();
		// 每個切割檔的大小
		// 建立每個容器的Channel
		FileChannel[] sp_Channel = new FileChannel[f.sub_number];
		// 取得每個容器的Channel，準備用對"映"的寫入資料
		for (int i = 0; i < f.sub_number; i++) 
			sp_Channel[i] = fos[i].getChannel();
		
		// 映射緩衝區(存放要"映"入的資料)
		MappedByteBuffer[] m_buf = new MappedByteBuffer[f.sub_number];
		// 每個切割檔案大小()
		long each_size = fChannel.size() / f.sub_number;
		long index = 0, sum = 0;
		// 寫入資料
		for (int i = 0; i < f.sub_number; i++) {
			// 最後一次執行的大小(總大小)-累積大小
			if (i == f.sub_number - 1) each_size = fChannel.size() - sum;
			// 累積大小(目的是處理最後一個切割檔案大小可能不符的問題)
			sum += each_size;
			// 映射資料到每個容器裡(模式,位置,大小)
			m_buf[i] = fChannel.map(FileChannel.MapMode.READ_ONLY, index,
					each_size);
			// 紀錄位置(位置每次都是增加一個 "切割容器大小" )
			index += fChannel.size() / f.sub_number;
			// 將映射緩衝區的資料寫入容器的Channel
			sp_Channel[i].write(m_buf[i]);
			// 寫入後就關閉
			sp_Channel[i].close();
			fos[i].close();
		}
		fChannel.close();
		Raf.close();
		return sp_file;
	}
}
