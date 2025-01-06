import java.net.*;
import java.io.*;
import javax.swing.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Random;

public class MyClient1 extends JFrame implements MouseListener,MouseMotionListener {
	private JButton buttonArray[][]; //�{�^���p�̔z��
	private JButton passButton;
	private int myColor, x, y;
	private Container c;
	private ImageIcon myIcon, yourIcon;
	private int myTurn;
	private ImageIcon blackIcon, whiteIcon, boardIcon;
	PrintWriter out;//�o�͗p�̃��C�^�[
	private int pass_count;

	public MyClient1() {
		//���O�̓��̓_�C�A���O���J��
		String myName = JOptionPane.showInputDialog(null,"���O����͂��Ă�������","���O�̓���",JOptionPane.QUESTION_MESSAGE);
		if(myName.equals("")){
			myName = "No name";//���O���Ȃ��Ƃ��́C"No name"�Ƃ���
		}
		String serverIP = JOptionPane.showInputDialog(null,"�T�[�o�[�̖��O����͂��Ă�������","�T�[�o�[�̓���",JOptionPane.QUESTION_MESSAGE);
		if(serverIP.equals("") || serverIP == null){
			serverIP = "localhost";
		}
		//�E�B���h�E���쐬����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�E�B���h�E�����Ƃ��ɁC����������悤�ɐݒ肷��
		setTitle("MyClient1");//�E�B���h�E�̃^�C�g����ݒ肷��
		setSize(800,600);//�E�B���h�E�̃T�C�Y��ݒ肷��
		c = getContentPane();//�t���[���̃y�C�����擾����

		//�A�C�R���̐ݒ�
		pistol = new ImageIcon("pistol.jpg");
		mouse = new ImageIcon("Mickey.jpg");
		boardIcon = new ImageIcon("GreenFrame.jpg");

		c.setLayout(null);//�������C�A�E�g�̐ݒ���s��Ȃ�
		//�̗�
		int myLife = 3;
		int yourLife = 3;
		
		
		//�p�X�{�^���̍쐬
		shotButton = new JButton("����ɑł�");
		shotButton.setBounds(525,225,150,80);
		shotButton.addMouseListener(this);//�{�^�����}�E�X�ł�������Ƃ��ɔ�������悤�ɂ���
		shotButton.setActionCommand("Shot");
		c.add(shotButton);
		
		passButton = new JButton("�����ɑł�");
		passButton.setBounds(525,225,180,120);
		passButton.addMouseListener(this);//�{�^�����}�E�X�ł�������Ƃ��ɔ�������悤�ɂ���
		passButton.setActionCommand("PASS");
		c.add(passButton);
		//�{�^���ɔz��̏���t������i�l�b�g���[�N����ăI�u�W�F�N�g�����ʂ��邽�߁j
			
		//�T�[�o�ɐڑ�����
		Socket socket = null;
		try {
			//"localhost"�́C���������ւ̐ڑ��Dlocalhost��ڑ����IP Address�i"133.42.155.201"�`���j�ɐݒ肷��Ƒ���PC�̃T�[�o�ƒʐM�ł���
			//10000�̓|�[�g�ԍ��DIP Address�Őڑ�����PC�����߂āC�|�[�g�ԍ��ł���PC�㓮�삷��v���O��������肷��
			socket = new Socket(serverIP, 10000);
		} catch (UnknownHostException e) {
			System.err.println("�z�X�g�� IP �A�h���X������ł��܂���: " + e);
		} catch (IOException e) {tg
			 System.err.println("�G���[���������܂���: " + e);
		}
		
		MesgRecvThread mrt = new MesgRecvThread(socket, myName);//��M�p�̃X���b�h���쐬����
		mrt.start();//�X���b�h�𓮂����iRun�������j
		//�m������
		public void Bullet(){
			Random rand = new Random();
			all_bullet = 6;
			int Gun = rand.nextInt(5) + 1;
			non_bullet = all_bullet - Gun;
		}
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
				}
				else{
					myTurn = 1;
				}
				while(true) {
					String inputLine = br.readLine();//�f�[�^����s�������ǂݍ���ł݂�
					if (inputLine != null) {//�ǂݍ��񂾂Ƃ��Ƀf�[�^���ǂݍ��܂ꂽ���ǂ������`�F�b�N����
						System.out.println(inputLine);//�f�o�b�O�i����m�F�p�j�ɃR���\�[���ɏo�͂���
						String[] inputTokens = inputLine.split(" ");	//���̓f�[�^����͂��邽�߂ɁA�X�y�[�X�Ő؂蕪����
						String cmd = inputTokens[0];//�R�}���h�̎��o���D�P�ڂ̗v�f�����o��
						}

						if(cmd.equals("Shot")){//cmd�̕�����"Shot"�����������ׂ�D��������true�ƂȂ�
							//Shot�̎��̏���(�R�}�̈ړ��̏���)
							String theBName = inputTokens[1];//�{�^���̖��O�i�ԍ��j�̎擾
							int theBnum = Integer.parseInt(theBName);//�{�^���̖��O�𐔒l�ɕϊ�����
							myTurn = 1 - myTurn;
							if ( == Gun){
								
							}
							else{
								buttonArray[j][i].setIcon(yourIcon);
							}
							pass_count = 0;
						}
						}
						if(cmd.equals("PASS")){//�p�X�{�^���������ꂽ�ꍇ�̏���
							pass_count += 1;//�p�X�J�E���g��1���₷
							
							if (pass_count >1){
								Count_board();//�p�X�J�E���g��2�ȏ�̏ꍇ�R�}�𐔂���B
								break;
							}
							myTurn = 1 - myTurn;
						}
						if(kakuninn()){
							Count_board();
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
		MyClient1 net = new MyClient1();
		net.setVisible(true);
	}
  	
