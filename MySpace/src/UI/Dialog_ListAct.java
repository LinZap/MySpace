package UI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.dom4j.DocumentException;


import Data.Xml_act;

public class Dialog_ListAct extends JDialog implements ActionListener {

	
	private static final long serialVersionUID = 1L;
	private JTable table;
	private Object[] columnTitle = { " ","帳號", "密碼" };
	private Object[][] columnContext;
	private JScrollPane js;
	private Model_Act ma ;
	
	
	public Dialog_ListAct() throws DocumentException{
		
		super(UI_main.main_frame, "新增的帳戶", true);
		setSize(440, 340);
		setLocationRelativeTo(null);
		
		JPanel hint = new JPanel(new FlowLayout(FlowLayout.CENTER,0,3));
		JLabel txt = new JLabel("帳戶清單");
		hint.add(txt);
		
		
		Xml_act xa = new Xml_act();
		columnContext = xa.read_account();
		ma = new Model_Act(columnContext,columnTitle);
		
		
		JPanel tpa = new JPanel(new FlowLayout());
		table = new JTable(ma);
		js = new JScrollPane(table);	
		js.setPreferredSize(new Dimension(420, 300));
		tpa.add(js);

		add(hint);
		add(tpa);
		
		TableColumn nameColumn = table.getColumn(columnTitle[0]);
		nameColumn.setMinWidth(30);
		nameColumn.setMaxWidth(30);
		js.setViewportView(table);
		
		setVisible(true);
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {


		
	}

}
