import java.net.*;
import java.io.*;
import javax.swing.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class MyClient extends JFrame implements MouseListener,MouseMotionListener {
	private JButton buttonArray[][]; //�{�^���p�̔z��
	private JButton passButton;
	private int myColor, x, y;
	private Container c;
	private ImageIcon myIcon, yourIcon;
	private int myTurn;
	private ImageIcon blackIcon, whiteIcon, boardIcon;
	PrintWriter out;//�o�͗p�̃��C�^�[
	private int pass_count;

	public MyClient() {
		//���O�̓��̓_�C�A���O���J��
		String myName = JOptionPane.showInputDialog(null,"���O����͂��Ă�������","���O�̓���",JOptionPane.QUESTION_MESSAGE);
		if(myName.equals("")){
			myName = "No name";//���O���Ȃ��Ƃ��́C"No name"�Ƃ���
		}
		String IPname = JOptionPane.showInputDialog(null,"�T�[�o�[�̖��O����͂��Ă�������","�T�[�o�[�̓���",JOptionPane.QUESTION_MESSAGE);
		if(IPname.equals("") || IPname == null){
			IPname = "localhost";
		}
		//�E�B���h�E���쐬����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�E�B���h�E�����Ƃ��ɁC����������悤�ɐݒ肷��
		setTitle("MyClient");//�E�B���h�E�̃^�C�g����ݒ肷��
		setSize(800,600);//�E�B���h�E�̃T�C�Y��ݒ肷��
		c = getContentPane();//�t���[���̃y�C�����擾����

		//�A�C�R���̐ݒ�
		whiteIcon = new ImageIcon("White.jpg");
		blackIcon = new ImageIcon("Black.jpg");
		boardIcon = new ImageIcon("GreenFrame.jpg");

		c.setLayout(null);//�������C�A�E�g�̐ݒ���s��Ȃ�
		//�{�^���̐���
		buttonArray = new JButton[8][8];//�{�^���̔z����T�쐬����[0]����[8]�܂Ŏg����
		for(int j=0;j<8;j++){
			for(int i=0; i<8; i++){
				buttonArray[j][i] = new JButton(boardIcon);//�{�^���ɃA�C�R����ݒ肷��
				c.add(buttonArray[j][i]);//�y�C���ɓ\��t����
				buttonArray[j][i].setBounds(i*50+10,j*50+10,50,50);//�{�^���̑傫���ƈʒu��ݒ肷��D(x���W�Cy���W,x�̕�,y�̕��j
				buttonArray[j][i].addMouseListener(this);//�{�^�����}�E�X�ł�������Ƃ��ɔ�������悤�ɂ���
				buttonArray[j][i].addMouseMotionListener(this);//�{�^�����}�E�X�œ��������Ƃ����Ƃ��ɔ�������悤�ɂ���
				buttonArray[j][i].setActionCommand(Integer.toString((8*j+i)));//�{�^���ɔz��̏���t������i�l�b�g���[�N����ăI�u�W�F�N�g�����ʂ��邽�߁j
			}
		}
		//�����̃{�^���z�u//
		buttonArray[3][3].setIcon(blackIcon);
		buttonArray[4][4].setIcon(blackIcon);
		buttonArray[3][4].setIcon(whiteIcon);
		buttonArray[4][3].setIcon(whiteIcon);
		//�p�X�{�^���̍쐬
		passButton = new JButton("�p�X");
		passButton.setBounds(500,280,180,100);
		passButton.addMouseListener(this);//�{�^�����}�E�X�ł�������Ƃ��ɔ�������悤�ɂ���
		passButton.setActionCommand("PASS");
		c.add(passButton);
		//�{�^���ɔz��̏���t������i�l�b�g���[�N����ăI�u�W�F�N�g�����ʂ��邽�߁j
			
		//�T�[�o�ɐڑ�����
		Socket socket = null;
		try {
			//"localhost"�́C���������ւ̐ڑ��Dlocalhost��ڑ����IP Address�i"133.42.155.201"�`���j�ɐݒ肷��Ƒ���PC�̃T�[�o�ƒʐM�ł���
			//10000�̓|�[�g�ԍ��DIP Address�Őڑ�����PC�����߂āC�|�[�g�ԍ��ł���PC�㓮�삷��v���O��������肷��
			socket = new Socket(IPname, 10000);
		} catch (UnknownHostException e) {
			System.err.println("�z�X�g�� IP �A�h���X������ł��܂���: " + e);
		} catch (IOException e) {
			 System.err.println("�G���[���������܂���: " + e);
		}
		
		MesgRecvThread mrt = new MesgRecvThread(socket, myName);//��M�p�̃X���b�h���쐬����
		mrt.start();//�X���b�h�𓮂����iRun�������j
	}
		
	//���b�Z�[�W��M�̂��߂̃X���b�h
	public class MesgRecvThread extends Thread {
		
		Socket socket;
		String myName;
		
		public MesgRecvThread(Socket s, String n){
			socket = s;
			myName = n;
		}
		
		//�ʐM�󋵂��Ď����C��M�f�[�^�ɂ���ē��삷��
		public void run() {
			try{
				InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(sisr);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(myName);//�ڑ��̍ŏ��ɖ��O�𑗂�
				String myNumberStr = br.readLine();
				int myNumberInt = Integer.parseInt(myNumberStr);
				if(myNumberInt % 2 == 0){//�^�[���ƃR�}�̐F�����߂�
					myTurn = 0;
					myIcon = blackIcon;
			        yourIcon = whiteIcon;
					myColor = 0;
				}
				else{
					myIcon=whiteIcon;
					yourIcon=blackIcon;
					myColor = 1;
					myTurn = 1;
				}
				while(true) {
					String inputLine = br.readLine();//�f�[�^����s�������ǂݍ���ł݂�
					if (inputLine != null) {//�ǂݍ��񂾂Ƃ��Ƀf�[�^���ǂݍ��܂ꂽ���ǂ������`�F�b�N����
						System.out.println(inputLine);//�f�o�b�O�i����m�F�p�j�ɃR���\�[���ɏo�͂���
						String[] inputTokens = inputLine.split(" ");	//���̓f�[�^����͂��邽�߂ɁA�X�y�[�X�Ő؂蕪����
						String cmd = inputTokens[0];//�R�}���h�̎��o���D�P�ڂ̗v�f�����o��
						if(cmd.equals("MOVE")){//cmd�̕�����"MOVE"�����������ׂ�D��������true�ƂȂ�
							//MOVE�̎��̏���(�R�}�̈ړ��̏���)
							String theBName = inputTokens[1];//�{�^���̖��O�i�ԍ��j�̎擾
							int theBnum = Integer.parseInt(theBName);//�{�^���̖��O�𐔒l�ɕϊ�����
							int x = Integer.parseInt(inputTokens[2]);//���l�ɕϊ�����
							int y = Integer.parseInt(inputTokens[3]);//���l�ɕϊ�����
							int j = theBnum / 8;
							int i = theBnum % 8;
							buttonArray[j][i].setLocation(x,y);//�w��̃{�^�����ʒu��x,y�ɐݒ肷��
						}
						if(cmd.equals("PLACE")){//cmd�̕�����"PLACE"�����������ׂ�D��������true�ƂȂ�
							//PLACE�̎��̏���(�R�}�̈ړ��̏���)
							String theBName = inputTokens[1];//�{�^���̖��O�i�ԍ��j�̎擾
							int theBnum = Integer.parseInt(theBName);//�{�^���̖��O�𐔒l�ɕϊ�����
							myTurn = 1 - myTurn;
							int theColor = Integer.parseInt(inputTokens[2]);//���l�ɕϊ�����
							int j = theBnum / 8;
							int i = theBnum % 8;
							if (theColor == myColor){
								buttonArray[j][i].setIcon(myIcon);
							}
							else{
								buttonArray[j][i].setIcon(yourIcon);
							}
							pass_count = 0;
						}
						if(cmd.equals("FLIP")){//cmd�̕�����"FLIP"�����������ׂ�D��������true�ƂȂ�
							//FLIP�̎��̏���(�R�}�̈ړ��̏���)
							String theBName = inputTokens[1];//�{�^���̖��O�i�ԍ��j�̎擾
							int theBnum = Integer.parseInt(theBName);//�{�^���̖��O�𐔒l�ɕϊ�����
							int theColor = Integer.parseInt(inputTokens[2]);//���l�ɕϊ�����
							int j = theBnum / 8;
							int i = theBnum % 8;
							if (theColor == myColor){//�u�����{�^���̏���
								buttonArray[j][i].setIcon(myIcon);
							}
							else{
								buttonArray[j][i].setIcon(yourIcon);
							}
						}
						if(cmd.equals("PASS")){//�p�X�{�^���������ꂽ�ꍇ�̏���
							pass_count += 1;//�p�X�J�E���g��1���₷
							if (pass_count >1){
								check_count();//�p�X�J�E���g��2�ȏ�̏ꍇ�R�}�𐔂���B
								break;
							}
							myTurn = 1 - myTurn;
						}
						if(check()){
							check_count();
							break;
						}
					}else{
						break;
					}
				
				}
				
				socket.close();
			} catch (IOException e) {
				System.err.println("�G���[���������܂���: " + e);
			}
		}
	}

	public static void main(String[] args) {
		MyClient net = new MyClient();
		net.setVisible(true);
	}
  	
	public void mouseClicked(MouseEvent e) {//�{�^�����N���b�N�����Ƃ��̏���
		if (myTurn == 0){
			System.out.println("�N���b�N");
			JButton theButton = (JButton)e.getComponent();//�N���b�N�����I�u�W�F�N�g�𓾂�D�^���Ⴄ�̂ŃL���X�g����
			String theArrayIndex = theButton.getActionCommand();//�{�^���̔z��̔ԍ������o��
			Icon theIcon = theButton.getIcon();//theIcon�ɂ́C���݂̃{�^���ɐݒ肳�ꂽ�A�C�R��������
			if (theArrayIndex.equals("PASS")){
				String msg = "PASS";

				//�T�[�o�ɏ��𑗂�
				out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
				out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
			}
			if (theIcon == boardIcon){
				int temp = Integer.parseInt(theArrayIndex); //int�^�ɕϊ�
				int y = temp / 8;
				int x = temp % 8;
				if(judgeButton(x, y)){//���Ԃ�����̂����邩�m�F
					System.out.println(theIcon);//�f�o�b�O�i�m�F�p�j�ɁC�N���b�N�����A�C�R���̖��O���o�͂���

					repaint();//��ʂ̃I�u�W�F�N�g��`�悵����
					//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
					String msg = "PLACE"+" "+theArrayIndex+" "+myColor;

					//�T�[�o�ɏ��𑗂�
					out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
					out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
				}
				else{
					System.out.println("�����ɂ͔z�u�ł��܂���");
				}
			}
		}

	}
	
	public void mouseEntered(MouseEvent e) {//�}�E�X���I�u�W�F�N�g�ɓ������Ƃ��̏���
		// System.out.println("�}�E�X��������");
	}
	
	public void mouseExited(MouseEvent e) {//�}�E�X���I�u�W�F�N�g����o���Ƃ��̏���
		// System.out.println("�}�E�X�E�o");
	}
	
	public void mousePressed(MouseEvent e) {//�}�E�X�ŃI�u�W�F�N�g���������Ƃ��̏����i�N���b�N�Ƃ̈Ⴂ�ɒ��Ӂj
		// System.out.println("�}�E�X��������");
	}
	
	public void mouseReleased(MouseEvent e) {//�}�E�X�ŉ����Ă����I�u�W�F�N�g�𗣂����Ƃ��̏���
		// System.out.println("�}�E�X�������");
	}
	
	public void mouseDragged(MouseEvent e) {//�}�E�X�ŃI�u�W�F�N�g�Ƃ��h���b�O���Ă���Ƃ��̏���
		/*
		System.out.println("�}�E�X���h���b�O");
		JButton theButton = (JButton)e.getComponent();//�^���Ⴄ�̂ŃL���X�g����
		String theArrayIndex = theButton.getActionCommand();//�{�^���̔z��̔ԍ������o��
		if (!theArrayIndex.equalsIgnoreCase("0")){
			Point theMLoc = e.getPoint();//�������R���|�[�l���g����Ƃ��鑊�΍��W
			System.out.println(theMLoc);//�f�o�b�O�i�m�F�p�j�ɁC�擾�����}�E�X�̈ʒu���R���\�[���ɏo�͂���
			Point theBtnLocation = theButton.getLocation();//�N���b�N�����{�^�������W���擾����
			theBtnLocation.x += theMLoc.x-15;//�{�^���̐^�񒆓�����Ƀ}�E�X�J�[�\��������悤�ɕ␳����
			theBtnLocation.y += theMLoc.y-15;//�{�^���̐^�񒆓�����Ƀ}�E�X�J�[�\��������悤�ɕ␳����
			theButton.setLocation(theBtnLocation);//�}�E�X�̈ʒu�ɂ��킹�ăI�u�W�F�N�g���ړ�����
	 
			//���M�����쐬����i��M���ɂ́C���̑��������ԂɃf�[�^�����o���D�X�y�[�X���f�[�^�̋�؂�ƂȂ�j
			String msg = "MOVE"+" "+theArrayIndex+" "+theBtnLocation.x+" "+theBtnLocation.y;

			//�T�[�o�ɏ��𑗂�
			out.println(msg);//���M�f�[�^���o�b�t�@�ɏ����o��
			out.flush();//���M�f�[�^���t���b�V���i�l�b�g���[�N��ɂ͂��o���j����
		}
		repaint();//�I�u�W�F�N�g�̍ĕ`����s��
		*/
	}

	public void mouseMoved(MouseEvent e) {//�}�E�X���I�u�W�F�N�g��ňړ������Ƃ��̏���
		/*
		System.out.println("�}�E�X�ړ�");
		int theMLocX = e.getX();//�}�E�X��x���W�𓾂�
		int theMLocY = e.getY();//�}�E�X��y���W�𓾂�
		System.out.println(theMLocX+","+theMLocY);//�R���\�[���ɏo�͂���
		*/
	}
	//�Ԃ���R�}�����邩���m�F����֐�
	public boolean judgeButton(int x, int y){
		boolean flag = false;
		//���ׂĂ̕����ɂ�����R�}������m�F
		for(int j=-1;j<1;j++){
			for(int i=-1; i<1; i++){
				if(y+j<0 || 8<=y+j || x+i<0 || 8<=x+i){//�Q�����z������Q�Ƃ��Ă���ꍇ�͑�����
					continue;
				}
				if(1<=flipButtons(x,y,i,j)){//����ꍇ��flag=true�ɂ���
					flag = true;
				}
			}
		}
		return flag;
	}
	//�����Ԃ��閇�������邩������֐�
	public int flipButtons(int x, int y, int i, int j){
		int flipNum = 0;//�����Ԃ��邩��ۑ�
		if (i==0 && j==0){//�������Q�Ƃ��Ă���ꍇ�͒��f
			return 0;
		}
		for(int dy=j, dx=i; ; dy+=j, dx+=i){//i��j�̕����ւ̒T����i�߂�B
			int ydy=dy+y;
			int xdx=dx+x;
			if(ydy<0 || 8<=ydy || xdx<0 || 8<=xdx){//�Q�����z������Q�Ƃ��Ȃ��ꍇ�͏����𒆒f
				return 0;
			}
			Icon theIcon = buttonArray[ydy][xdx].getIcon();//i��j������Icon���m�F
			if(theIcon == boardIcon){//�{�[�h�A�C�R���̏ꍇ�͏����𒆒f
				return 0;
			}
			if(theIcon == myIcon){
				if(flipNum >= 1){//1���ł��Ԃ��閇��������Ώ��𑗂�
					for(int msm=j, sms=i, k=0; k<flipNum; k++, msm+=j, sms+=i){
						//�{�^���̈ʒu�������
						int msgy = y + msm;
						int msgx = x + sms;
						int theArrayIndex = msgy*8 + msgx;
						//�T�[�o�ɏ��𑗂�
						String msg = "FLIP"+" "+theArrayIndex+" "+myColor;
						out.println(msg);
						out.flush();
					}
				}
				return flipNum;
			}
			if(theIcon == yourIcon){//youIcon�Ȃ�flipnum�𑝉�
				flipNum++;
			}
		}
	}
	//�{�[�h�A�C�R�������邩���m�F����֐�
	public boolean check(){//�{�[�h�A�C�R�������邩���m�F����
		boolean flag = true;
		Icon theIcon = null;
		//���ׂẴ}�X�ڂ��m�F���Ă���
		for (int i=0; i<8; i++){
			for (int j=0; j<8; j++){
				theIcon = buttonArray[i][j].getIcon();//�}�X�ڂ��m�F
				if (theIcon == boardIcon){//�{�[�h�A�C�R��������ꍇflag��false�ɐݒ�
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
	//�Ō�ɃR�}�𐔂���֐�
	public void check_count(){
		//���ꂼ���������
		int white = 0;
		int black = 0;
		Icon theIcon = null;
		//���ׂẴ}�X�ڂ�T��
		for (int i=0; i<8; i++){
			for (int j=0; j<8; j++){
				theIcon = buttonArray[i][j].getIcon();
				//���ꂼ��̏ꍇ�ŃJ�E���g�𑝂₷
				if (theIcon == whiteIcon){
					white++;
				}
				else if (theIcon == blackIcon){
					black++;
				}
			}
		}
		//���s�̏������R���s��
		if (white < black){
			System.out.println("���̃R�}�̏����ł�");
		}
		else if (white > black){
			System.out.println("���̃R�}�̏����ł�");
		}
		else{
			System.out.println("���������ł�");
		}
	}
}
