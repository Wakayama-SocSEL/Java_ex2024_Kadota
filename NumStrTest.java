public class NumStrTest {

	public static void main(String[] argv){
	
		// ���l�^���當����^�ւ̕ϊ�
		System.out.println("�ÖٓI�ɕϊ������ꍇ");
		int numberten = 10;
		System.out.println("Number "+ numberten);

		System.out.println("�����I�ɕϊ��R�[�h�������ꍇ");
		String str = Integer.toString(numberten);
		System.out.println(str);


		// ������^���琔�l�^�ւ̕ϊ�
		System.out.println("int�^�̕ϊ�");
		String strint = "1234abc";
		
		try {
		  int inum = Integer.parseInt(strint);
		  System.out.println(inum);
		//�G���[�������������̏���
		} catch (NumberFormatException ex) {
		  System.out.println(strint + "�͐��l�ɕϊ��ł��܂���I�I");
		}

		System.out.println("float�^�̕ϊ�");
		String strfloat = "1234.56";
		double fnum = Double.parseDouble(strfloat);
		System.out.println(fnum);

	}
}