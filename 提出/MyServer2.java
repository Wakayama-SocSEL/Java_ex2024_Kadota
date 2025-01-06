import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.*;
import java.util.zip.*;



// �t�@�C����zip���k����
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream; 
import java.io.FileOutputStream;
import java.util.zip.ZipEntry; 
import java.util.zip.ZipOutputStream;
 
 
public class Main {
    public static void main(String[] args) throws Exception {
        
        // �쐬����zip�t�@�C�� 
        File zipFile = new File("CompressedFile.zip");
        
        // �쐬�O��zip�t�@�C���̑��݊m�F���ʂ�\������ 
        System.out.println(
            "zip���k�O�̑��݊m�F:" + zipFile.exists());
        
        // �쐬����zip�t�@�C���p�̏o�̓X�g���[�� 
        ZipOutputStream zostr = new ZipOutputStream(
            new FileOutputStream(zipFile));
            
        // ���k�Ώ� 
        File targetFile = new File("target");
        
        // �t�@�C���̈��k���s�� 
        Main.compressFile(zostr, targetFile);
        
        // �쐬����zip�t�@�C���p�̏o�̓X�g���[�������
        zostr.close();
        
        // �쐬���zip�t�@�C���̑��݊m�F���ʂ�\���B 
        System.out.println(
            "zip���k��̑��݊m�F: " + zipFile.exists());
    }
        
    // ���ۂɃt�@�C�����Ƀt�@�C����zip���k���郁�\�b�h 
    private static void compressFile(
        ZipOutputStream zos, File file) throws Exception {
        
        byte[] buf = new byte[1024]; 
        int len;
        
        if (file.isDirectory()) {
            // �Ώۂ��f�B���N�g���̏ꍇ 
            // �쐬����zip�t�@�C���Ƀf�B���N�g���̃G���g���̐ݒ���s�� 
            zostr.putNextEntry(new ZipEntry(file.getPath() + "/"));
            
            // �f�B���N�g�����̃t�@�C���ꗗ���擾���� 
            File[] childFileList = file.listFiles();
            
            for (File childFile: childFileList) {
                
                //�f�B���N�g�����̃t�@�C���ɂčċA�Ăяo������
                compressFile(zostr, childFile);
            }
            
        }else {
            // �Ώۂ��t�@�C���̏ꍇ
            // �쐬����zip�t�@�C���ɃG���g���̐ݒ���s��
            zostr.putNextEntry(new ZipEntry(file.getPath()));
            
            // ���k����t�@�C���p�̓��̓X�g���[�� 
            BufferedInputStream bistr = new BufferedInputStream(
                new FileInputStream(file));
                
            // ���k����t�@�C����ǂݍ��݂Ȃ���A
            //zip�t�@�C���p�̏o�̓X�g���[���֏������݂�����
            while ((len = bistr.read(buf, 0, buf.length)) != -1) {
                zostr.write(buf, 0, len);
            }
                
            // ���k����t�@�C���p�̓��̓X�g���[������� 
            bistr.close();
            
            // �G���g������� 
            zostr.closeEntry();
        }
    }
}
//�X���b�h���i�e�N���C�A���g�ɉ����āj
class ClientProcThread extends Thread {
	private int number;//�����̔ԍ�
	private Socket incoming;
	private InputStreamReader myIsr;
	private BufferedReader myIn;
	private PrintWriter myOut;
	private String myName;//�ڑ��҂̖��O

	public ClientProcThread(int n, Socket i, InputStreamReader isr, BufferedReader in, PrintWriter out) {
		number = n;
		incoming = i;
		myIsr = isr;
		myIn = in;
		myOut = out;
	}

	public void run() {
		try {
			myOut.println(number);//���񂾂��Ă΂��
			
			myName = myIn.readLine();//���߂Đڑ������Ƃ��̈�s�ڂ͖��O

			while (true) {//�������[�v�ŁC�\�P�b�g�ւ̓��͂��Ď�����
				String str = myIn.readLine();
				System.out.println("Received from client No."+number+"("+myName+"), Messages: "+str);
				if (str != null) {//���̃\�P�b�g�i�o�b�t�@�j�ɓ��͂����邩���`�F�b�N
					if (str.toUpperCase().equals("BYE")) {
						myOut.println("Good bye!");
						break;
					}
					MyServer2.SendAll(str, myName);//�T�[�o�ɗ������b�Z�[�W�͐ڑ����Ă���N���C�A���g�S���ɔz��
				}
			}
		} catch (Exception e) {
			//�����Ƀv���O���������B����Ƃ��́C�ڑ����؂ꂽ�Ƃ�
			System.out.println("Disconnect from client No."+number+"("+myName+")");
			MyServer2.SetFlag(number, false);//�ڑ����؂ꂽ�̂Ńt���O��������
		}
	}
}

class MyServer2{
	
	private static int maxConnection=100;//�ő�ڑ���
	private static Socket[] incoming;//��t�p�̃\�P�b�g
	private static boolean[] flag;//�ڑ������ǂ����̃t���O
	private static InputStreamReader[] isr;//���̓X�g���[���p�̔z��
	private static BufferedReader[] in;//�o�b�t�@�����O���ɂ��e�L�X�g�ǂݍ��ݗp�̔z��
	private static PrintWriter[] out;//�o�̓X�g���[���p�̔z��
	private static ClientProcThread[] myClientProcThread;//�X���b�h�p�̔z��
	private static int member;//�ڑ����Ă��郁���o�[�̐�

	//�S���Ƀ��b�Z�[�W�𑗂�
	public static void SendAll(String str, String myName){
		//����ꂽ�������b�Z�[�W��ڑ����Ă���S���ɔz��
		for(int i=1;i<=member;i++){
			if(flag[i] == true){
				out[i].println(str);
				out[i].flush();//�o�b�t�@���͂��o�������o�b�t�@�ɂ���S�Ẵf�[�^�������ɑ��M����
				System.out.println("Send messages to client No."+i);
			}
		}	
	}
	
	//�t���O�̐ݒ���s��
	public static void SetFlag(int n, boolean value){
		flag[n] = value;
	}
	
	//main�v���O����
	public static void main(String[] args) {
		//�K�v�Ȕz����m�ۂ���
		incoming = new Socket[maxConnection];
		flag = new boolean[maxConnection];
		isr = new InputStreamReader[maxConnection];
		in = new BufferedReader[maxConnection];
		out = new PrintWriter[maxConnection];
		myClientProcThread = new ClientProcThread[maxConnection];
		
		int n = 1;
		member = 0;//�N���ڑ����Ă��Ȃ��̂Ń����o�[���͂O

		try {
			System.out.println("The server has launched!");
			ServerSocket server = new ServerSocket(10000);//10000�ԃ|�[�g�𗘗p����
			while (true) {
				incoming[n] = server.accept();
				flag[n] = true;
				System.out.println("Accept client No." + n);
				//�K�v�ȓ��o�̓X�g���[�����쐬����
				isr[n] = new InputStreamReader(incoming[n].getInputStream());
				in[n] = new BufferedReader(isr[n]);
				out[n] = new PrintWriter(incoming[n].getOutputStream(), true);
				
				myClientProcThread[n] = new ClientProcThread(n, incoming[n], isr[n], in[n], out[n]);//�K�v�ȃp�����[�^��n���X���b�h���쐬
				myClientProcThread[n] .start();//�X���b�h���J�n����
				member = n;//�����o�[�̐����X�V����
				n++;
			}
		} catch (Exception e) {
			System.err.println("�\�P�b�g�쐬���ɃG���[���������܂���: " + e);
		}
	}
}
