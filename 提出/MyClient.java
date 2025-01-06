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
		String IPname = JOptionPane.showInputDialog(null,"サーバーの名前を入力してください","サーバーの入力",JOptionPane.QUESTION_MESSAGE);
		if(IPname.equals("") || IPname == null){
			IPname = "localhost";
		}
		//ウィンドウを作成する
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じるときに，正しく閉じるように設定する
		setTitle("MyClient");//ウィンドウのタイトルを設定する
		setSize(800,600);//ウィンドウのサイズを設定する
		c = getContentPane();//フレームのペインを取得する

		//アイコンの設定
		whiteIcon = new ImageIcon("White.jpg");
		blackIcon = new ImageIcon("Black.jpg");
		boardIcon = new ImageIcon("GreenFrame.jpg");

		c.setLayout(null);//自動レイアウトの設定を行わない
		//ボタンの生成
		buttonArray = new JButton[8][8];//ボタンの配列を５個作成する[0]から[8]まで使える
		for(int j=0;j<8;j++){
			for(int i=0; i<8; i++){
				buttonArray[j][i] = new JButton(boardIcon);//ボタンにアイコンを設定する
				c.add(buttonArray[j][i]);//ペインに貼り付ける
				buttonArray[j][i].setBounds(i*50+10,j*50+10,50,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
				buttonArray[j][i].addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
				buttonArray[j][i].addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
				buttonArray[j][i].setActionCommand(Integer.toString((8*j+i)));//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
			}
		}
		//初期のボタン配置//
		buttonArray[3][3].setIcon(blackIcon);
		buttonArray[4][4].setIcon(blackIcon);
		buttonArray[3][4].setIcon(whiteIcon);
		buttonArray[4][3].setIcon(whiteIcon);
		//パスボタンの作成
		passButton = new JButton("パス");
		passButton.setBounds(500,280,180,100);
		passButton.addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
		passButton.setActionCommand("PASS");
		c.add(passButton);
		//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
			
		//サーバに接続する
		Socket socket = null;
		try {
			//"localhost"は，自分内部への接続．localhostを接続先のIP Address（"133.42.155.201"形式）に設定すると他のPCのサーバと通信できる
			//10000はポート番号．IP Addressで接続するPCを決めて，ポート番号でそのPC上動作するプログラムを特定する
			socket = new Socket(IPname, 10000);
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
				if(myNumberInt % 2 == 0){//ターンとコマの色を決める
					myTurn = 0;
					myIcon = blackIcon;
			        yourIcon = whiteIcon;
					myColor = 0;
				}
				else{
					myIcon=whiteIcon;
					yourIcon=blackIcon;
					myColor = 1;
					myTurn = 1;
				}
				while(true) {
					String inputLine = br.readLine();//データを一行分だけ読み込んでみる
					if (inputLine != null) {//読み込んだときにデータが読み込まれたかどうかをチェックする
						System.out.println(inputLine);//デバッグ（動作確認用）にコンソールに出力する
						String[] inputTokens = inputLine.split(" ");	//入力データを解析するために、スペースで切り分ける
						String cmd = inputTokens[0];//コマンドの取り出し．１つ目の要素を取り出す
						if(cmd.equals("MOVE")){//cmdの文字と"MOVE"が同じか調べる．同じ時にtrueとなる
							//MOVEの時の処理(コマの移動の処理)
							String theBName = inputTokens[1];//ボタンの名前（番号）の取得
							int theBnum = Integer.parseInt(theBName);//ボタンの名前を数値に変換する
							int x = Integer.parseInt(inputTokens[2]);//数値に変換する
							int y = Integer.parseInt(inputTokens[3]);//数値に変換する
							int j = theBnum / 8;
							int i = theBnum % 8;
							buttonArray[j][i].setLocation(x,y);//指定のボタンを位置をx,yに設定する
						}
						if(cmd.equals("PLACE")){//cmdの文字と"PLACE"が同じか調べる．同じ時にtrueとなる
							//PLACEの時の処理(コマの移動の処理)
							String theBName = inputTokens[1];//ボタンの名前（番号）の取得
							int theBnum = Integer.parseInt(theBName);//ボタンの名前を数値に変換する
							myTurn = 1 - myTurn;
							int theColor = Integer.parseInt(inputTokens[2]);//数値に変換する
							int j = theBnum / 8;
							int i = theBnum % 8;
							if (theColor == myColor){
								buttonArray[j][i].setIcon(myIcon);
							}
							else{
								buttonArray[j][i].setIcon(yourIcon);
							}
							pass_count = 0;
						}
						if(cmd.equals("FLIP")){//cmdの文字と"FLIP"が同じか調べる．同じ時にtrueとなる
							//FLIPの時の処理(コマの移動の処理)
							String theBName = inputTokens[1];//ボタンの名前（番号）の取得
							int theBnum = Integer.parseInt(theBName);//ボタンの名前を数値に変換する
							int theColor = Integer.parseInt(inputTokens[2]);//数値に変換する
							int j = theBnum / 8;
							int i = theBnum % 8;
							if (theColor == myColor){//置いたボタンの処理
								buttonArray[j][i].setIcon(myIcon);
							}
							else{
								buttonArray[j][i].setIcon(yourIcon);
							}
						}
						if(cmd.equals("PASS")){//パスボタンが押された場合の処理
							pass_count += 1;//パスカウントを1増やす
							if (pass_count >1){
								check_count();//パスカウントが2以上の場合コマを数える。
								break;
							}
							myTurn = 1 - myTurn;
						}
						if(check()){
							check_count();
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
			if (theIcon == boardIcon){
				int temp = Integer.parseInt(theArrayIndex); //int型に変換
				int y = temp / 8;
				int x = temp % 8;
				if(judgeButton(x, y)){//裏返せるものがあるか確認
					System.out.println(theIcon);//デバッグ（確認用）に，クリックしたアイコンの名前を出力する

					repaint();//画面のオブジェクトを描画し直す
					//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
					String msg = "PLACE"+" "+theArrayIndex+" "+myColor;

					//サーバに情報を送る
					out.println(msg);//送信データをバッファに書き出す
					out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
				}
				else{
					System.out.println("そこには配置できません");
				}
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
	//返せるコマがあるかを確認する関数
	public boolean judgeButton(int x, int y){
		boolean flag = false;
		//すべての方向におけるコマがある確認
		for(int j=-1;j<1;j++){
			for(int i=-1; i<1; i++){
				if(y+j<0 || 8<=y+j || x+i<0 || 8<=x+i){//２次元配列内を参照している場合は続ける
					continue;
				}
				if(1<=flipButtons(x,y,i,j)){//ある場合はflag=trueにする
					flag = true;
				}
			}
		}
		return flag;
	}
	//何枚返せる枚数があるかを見る関数
	public int flipButtons(int x, int y, int i, int j){
		int flipNum = 0;//何枚返せるかを保存
		if (i==0 && j==0){//自分を参照している場合は中断
			return 0;
		}
		for(int dy=j, dx=i; ; dy+=j, dx+=i){//iとjの方向への探索を進める。
			int ydy=dy+y;
			int xdx=dx+x;
			if(ydy<0 || 8<=ydy || xdx<0 || 8<=xdx){//２次元配列内を参照しない場合は処理を中断
				return 0;
			}
			Icon theIcon = buttonArray[ydy][xdx].getIcon();//iとj方向のIconを確認
			if(theIcon == boardIcon){//ボードアイコンの場合は処理を中断
				return 0;
			}
			if(theIcon == myIcon){
				if(flipNum >= 1){//1枚でも返せる枚数があれば情報を送る
					for(int msm=j, sms=i, k=0; k<flipNum; k++, msm+=j, sms+=i){
						//ボタンの位置情報を作る
						int msgy = y + msm;
						int msgx = x + sms;
						int theArrayIndex = msgy*8 + msgx;
						//サーバに情報を送る
						String msg = "FLIP"+" "+theArrayIndex+" "+myColor;
						out.println(msg);
						out.flush();
					}
				}
				return flipNum;
			}
			if(theIcon == yourIcon){//youIconならflipnumを増加
				flipNum++;
			}
		}
	}
	//ボードアイコンがあるかを確認する関数
	public boolean check(){//ボードアイコンがあるかを確認する
		boolean flag = true;
		Icon theIcon = null;
		//すべてのマス目を確認していく
		for (int i=0; i<8; i++){
			for (int j=0; j<8; j++){
				theIcon = buttonArray[i][j].getIcon();//マス目を確認
				if (theIcon == boardIcon){//ボードアイコンがある場合flagをfalseに設定
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
	//最後にコマを数える関数
	public void check_count(){
		//それぞれを初期化
		int white = 0;
		int black = 0;
		Icon theIcon = null;
		//すべてのマス目を探索
		for (int i=0; i<8; i++){
			for (int j=0; j<8; j++){
				theIcon = buttonArray[i][j].getIcon();
				//それぞれの場合でカウントを増やす
				if (theIcon == whiteIcon){
					white++;
				}
				else if (theIcon == blackIcon){
					black++;
				}
			}
		}
		//勝敗の条件理由を行う
		if (white < black){
			System.out.println("黒のコマの勝ちです");
		}
		else if (white > black){
			System.out.println("白のコマの勝ちです");
		}
		else{
			System.out.println("引き分けです");
		}
	}
}
