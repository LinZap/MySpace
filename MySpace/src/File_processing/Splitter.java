package File_processing;

/*
 * �o�ӰƵ{���D�n�ΨӤ��λP�X���ɮ�
 * �ǤJ���ѼƦ��G�n�B�z���ɮ׻P���μƶq
 * */
import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;

import Data.Obj_File;

public class Splitter {
		
	public static synchronized File[] split(Obj_File f,File zipfile) throws FileNotFoundException, IOException {
		
		// �إߦs����Ϊ��ɮ׮e��(�ƶq)
		File[] sp_file = new File[f.sub_number];
		// �إߧ��Ƽg�i�e������y(�h��)
		FileOutputStream[] fos = new FileOutputStream[f.sub_number];
		
		// �إߤ����ɮת��e��tmp(name.zip( i ).tmp)
		for (int i = 0; i < f.sub_number; i++) {
			// ���o�����ɦW
			System.out.println("�����ɦW: "+f.store[i][0]);
			sp_file[i] = new File(f.store[i][0]);
			fos[i] = new FileOutputStream(sp_file[i], true);
			
		}

		// �إ߿�J�ɮת�RandomAccessFile�A
		// �]���ݭn�άM�g���覡������ư����ΡA�B�o�ث��A�i�H�O����m
		RandomAccessFile Raf = new RandomAccessFile(zipfile , "rw");
		// �إߨ�Channel�A�]���ݪ��D��Ƥj�p�B�B�ϥ�map()��k�M�J��ơC
		FileChannel fChannel = Raf.getChannel();
		// �C�Ӥ����ɪ��j�p
		// �إߨC�Ӯe����Channel
		FileChannel[] sp_Channel = new FileChannel[f.sub_number];
		// ���o�C�Ӯe����Channel�A�ǳƥι�"�M"���g�J���
		for (int i = 0; i < f.sub_number; i++) 
			sp_Channel[i] = fos[i].getChannel();
		
		// �M�g�w�İ�(�s��n"�M"�J�����)
		MappedByteBuffer[] m_buf = new MappedByteBuffer[f.sub_number];
		// �C�Ӥ����ɮפj�p()
		long each_size = fChannel.size() / f.sub_number;
		long index = 0, sum = 0;
		// �g�J���
		for (int i = 0; i < f.sub_number; i++) {
			// �̫�@�����檺�j�p(�`�j�p)-�ֿn�j�p
			if (i == f.sub_number - 1) each_size = fChannel.size() - sum;
			// �ֿn�j�p(�ت��O�B�z�̫�@�Ӥ����ɮפj�p�i�ण�Ū����D)
			sum += each_size;
			// �M�g��ƨ�C�Ӯe����(�Ҧ�,��m,�j�p)
			m_buf[i] = fChannel.map(FileChannel.MapMode.READ_ONLY, index,
					each_size);
			// ������m(��m�C�����O�W�[�@�� "���ήe���j�p" )
			index += fChannel.size() / f.sub_number;
			// �N�M�g�w�İϪ���Ƽg�J�e����Channel
			sp_Channel[i].write(m_buf[i]);
			// �g�J��N����
			sp_Channel[i].close();
			fos[i].close();
		}
		fChannel.close();
		Raf.close();
		return sp_file;
	}
}
