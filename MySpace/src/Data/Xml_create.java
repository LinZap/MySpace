package Data;

import java.io.FileOutputStream;
import java.io.IOException;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import System.System_parameters;

public class Xml_create {

	private Document document = null;
	private String path;
	private Element r;

	public void create(String path) throws IOException {

		this.path = path;
		this.document = DocumentHelper.createDocument();
		this.document.setXMLEncoding("utf-8");
		r = this.document.addElement("root");

		// 初步設定 [Myspace]act.xml 的資料  space="0"
		if (this.path.equals(System_parameters.get_db_path(2))) {

			Element account = r.addElement("account");

			account.addAttribute("space", "0");

			Element act = account.addElement("act");
			Element psd = account.addElement("psd");

			act.setText(System_parameters.root_act);
			psd.setText(System_parameters.root_psd);
		}

		// 初步設定 [Myspace]dir.xml 的資料 //name="MySpace"
		else if (this.path.equals(System_parameters.get_db_path(1))) {
			r.addAttribute("name", "MySpace");

			Element dir = r.addElement("dir");
			dir.addAttribute("name", "檔案接收櫃");
		}

		save();
	}

	private synchronized void save() throws IOException {
		XMLWriter writer = new XMLWriter(new FileOutputStream(path),
				OutputFormat.createPrettyPrint());
		writer.write(document);
		writer.close();
	}
}
