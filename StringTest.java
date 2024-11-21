public class StringTest {
    public static void main(String[] args) {

		// 文字列の宣言，宣言とともに値を設定
		String a = "Good ";
		String b = "Morning!";


		// System.out.printlnはコマンドプロンプト（コンソール）への出力
		System.out.println("a=" + a);
		System.out.println("b=" + b);


		// 文字列は+で文字列同士を連結する
		String c = a+b;
		System.out.println("c=" + c);

		
		// equals()で文字列の比較を行う
		if (a.equals("good ")) {
			System.out.println("変数a の値は good です．");
		} else {
			System.out.println("変数a の値は good ではありません．");
		}
		

		// equalsIgnoreCase()で大文字，小文字を無視した文字列の比較を行う
		if (a.equalsIgnoreCase("good ")) {
			System.out.println("変数a の値は good です．");
		} else {
			System.out.println("変数a の値は good ではありません．");
		}


		// toLowerCase()で全て小文字に
		System.out.println(c.toLowerCase());
		
		// toUpperCase()で全て大文字に
		System.out.println(c.toUpperCase());


		// length()で，文字列の長さを得る
		int alen = a.length();
		int blen = b.length();
		System.out.println("文字列 a の長さは " + alen );
		System.out.println("文字列 b の長さは " + blen );
		System.out.println("文字列 c の長さは " + c.length());

		
		// substring(開始位置，終了位置)で，部分文字列を取り出します．
		// 終了位置を指定しないと，開始位置から末尾まで取り出します．
		String e = c.substring(2,4);
		System.out.println(e);
		String f = c.substring(5);
 		System.out.println(f);


		// 検討課題
		System.out.println ("確認１："+ (a == "Good ") );

		String foo= "the Good Event";
		String subfoo = foo.substring(4,9);
		System.out.println ("|"+ subfoo +"|");
		System.out.println ("確認２(==の場合)："+ (a == subfoo) );
		System.out.println ("確認２(equalsの場合)："+ (a.equals(subfoo)) );

   }
}