	public void mouseClicked(MouseEvent e) {//�{�^�����N���b�N�����Ƃ��̏���
		if (myTurn == 0){
			System.out.println("�N���b�N");
			JButton theButton = (JButton)e.getComponent();//�N���b�N�����I�u�W�F�N�g�𓾂�D�^���Ⴄ�̂ŃL���X�g����
			String theArrayIndex = theButton.getActionCommand();//�{�^���̔z��̔ԍ������o��
			
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
					String msg = "Shot"+" "+theArrayIndex+" "+myColor;

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
		for(int j=-1;j<2;j++){
			for(int i=-1; i<2; i++){
				if(y+j<0 || 7<y+j || x+i<0 || 7<x+i){//�Q�����z������Q�Ƃ��Ă���ꍇ�͑�����
					continue;
				}
				if(flipButtons(x,y,i,j) >= 1){//����ꍇ��flag=true�ɂ���
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
			int a=dy+y;
			int b=dx+x;
			System.out.println("a=" + a + " b=" + b);
			if(a<0 || 7<a || b<0 || 7<b){//�Q�����z������Q�Ƃ��Ȃ��ꍇ�͏����𒆒f
				return 0;
			}
			Icon theIcon = buttonArray[a][b].getIcon();//i��j������Icon���m�F
			if(theIcon == boardIcon){//�{�[�h�A�C�R���̏ꍇ�͏����𒆒f
				return 0;
			}
			if(theIcon == myIcon){
				if(flipNum >= 1){//1���ł��Ԃ��閇��������Ώ��𑗂�
					for(int ddy=j, ddx=i, k=0; k<flipNum; k++, ddy+=j, ddx+=i){
						//�{�^���̈ʒu�������
						int msgy = y + ddy;
						int msgx = x + ddx;
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
	public boolean kakuninn(){//�{�[�h�A�C�R�������邩���m�F����
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
	public void Count_board(){
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
		if (yourLife < 0){
			System.out.println("You win");
		}else{
			System.out.println("You lose")
		}
	}
}
