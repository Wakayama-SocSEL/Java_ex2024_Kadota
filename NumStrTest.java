public class NumStrTest {

	public static void main(String[] argv){
	
		// 数値型から文字列型への変換
		System.out.println("暗黙的に変換される場合");
		int numberten = 10;
		System.out.println("Number "+ numberten);

		System.out.println("明示的に変換コードを書く場合");
		String str = Integer.toString(numberten);
		System.out.println(str);


		// 文字列型から数値型への変換
		System.out.println("int型の変換");
		String strint = "1234abc";
		
		try {
		  int inum = Integer.parseInt(strint);
		  System.out.println(inum);
		//エラーが発生した時の処理
		} catch (NumberFormatException ex) {
		  System.out.println(strint + "は数値に変換できません！！");
		}

		System.out.println("float型の変換");
		String strfloat = "1234.56";
		double fnum = Double.parseDouble(strfloat);
		System.out.println(fnum);

	}
}