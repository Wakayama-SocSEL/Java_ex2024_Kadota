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
		String serverIP = JOptionPane.showInputDialog(null,"�T�[�o�[�̖��O����͂��Ă�������","�T�[�o�[�̓���",JOptionPane.QUESTION_MESSAGE);
		if(serverIP.equals("") || serverIP == null){
			serverIP = "localhost";
		}
		//�E�B���h�E���쐬����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�E�B���h�E�����Ƃ��ɁC����������悤�ɐݒ肷��
		setTitle("MyClient");//�E�B���h�E�̃^�C�g����ݒ肷��
		setSize(800,600);//�E�B���h�E�̃T�C�Y��ݒ肷��
		c = getContentPane();//�t���[���̃y�C�����擾����

		//�A�C�R���̐ݒ�
		twice = new ImageIcon("twice.jpg");
		watch = new ImageIcon("Black.jpg");
		joker = new ImageIcon("GreenFrame.jpg");
		heal = new ImageIcon("heal.jpg");
		watch = new ImageIcon("Black.jpg");

		c.setLayout(null);//�������C�A�E�g�̐ݒ���s��Ȃ�
		public static void main(String[] args) {
			Random rand = new Random();
			int all_bullet = 6;
			int Gun_bullet = rand.nextInt(all_bullet) + 1;
			List<String>chamber = new ArrayList<String>();
		
		for(int i = 0; i < Gun_bullet; i++){
			chamber.add("1");
		}
		for(int i = 0; i < all_bullet - Gun_bullet; i++){
			chamber.add("0");
		}
		Collections.shuffle(chamber);
		int cnt = 0;
		for(String str: chamber){
			if("1".equals(str)){
				cnt++;			
			}
		}
		System.out.println("���e�̌�: " + cnt);
		
		}
		
		
		//�p�X�{�^���̍쐬
		passButton = new JButton("�p�X");
		passButton.setBounds(525,225,150,80);
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
				if(myNumberInt % 2 == 0){//�^�[���Ƃ����߂�
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
						if (cmd.equals("watch")) {
							if (watchCount > 0) {
								int nextGun = chamber.get(0); // ���̒e������
								if (nextGun == 1) {
									System.out.println("���̒e�͎��e�ł��I");
								} else {
									System.out.println("���̒e�͋�e�ł��I");
								}
								watchCount--; // Watch�̎g�p�񐔂����炷
								System.out.println("�c���Watch��: " + watchCount);
								if(watchCount == 0){
								watchButton.setEnabled(false);
								}
							}
						}					
						if (cmd.equals("PASS")) { // cmd��"PASS"�̏ꍇ�̏���
							// �e�ۂ�1�����o��
							int bullet = chamber.remove(0); // ���X�g����ŏ��̒e�����o��
							if (bullet == 1) {
								// ���e�������ꍇ
								playerHealth -= 1; // �v���C���[�̗̑͂�1���炷
								System.out.println("���e�I�̗͂��������܂����B���݂̗̑�: " + playerHealth);
								myTurn = 1 - myTurn;; // �����̃^�[�����I��
							} else {
								// ��e�������ꍇ
								System.out.println("�Z�[�t�I������x�����Ă��������B");
								// myTurn���ێ����čēx���s
								myTurn = 0 - myTurn;
							}
						}
						if (cmd.equals("Shot")) { // cmd��"shot"�̏ꍇ�̏���
								// �e�ۂ�1�����o��
								int bullet = chamber.remove(0); // ���X�g����ŏ��̒e�����o��
								if (bullet == 1) {
									// ���e�������ꍇ
									opponentHealth -= 1; // �v���C���[�̗̑͂�1���炷
									System.out.println("���e�I����̗̑͂��������܂����B����̗̑�: " + opponentHealth);
									myTurn = 1 - myTurn; // �����̃^�[�����I��
								} else {
									// ��e�������ꍇ
									opponentHealth -= 0;
									System.out.println("�c�O�I ����̗̑�:"�@+ opponentHealth);
									// �����̃^�[�����I��
									myTurn = 1 - myTurn;
								}
						}
						if(cmd.equals("PASS")){//�p�X�{�^���������ꂽ�ꍇ�̏���
							pass_count += 1;//�p�X�J�E���g��1���₷
							System.out.println(pass_count);
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
	
		//���s�̏������R���s��
		if ( playerHealth < 0){
			System.out.println("Player�̕����ł�");
		}
		else{
			System.out.println("Player�̏����ł�");
		}
	}
}
