import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.*;
import java.util.zip.*;



// ファイルをzip圧縮する
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream; 
import java.io.FileOutputStream;
import java.util.zip.ZipEntry; 
import java.util.zip.ZipOutputStream;
 
 
public class Main {
    public static void main(String[] args) throws Exception {
        
        // 作成するzipファイル 
        File zipFile = new File("CompressedFile.zip");
        
        // 作成前のzipファイルの存在確認結果を表示する 
        System.out.println(
            "zip圧縮前の存在確認:" + zipFile.exists());
        
        // 作成するzipファイル用の出力ストリーム 
        ZipOutputStream zostr = new ZipOutputStream(
            new FileOutputStream(zipFile));
            
        // 圧縮対象 
        File targetFile = new File("target");
        
        // ファイルの圧縮を行う 
        Main.compressFile(zostr, targetFile);
        
        // 作成するzipファイル用の出力ストリームを閉じる
        zostr.close();
        
        // 作成後のzipファイルの存在確認結果を表す。 
        System.out.println(
            "zip圧縮後の存在確認: " + zipFile.exists());
    }
        
    // 実際にファイルをにファイルをzip圧縮するメソッド 
    private static void compressFile(
        ZipOutputStream zos, File file) throws Exception {
        
        byte[] buf = new byte[1024]; 
        int len;
        
        if (file.isDirectory()) {
            // 対象がディレクトリの場合 
            // 作成するzipファイルにディレクトリのエントリの設定を行う 
            zostr.putNextEntry(new ZipEntry(file.getPath() + "/"));
            
            // ディレクトリ内のファイル一覧を取得する 
            File[] childFileList = file.listFiles();
            
            for (File childFile: childFileList) {
                
                //ディレクトリ内のファイルにて再帰呼び出しする
                compressFile(zostr, childFile);
            }
            
        }else {
            // 対象がファイルの場合
            // 作成するzipファイルにエントリの設定を行う
            zostr.putNextEntry(new ZipEntry(file.getPath()));
            
            // 圧縮するファイル用の入力ストリーム 
            BufferedInputStream bistr = new BufferedInputStream(
                new FileInputStream(file));
                
            // 圧縮するファイルを読み込みながら、
            //zipファイル用の出力ストリームへ書き込みをする
            while ((len = bistr.read(buf, 0, buf.length)) != -1) {
                zostr.write(buf, 0, len);
            }
                
            // 圧縮するファイル用の入力ストリームを閉じる 
            bistr.close();
            
            // エントリを閉じる 
            zostr.closeEntry();
        }
    }
}
//スレッド部（各クライアントに応じて）
class ClientProcThread extends Thread {
	private int number;//自分の番号
	private Socket incoming;
	private InputStreamReader myIsr;
	private BufferedReader myIn;
	private PrintWriter myOut;
	private String myName;//接続者の名前

	public ClientProcThread(int n, Socket i, InputStreamReader isr, BufferedReader in, PrintWriter out) {
		number = n;
		incoming = i;
		myIsr = isr;
		myIn = in;
		myOut = out;
	}

	public void run() {
		try {
			myOut.println(number);//初回だけ呼ばれる
			
			myName = myIn.readLine();//初めて接続したときの一行目は名前

			while (true) {//無限ループで，ソケットへの入力を監視する
				String str = myIn.readLine();
				System.out.println("Received from client No."+number+"("+myName+"), Messages: "+str);
				if (str != null) {//このソケット（バッファ）に入力があるかをチェック
					if (str.toUpperCase().equals("BYE")) {
						myOut.println("Good bye!");
						break;
					}
					MyServer2.SendAll(str, myName);//サーバに来たメッセージは接続しているクライアント全員に配る
				}
			}
		} catch (Exception e) {
			//ここにプログラムが到達するときは，接続が切れたとき
			System.out.println("Disconnect from client No."+number+"("+myName+")");
			MyServer2.SetFlag(number, false);//接続が切れたのでフラグを下げる
		}
	}
}

class MyServer2{
	
	private static int maxConnection=100;//最大接続数
	private static Socket[] incoming;//受付用のソケット
	private static boolean[] flag;//接続中かどうかのフラグ
	private static InputStreamReader[] isr;//入力ストリーム用の配列
	private static BufferedReader[] in;//バッファリングをによりテキスト読み込み用の配列
	private static PrintWriter[] out;//出力ストリーム用の配列
	private static ClientProcThread[] myClientProcThread;//スレッド用の配列
	private static int member;//接続しているメンバーの数

	//全員にメッセージを送る
	public static void SendAll(String str, String myName){
		//送られた来たメッセージを接続している全員に配る
		for(int i=1;i<=member;i++){
			if(flag[i] == true){
				out[i].println(str);
				out[i].flush();//バッファをはき出す＝＞バッファにある全てのデータをすぐに送信する
				System.out.println("Send messages to client No."+i);
			}
		}	
	}
	
	//フラグの設定を行う
	public static void SetFlag(int n, boolean value){
		flag[n] = value;
	}
	
	//mainプログラム
	public static void main(String[] args) {
		//必要な配列を確保する
		incoming = new Socket[maxConnection];
		flag = new boolean[maxConnection];
		isr = new InputStreamReader[maxConnection];
		in = new BufferedReader[maxConnection];
		out = new PrintWriter[maxConnection];
		myClientProcThread = new ClientProcThread[maxConnection];
		
		int n = 1;
		member = 0;//誰も接続していないのでメンバー数は０

		try {
			System.out.println("The server has launched!");
			ServerSocket server = new ServerSocket(10000);//10000番ポートを利用する
			while (true) {
				incoming[n] = server.accept();
				flag[n] = true;
				System.out.println("Accept client No." + n);
				//必要な入出力ストリームを作成する
				isr[n] = new InputStreamReader(incoming[n].getInputStream());
				in[n] = new BufferedReader(isr[n]);
				out[n] = new PrintWriter(incoming[n].getOutputStream(), true);
				
				myClientProcThread[n] = new ClientProcThread(n, incoming[n], isr[n], in[n], out[n]);//必要なパラメータを渡しスレッドを作成
				myClientProcThread[n] .start();//スレッドを開始する
				member = n;//メンバーの数を更新する
				n++;
			}
		} catch (Exception e) {
			System.err.println("ソケット作成時にエラーが発生しました: " + e);
		}
	}
}
