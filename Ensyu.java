import java.io.*;	//import�͗p�ӂ���Ă���N���X���C�u������ǂݍ��݂܂��D
					//java.io�́C�f�[�^��t�@�C���̓ǂݏ����ȂǃV�X�e���̓��o�͗p��
					//���C�u�������p�ӂ���Ă��܂��D
					//*�́Cjava.io�Ƃ������O�̃p�b�P�[�W�Ɋ܂܂�Ă���
					//�S�ẴN���X�ƃC���^�[�t�F�C�X���ꊇ����import���܂��D
					//���̃v���O�����ł́CInputStreamReader��BufferedReader���Y�����܂��D

public class Ensyu {
    public static void main(String[] args) {//�R�}���h���C������n���������args�œǂݎ��܂��D
	int value = 0;

	// EXIT�����͂����ƏI��
	while (true) {
		// �R���\�[�����當������擾����
		System.out.println("���݂̒l�́F"+ value);
		System.out.print("���߂���͂��Ă��������F�@");
		InputStreamReader isr = new InputStreamReader(System.in);//�L�[�{�[�h����̓��́iSystem.in�j���e�L�X�g�Ƃ��ēǂݍ��ނ��߂̋��n��
		BufferedReader br = new BufferedReader(isr);//�����悭�ǂݍ��݂��邽�߂ɁC�o�b�t�@�����O���܂��D�o�b�t�@�����O�͂��܂��������񂩂�ǂݏo���̂�
													//�����悭�ǂݏo�����ł��܂�
		String readdata;
		try {
			readdata = br.readLine();//�o�b�t�@�����s�ǂݍ��݂܂�
		} catch (Exception ex) {
			// �ǂݍ��ݎ��s���́CEXIT����
			readdata = "EXIT";
		}

		// Split���g���Ė��߂𕪊�����
		String[] item = readdata.split(" ");//�������" "����؂�ɂ��āC�������C�z��ɓ���܂��D

		// (1) 2�ڂ̗v�f������΁C�����񂩂琔�l�ɕϊ�����D
		int abc=0;
		
		if  (item.length>1){
			abc=Integer.parseInt(item[1]);
		}
		
		

		// ���������v�f�̂����C���ߕ����ioperator�j���擾����
			if (item.length >= 1) {
				// ���ߐ����m�F���āCitem[0]�����݂���ꍇ�Ɏ��s
				String operator = item[0];
		
				
				if (item[0].equalsIgnoreCase("ADD")){
					value=value+abc;
				}
			
				else if (item[0].equalsIgnoreCase("SUB")){
					value=value-abc;
				}
			
				else if (item[0].equalsIgnoreCase("MUL")){
					value=value*abc;
				}
			
				else if (item[0].equalsIgnoreCase("DIV")){
					value=value/abc;
				}
			
		

				// (2) ���܂��܂Ȗ��ߕ��i�I�y���[�^�j�ɑΉ�����D
				else if (operator.equalsIgnoreCase("EXIT")) {//�啶���E����������ʂ��Ĕ�r���܂��D
						// while���I������
					break;
		}		else {
						System.out.println("����"+ operator +"�͉��߂ł��܂���D");
				}
			}
		
		
	 // ���͂𑱂��邽�߂�while��
	
}
	}
}
