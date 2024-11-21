import java.io.*;	//importは用意されているクラスライブラリを読み込みます．
					//java.ioは，データやファイルの読み書きなどシステムの入出力用の
					//ライブラリが用意されています．
					//*は，java.ioという名前のパッケージに含まれている
					//全てのクラスとインターフェイスを一括してimportします．
					//下のプログラムでは，InputStreamReaderやBufferedReaderが該当します．

public class Ensyu {
    public static void main(String[] args) {//コマンドラインから渡す文字列をargsで読み取れます．
	int value = 0;

	// EXITが入力されると終了
	while (true) {
		// コンソールから文字列を取得する
		System.out.println("現在の値は："+ value);
		System.out.print("命令を入力してください：　");
		InputStreamReader isr = new InputStreamReader(System.in);//キーボードからの入力（System.in）をテキストとして読み込むための橋渡し
		BufferedReader br = new BufferedReader(isr);//効率よく読み込みするために，バッファリングします．バッファリングはたまった文字列から読み出すので
													//効率よく読み出しができます
		String readdata;
		try {
			readdata = br.readLine();//バッファから一行読み込みます
		} catch (Exception ex) {
			// 読み込み失敗時は，EXITする
			readdata = "EXIT";
		}

		// Splitを使って命令を分割する
		String[] item = readdata.split(" ");//文字列を" "を区切りにして，分割し，配列に入れます．

		// (1) 2つ目の要素があれば，文字列から数値に変換する．
		int abc=0;
		
		if  (item.length>1){
			abc=Integer.parseInt(item[1]);
		}
		
		

		// 分割した要素のうち，命令部分（operator）を取得する
			if (item.length >= 1) {
				// 命令数を確認して，item[0]が存在する場合に実行
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
			
		

				// (2) さまざまな命令文（オペレータ）に対応する．
				else if (operator.equalsIgnoreCase("EXIT")) {//大文字・小文字を区別して比較します．
						// whileを終了する
					break;
		}		else {
						System.out.println("命令"+ operator +"は解釈できません．");
				}
			}
		
		
	 // 入力を続けるためのwhile文
	
}
	}
}
