package System;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Favorites_Link {

	public void create_Favorite_Link() {

		try {
			// C槽下沒有資料夾時建立
			File dir = new File(System_parameters.system_dir_path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			// D槽下沒有程式需要的ico檔時建立，將主程式目錄下的圖案複製過去
			File newico = new File("C:\\MySpace\\cat.ico");
			if (!newico.exists()) {

				File oldico = new File(System_parameters.system_icon_path);
				BufferedInputStream in = new BufferedInputStream(
						new FileInputStream(oldico));
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(newico));
				byte[] tmp = new byte[1];
				while (in.read(tmp) != -1)
					out.write(tmp);
				in.close();
				out.close();
			}

			File f = new File("vbs\\NewLink_icon.vbs");

			Runtime run = Runtime.getRuntime();
			String[] arg = new String[3];

			arg[0] = "cmd";
			arg[1] = "/c";
			arg[2] = "Explorer.exe /n , /open," + f.getAbsolutePath();

			run.exec(arg);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
