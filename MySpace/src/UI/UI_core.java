package UI;


/**
 * 
 * �o��class�t�d�D����
 * �D���� ��3�ӥD�n���s �u���ݪŶ��v�B�u�b��޲z�v�B�u���n�]�w�v
 * �D�e���t�d�I�s3�ӫ��s�U�۪������A�O�@�Ӿɤު�����
 * 
 * */

public class UI_core  {
	/*
	private JButton dive, act, set;
	public static JPanel ui_core, ui_dive, ui_act, ui_set;
	public static ImageIcon img_dive, img_act, img_set, img_line;
	private static final long serialVersionUID = 1L;

	public UI_core() throws DocumentException {
		ui_core = this;
		// �e���]�w
		this.setLayout(new FlowLayout(1, 0, 45));
		this.setBackground(Color.white);

		// �]�m���s�W�����Ϥ��귽
		img_dive = new ImageIcon("dive.fw.png");
		img_act = new ImageIcon("act.fw.png");
		img_set = new ImageIcon("set.fw.png");
		img_line = new ImageIcon("line.fw.png");

		// �򥻤���
		JLabel line1 = new JLabel(img_line);
		JLabel line2 = new JLabel(img_line);
		dive = new RButton("�����ɮ�", img_dive);
		act = new RButton("�޲z�Ŷ�", img_act);
		set = new RButton("���n�]�w", img_set);
		dive.setFont(new Font("�L�n������", Font.PLAIN, 16));
		act.setFont(new Font("�L�n������", Font.PLAIN, 16));
		set.setFont(new Font("�L�n������", Font.PLAIN, 16));
		

		// �]�m���s�j�p
		dive.setPreferredSize(new Dimension(150, 270));
		act.setPreferredSize(new Dimension(150, 270));
		set.setPreferredSize(new Dimension(150, 270));
		
		 //���e�m���A���s�Ϥ�]�m�����ƦC
		dive.setHorizontalTextPosition(SwingConstants.CENTER);
		dive.setVerticalTextPosition(SwingConstants.BOTTOM);
		act.setHorizontalTextPosition(SwingConstants.CENTER);
		act.setVerticalTextPosition(SwingConstants.BOTTOM);
		set.setHorizontalTextPosition(SwingConstants.CENTER);
		set.setVerticalTextPosition(SwingConstants.BOTTOM);

		
		// �N����s�W��e��( ���s1 + ����u + ���s2 + ���j�u + ���s3 )
		add(dive);
		add(line1);
		add(act);
		add(line2);
		add(set);

		// �s�W�C�ӫ��s��Ĳ�o�ƥ�
		dive.addActionListener(this);
		act.addActionListener(this);
		set.addActionListener(this);

		
		//�i�J�D�e����A�ߨ��@3�Ӥl�e��
		// ��@�����Jmain_Container�A���Ȯɥ����å���
		// �o�ǵe������3�ӫ��s���ե�
		ui_dive = new UI_drive();
		ui_act = new UI_act();
		ui_set = new UI_set();
		
	
	

	}

	// �o���@3�ӫ��s�ӭt�d���\��
	public void actionPerformed(ActionEvent e) {

		// �I�sUI_dive(���ݪŶ�)�����A�����åثe������
		if (e.getSource() == dive) {
			setVisible(false);
			UI_main.main_Container.add(ui_dive);
			ui_dive.setVisible(true);
			
		}

		// �I�sUI_act(�b��޲z)�����A�����åثe������
		if (e.getSource() == act) {
			setVisible(false);
			UI_main.main_Container.add(ui_act);
			ui_act.setVisible(true);
		
		}

		// �I�sUI_set(���n�]�w)�����A�����åثe������
		if (e.getSource() == set) {
			setVisible(false);
			UI_main.main_Container.add(ui_set);
			ui_set.setVisible(true);
		
		}

	}*/
}