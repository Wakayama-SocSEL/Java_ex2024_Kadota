import java.net.*;
import java.io.*;
import javax.swing.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class MyClient extends JFrame implements MouseListener,MouseMotionListener {
	private JButton buttonArray[][]; //ボタン用の配列
	private JButton passButton;
	private int myColor, x, y;
	private Container c;
	private ImageIcon myIcon, yourIcon;
	private int myTurn;
	private ImageIcon blackIcon, whiteIcon, boardIcon;
	PrintWriter out;//出力用のライター
	private int pass_count;

	public MyClient() {
		//名前の入力ダイアログを開く
		String myName = JOptionPane.showInputDialog(null,"名前を入力してください","名前の入力",JOptionPane.QUESTION_MESSAGE);
		if(myName.equals("")){
			myName = "No name";//名前がないときは，"No name"とする
		}
		String serverIP = JOptionPane.showInputDialog(null,"サーバーの名前を入力してください","サーバーの入力",JOptionPane.QUESTION_MESSAGE);
		if(serverIP.equals("") || serverIP == null){
			serverIP = "localhost";
		}
		//ウィンドウを作成する
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じるときに，正しく閉じるように設定する
		setTitle("MyClient");//ウィンドウのタイトルを設定する
		setSize(800,600);//ウィンドウのサイズを設定する
		c = getContentPane();//フレームのペインを取得する

		//アイコンの設定
		twice = new ImageIcon("twice.jpg");
		watch = new ImageIcon("Black.jpg");
		joker = new ImageIcon("GreenFrame.jpg");
		heal = new ImageIcon("heal.jpg");
		watch = new ImageIcon("Black.jpg");

		c.setLayout(null);//自動レイアウトの設定を行わない
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
		System.out.println("実弾の個数: " + cnt);
		
		}
		
		
		//パスボタンの作成
		passButton = new JButton("パス");
		passButton.setBounds(525,225,150,80);
		passButton.addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
		passButton.setActionCommand("PASS");
		c.add(passButton);
		//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
			
		//サーバに接続する
		Socket socket = null;
		try {
			//"localhost"は，自分内部への接続．localhostを接続先のIP Address（"133.42.155.201"形式）に設定すると他のPCのサーバと通信できる
			//10000はポート番号．IP Addressで接続するPCを決めて，ポート番号でそのPC上動作するプログラムを特定する
			socket = new Socket(serverIP, 10000);
		} catch (UnknownHostException e) {
			System.err.println("ホストの IP アドレスが判定できません: " + e);
		} catch (IOException e) {
			 System.err.println("エラーが発生しました: " + e);
		}
		
		MesgRecvThread mrt = new MesgRecvThread(socket, myName);//受信用のスレッドを作成する
		mrt.start();//スレッドを動かす（Runが動く）
	}
		
	//メッセージ受信のためのスレッド
	public class MesgRecvThread extends Thread {
		
		Socket socket;
		String myName;
		
		public MesgRecvThread(Socket s, String n){
			socket = s;
			myName = n;
		}
		
		//通信状況を監視し，受信データによって動作する
		public void run() {
			try{
				InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(sisr);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(myName);//接続の最初に名前を送る
				String myNumberStr = br.readLine();
				int myNumberInt = Integer.parseInt(myNumberStr);
				if(myNumberInt % 2 == 0){//ターンとを決める
					myTurn = 0;
				}
				else{
					myTurn = 1;
				}
				while(true) {
					String inputLine = br.readLine();//データを一行分だけ読み込んでみる
					if (inputLine != null) {//読み込んだときにデータが読み込まれたかどうかをチェックする
						System.out.println(inputLine);//デバッグ（動作確認用）にコンソールに出力する
						String[] inputTokens = inputLine.split(" ");	//入力データを解析するために、スペースで切り分ける
						String cmd = inputTokens[0];//コマンドの取り出し．１つ目の要素を取り出す
						if (cmd.equals("watch")) {
							if (watchCount > 0) {
								int nextGun = chamber.get(0); // 次の弾を見る
								if (nextGun == 1) {
									System.out.println("次の弾は実弾です！");
								} else {
									System.out.println("次の弾は空弾です！");
								}
								watchCount--; // Watchの使用回数を減らす
								System.out.println("残りのWatch回数: " + watchCount);
								if(watchCount == 0){
								watchButton.setEnabled(false);
								}
							}
						}					
						if (cmd.equals("PASS")) { // cmdが"PASS"の場合の処理
							// 弾丸を1発取り出す
							int bullet = chamber.remove(0); // リストから最初の弾を取り出す
							if (bullet == 1) {
								// 実弾だった場合
								playerHealth -= 1; // プレイヤーの体力を1減らす
								System.out.println("実弾！体力が減少しました。現在の体力: " + playerHealth);
								myTurn = 1 - myTurn;; // 自分のターンを終了
							} else {
								// 空弾だった場合
								System.out.println("セーフ！もう一度引いてください。");
								// myTurnを維持して再度試行
								myTurn = 0 - myTurn;
							}
						}
						if (cmd.equals("Shot")) { // cmdが"shot"の場合の処理
								// 弾丸を1発取り出す
								int bullet = chamber.remove(0); // リストから最初の弾を取り出す
								if (bullet == 1) {
									// 実弾だった場合
									opponentHealth -= 1; // プレイヤーの体力を1減らす
									System.out.println("実弾！相手の体力が減少しました。相手の体力: " + opponentHealth);
									myTurn = 1 - myTurn; // 自分のターンを終了
								} else {
									// 空弾だった場合
									opponentHealth -= 0;
									System.out.println("残念！ 相手の体力:"　+ opponentHealth);
									// 自分のターンを終了
									myTurn = 1 - myTurn;
								}
						}
						if(cmd.equals("PASS")){//パスボタンが押された場合の処理
							pass_count += 1;//パスカウントを1増やす
							System.out.println(pass_count);
							if (pass_count >1){
								Count_board();//パスカウントが2以上の場合コマを数える。
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
				System.err.println("エラーが発生しました: " + e);
			}
		}
	}

	public static void main(String[] args) {
		MyClient net = new MyClient();
		net.setVisible(true);
	}
  	
	public void mouseClicked(MouseEvent e) {//ボタンをクリックしたときの処理
		if (myTurn == 0){
			System.out.println("クリック");
			JButton theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．型が違うのでキャストする
			String theArrayIndex = theButton.getActionCommand();//ボタンの配列の番号を取り出す
			Icon theIcon = theButton.getIcon();//theIconには，現在のボタンに設定されたアイコンが入る
			if (theArrayIndex.equals("PASS")){
				String msg = "PASS";

				//サーバに情報を送る
				out.println(msg);//送信データをバッファに書き出す
				out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
			}
			
		}

	}
	
	public void mouseEntered(MouseEvent e) {//マウスがオブジェクトに入ったときの処理
		// System.out.println("マウスが入った");
	}
	
	public void mouseExited(MouseEvent e) {//マウスがオブジェクトから出たときの処理
		// System.out.println("マウス脱出");
	}
	
	public void mousePressed(MouseEvent e) {//マウスでオブジェクトを押したときの処理（クリックとの違いに注意）
		// System.out.println("マウスを押した");
	}
	
	public void mouseReleased(MouseEvent e) {//マウスで押していたオブジェクトを離したときの処理
		// System.out.println("マウスを放した");
	}
	
	public void mouseDragged(MouseEvent e) {//マウスでオブジェクトとをドラッグしているときの処理
		/*
		System.out.println("マウスをドラッグ");
		JButton theButton = (JButton)e.getComponent();//型が違うのでキャストする
		String theArrayIndex = theButton.getActionCommand();//ボタンの配列の番号を取り出す
		if (!theArrayIndex.equalsIgnoreCase("0")){
			Point theMLoc = e.getPoint();//発生元コンポーネントを基準とする相対座標
			System.out.println(theMLoc);//デバッグ（確認用）に，取得したマウスの位置をコンソールに出力する
			Point theBtnLocation = theButton.getLocation();//クリックしたボタンを座標を取得する
			theBtnLocation.x += theMLoc.x-15;//ボタンの真ん中当たりにマウスカーソルがくるように補正する
			theBtnLocation.y += theMLoc.y-15;//ボタンの真ん中当たりにマウスカーソルがくるように補正する
			theButton.setLocation(theBtnLocation);//マウスの位置にあわせてオブジェクトを移動する
	 
			//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
			String msg = "MOVE"+" "+theArrayIndex+" "+theBtnLocation.x+" "+theBtnLocation.y;

			//サーバに情報を送る
			out.println(msg);//送信データをバッファに書き出す
			out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
		}
		repaint();//オブジェクトの再描画を行う
		*/
	}

	public void mouseMoved(MouseEvent e) {//マウスがオブジェクト上で移動したときの処理
		/*
		System.out.println("マウス移動");
		int theMLocX = e.getX();//マウスのx座標を得る
		int theMLocY = e.getY();//マウスのy座標を得る
		System.out.println(theMLocX+","+theMLocY);//コンソールに出力する
		*/
	}
	
		//勝敗の条件理由を行う
		if ( playerHealth < 0){
			System.out.println("Playerの負けです");
		}
		else{
			System.out.println("Playerの勝ちです");
		}
	}
}
