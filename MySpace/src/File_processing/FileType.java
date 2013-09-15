package File_processing;

import java.io.File;

public class FileType {

	public String Bulid_FileType(File f){
		String type = getTypeName(f);
		if (f.isDirectory() && type.equals("dir"))
			return "dir";
		else if (type.equals("png") || type.equals("jpg")
				|| type.equals("jpeg") || type.equals("gif"))
			return "jpg";
		else if (type.equals("zip") || type.equals("7z") || type.equals("rar"))
			return "zip";
		else if (type.equals("ppt") || type.equals("pptx"))
			return "ppt";
		else if (type.equals("xlsx") || type.equals("xls"))
			return "xlsx";
		else if (type.equals("txt") || type.equals("rtf"))
			return "txt";
		else if (type.equals("java") || type.equals("jar"))
			return "java";
		else
			return type;
	}
	
	// 取得檔案類型名稱 (存到Xml要用)
	private String getTypeName(File f) {
		if (!f.isDirectory()) {
			String sp[] = f.getName().split("\\.");
			return sp[sp.length - 1];
		} else {
			return "dir";
		}
	}

}
