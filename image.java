public class shot{		
		
	if(cmd.equals("Shot")){//cmd�̕�����"Shot"�����������ׂ�D��������true�ƂȂ�
							//Shot�̎��̏���
		String theBName = inputTokens[1];//�{�^���̖��O�i�ԍ��j�̎擾
		int theBnum = Integer.parseInt(theBName);//�{�^���̖��O�𐔒l�ɕϊ�����
		int consume = all_bullet.remove(0); // ���X�g����ŏ��̒e�����o��
		if (consume == 1){
			health[player] -= 1; // �v���C���[�̗̑͂�1���炷
			System.out.println("���e�I�̗͂��������܂����B���݂̗̑�: " + health[player]);
			myTurn--; // �����̃^�[�����I��
			if(cmd.equals("third")){
				health[player] -= 3; // �v���C���[�̗̑͂�1���炷
				System.out.println("���e�I�̗͂��������܂����B���݂̗̑�: " + health[player]);
				myTurn--; // �����̃^�[�����I��
			}
		}
		else{
			health[player] -= 0; // �v���C���[�̗̑͂�1���炷
			myTurn--; // �����̃^�[�����I��
		}
	}
}