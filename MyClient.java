import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.File;//音楽再生時に必要
import javax.sound.sampled.AudioFormat;//音楽再生時に必要
import javax.sound.sampled.AudioSystem;//音楽再生時に必要
import javax.sound.sampled.Clip;//音楽再生時に必要
import javax.sound.sampled.DataLine;//音楽再生時に必要

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        } catch (Exception e) {
            System.err.println("背景画像の読み込みに失敗: " + e.getMessage());
        }
    }
<<<<<<< HEAD

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

public class MyClient extends JFrame implements MouseListener, MouseMotionListener {
    private JButton shotButton, myselfPistolButton, watchButton, doublePowerButton, startButton,replayButton,exitButton;
    private JLabel playerHealthLabel, opponentHealthLabel, messageLabel, bulletCountLabel;
    private int playerHealth = 3;
    private int opponentHealth = 3;
    private ArrayList<Integer> chamber;
	private ArrayList<Integer> tmp;
    private int myTurn; // 0: 先攻, 1: 後攻
    private int watchCount = 2;
    private int powerMultiplier = 1; // 火力の倍率
	private int turnCount = 0; // ターン数を初期化
    private PrintWriter out;
    private String serverIP;
    private String myName;
    private Socket socket;	
    private String selectedCharacter; // 選択されたキャラクターを保持する
	private JLabel playerIcon;   // プレイヤーのアイコンを表示する
	private JLabel opponentIcon; // 相手のアイコンを表示する


	 // 弾の状態を保持する変数
    private boolean isLiveRound = true;  // 実弾か空弾か（true: 実弾、false: 空弾）

    public MyClient() {
		// 名前の入力ダイアログを開く
		myName = JOptionPane.showInputDialog(null, "名前を入力してください", "名前の入力", JOptionPane.QUESTION_MESSAGE);
		if (myName == null || myName.equals("")) {
			myName = "No name";
		}

		serverIP = JOptionPane.showInputDialog(null, "サーバーの名前を入力してください", "サーバーの入力", JOptionPane.QUESTION_MESSAGE);
		if (serverIP == null || serverIP.equals("")) {
			serverIP = "localhost";
		}

		// Setup JFrame with custom background
		setContentPane(new BackgroundPanel("/tabletop.png"));
		setLayout(null);

		// Player and opponent icons
        JLabel playerIcon = new JLabel(new ImageIcon(getClass().getResource("images.png")));
        playerIcon.setBounds(50, 50, 100, 100);
        add(playerIcon);

        JLabel opponentIcon = new JLabel(new ImageIcon(getClass().getResource("images.png")));
        opponentIcon.setBounds(650, 50, 100, 100);
        add(opponentIcon);


		

        // Start button
        startButton = new JButton("ゲーム開始");
        startButton.setBounds(300, 250, 200, 50);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        add(startButton);

        // Bullet count label (for displaying bullet counts)
        bulletCountLabel = new JLabel();
        bulletCountLabel.setBounds(250, 200, 300, 30);
        bulletCountLabel.setFont(new Font("MS Gothic", Font.BOLD, 16)); // 日本語対応フォント設定
        bulletCountLabel.setForeground(Color.YELLOW);
        add(bulletCountLabel);

        // Health labels
        playerHealthLabel = new JLabel("Player Health: " + playerHealth);
        playerHealthLabel.setBounds(50, 160, 200, 30);
        playerHealthLabel.setForeground(Color.WHITE);
        add(playerHealthLabel);

        opponentHealthLabel = new JLabel("Opponent Health: " + opponentHealth);
        opponentHealthLabel.setBounds(650, 160, 200, 30);
        opponentHealthLabel.setForeground(Color.WHITE);
        add(opponentHealthLabel);

        // Message label
        messageLabel = new JLabel("ゲーム開始", SwingConstants.CENTER);
        messageLabel.setBounds(200, 500, 400, 30);
        messageLabel.setFont(new Font("MS Gothic", Font.BOLD, 16)); // 日本語対応フォント設定
        messageLabel.setForeground(Color.YELLOW);
        add(messageLabel);

        // Buttons placed below the start button
       //ボタン作成
		watchButton = createButton("eye.jpeg", 50, 350, "次の弾の確認");
		watchButton.setEnabled(false);
		watchButton.addActionListener(e -> {
			watchaction(); // 次の弾の確認処理
			watchButton.setEnabled(false);
			doublePowerButton.setEnabled(false); // ダブルボタンを無効化
		});
		add(watchButton);


        myselfPistolButton = createButton("myself_pistol.png", 250, 350, "自分に打つ");
		myselfPistolButton.setEnabled(false);
        add(myselfPistolButton);

        shotButton = createButton("pistol.jpg", 450, 350, "相手に打つ");
		shotButton.setEnabled(false);
        add(shotButton);

        doublePowerButton = createButton("double_power.png", 650, 350, "火力倍増");
		doublePowerButton.setEnabled(false);
        add(doublePowerButton);
		
		

		// doublePowerButton のアクションリスナー
		doublePowerButton.addActionListener(e -> {
			doublePowerButton.setEnabled(false); // 自分自身を無効化
			watchButton.setEnabled(false); // ウォッチボタンを無効化
			powerUp(); // 火力倍増の処理
		});
		
		
		replayButton = new JButton("再プレイ");
		replayButton.setBounds(300, 450, 100, 50);
		replayButton.setVisible(false); // 初期状態では非表示
		replayButton.addActionListener(e -> resetGame());
		add(replayButton);

		exitButton = new JButton("終了");
		exitButton.setBounds(450, 450, 100, 50);
		exitButton.setVisible(false); // 初期状態では非表示
		exitButton.addActionListener(e -> System.exit(0)); // アプリケーションを終了
		add(exitButton);

        // ウィンドウを作成する
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setTitle("メルヘンロシアンルーレット");
        setVisible(true);

        Socket socket = null;
		try {
			socket = new Socket(serverIP, 10000);
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "サーバーへの接続に失敗しました: " + e.getMessage());
			System.exit(1);
		}
		
		 // Initialize chamber
        ratio();
        MesgRecvThread mrt = new MesgRecvThread(socket, myName); //受信用のスレッドを作成する
        mrt.start(); //スレッドを動かす（Runが動く）
    }
	
	// コンストラクタ外にメソッドを追加
		private void shootMyself() {
			// 自分を撃つ処理をシミュレート（trueを引数としてhandleShotメソッドを呼び出す）
			handleShot();
		}

		private void shootOpponent() {
			// 相手を撃つ処理をシミュレート（falseを引数としてhandleShotメソッドを呼び出す）
			powerShot();
		}

		private void powerUp() {
			// ダメージを2倍にする処理を呼び出す
			doublepower();
		}
		
		private void playSound(String soundFileName) {
			
			try {
				// ファイルのパスを指定してAudioInputStreamを取得
				File soundFile = new File(getClass().getResource("/" + soundFileName).toURI());
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
				Clip clip = AudioSystem.getClip();
				clip.open(audioStream);
				clip.start(); // 音を再生
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private JButton createButton(String iconPath, int x, int y, String labelText) {
			JButton button = new JButton(new ImageIcon(getClass().getResource(iconPath)));
			button.setBounds(x, y, 150, 80); // ボタンのサイズを調整
			button.setFocusPainted(false);
			button.setBackground(new Color(60, 63, 65));
			button.setOpaque(false);
			button.addMouseListener(this);
			button.setActionCommand(labelText);

			// ボタンの下に文字を追加
			JLabel label = new JLabel(labelText, SwingConstants.CENTER);
			label.setBounds(x, y + 80, 150, 30);
			label.setForeground(Color.WHITE);
			label.setFont(new Font("MS Gothic", Font.BOLD, 12)); // 日本語対応フォント
			add(label);

			// ボタンがクリックされたときの処理
			/*button.addActionListener(e -> {
			if (!"次の弾の確認".equals(labelText)) { // "次の弾の確認" 以外のボタンのみ音を再生
				if (isLiveRound) {
					playSound("burn.wav"); // 実弾
				} else {
					playSound("vacant.wav"); // 空弾
				}
				isLiveRound = !isLiveRound; // 弾の状態を切り替える
			}
		});*/


			return button;
		}
		
		
		// メッセージ受信のためのスレッド
    public class MesgRecvThread extends Thread {
        Socket socket;
        String myName;

        public MesgRecvThread(Socket s, String n) {
            socket = s;
            myName = n;
        }

		public void run() {
			try {
				boolean isDoublePowerActive = false;  // 火力倍増が有効かどうかを管理するフラグ
				InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(sisr);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(myName);//接続の最初に名前を送る
				String myNumberStr = br.readLine();
				int myNumberInt = Integer.parseInt(myNumberStr);
				myTurn = myNumberInt % 2 == 0 ? 0 : 1;  // Determine if the player is first or second
				// 初期ターンメッセージを表示
				if (myTurn == 0) {
					messageLabel.setText("あなたは先攻です！");
				} else {
					messageLabel.setText("あなたは後攻です！");
				}

				while (true) {
					String inputLine = br.readLine();
					if (inputLine != null) {
						String[] inputTokens = inputLine.split(" ");
						String cmd = inputTokens[0];	
						if (cmd.equals("Chamber")) {
							chamber = new ArrayList<>();
							for (int i = 0; i < inputTokens[1].length(); i++) {
								char c = inputTokens[1].charAt(i);
								chamber.add(Character.getNumericValue(c));
							}
							System.out.println(chamber);
						}


						if (cmd.equals("Watch")) {
							playSound("look.wav"); // watchButtonが押された時にlook.wavを再生
							// Handle "Watch" command to check the next bullet
							if (watchCount >= 0) {
								
								int nextBullet = chamber.get(0);
								
								if (myTurn == 0) {
									if(nextBullet == 1){
										messageLabel.setText("次の弾は実弾です！");
									}else{
										messageLabel.setText("次の弾は空弾です！");
									}
									watchCount--;
								} else {
									
									messageLabel.setText("相手は次の弾を確認しました");
									
								}
								
								if (watchCount == 0) {
									watchButton.setEnabled(false);
								}
								
							}
						}
						if (cmd.equals("Myself")) {
							int charge = chamber.remove(0); // 弾を1つ取り出す
							int decline = charge == 1 ? 1 * powerMultiplier : 0;	
							
												
							if(charge == 1){
								playSound("burn.wav");  // 実弾の音
								
								if(myTurn == 0){
									
									playerHealth -= decline;
									messageLabel.setText("自分に実弾命中！残り体力: " + playerHealth);
									myTurn = 1 - myTurn;
								}else{
									opponentHealth -= decline;
									messageLabel.setText("相手に実弾命中！残り体力: " + opponentHealth);
									myTurn = 1 - myTurn;
								}
							}else{
								playSound("vacant.wav");  // 空弾の音
								if(myTurn == 0){
									messageLabel.setText("空弾でした！残り体力: " + playerHealth);
									
								}else{
									messageLabel.setText("相手自身に打ちましたが空弾でした！残り体力: " + opponentHealth);
									
								}
								 
							}
							if (opponentHealth <= 0) {
								messageLabel.setText("あなたの勝ち！");
								endGame(); // 終了処理を呼び出す
							} else if (playerHealth <= 0) {
								messageLabel.setText("あなたの負け！");
								endGame(); // 終了処理を呼び出す
							}
							updateHealthLabels();
							
							if (isDoublePowerActive) {
								powerMultiplier = 1;  // 火力を元に戻す
								isDoublePowerActive = false;  // フラグをリセット
							}
							// ボタンの状態を更新
							updateButtonState(); 
							
						}
							
						
						if (cmd.equals("Shot")) {
							int charge = chamber.remove(0); // 弾を1つ取り出す
							int decline = charge == 1 ? 1 * powerMultiplier : 0;	
							
												
							if(charge == 1){
								playSound("burn.wav");  // 実弾の音
								
								if(myTurn == 0){
									opponentHealth -= decline;
									messageLabel.setText("相手に発砲し、実弾命中！相手の残り体力: " + opponentHealth);
									myTurn = 1 - myTurn;
								}else{
									playerHealth -= decline;
									messageLabel.setText("相手が発砲し実弾命中！残り体力: " + playerHealth);
									myTurn = 1 - myTurn;
								}
							}else{
								playSound("vacant.wav");  // 空弾の音
								
								if(myTurn == 0){
									messageLabel.setText("相手に打ちましたが空弾でした！残り体力: " + opponentHealth);
									myTurn = 1 - myTurn;
								}else{
									messageLabel.setText("相手が発砲、しかし空弾でした！残り体力: " + playerHealth);
									myTurn = 1 - myTurn;
								}
								 
							}
							if (opponentHealth <= 0) {
								messageLabel.setText("あなたの勝ち！");
								endGame(); // 終了処理を呼び出す
							} else if (playerHealth <= 0) {
								messageLabel.setText("あなたの負け！");
								endGame(); // 終了処理を呼び出す
							}
							updateHealthLabels();
							
							if (isDoublePowerActive) {
								powerMultiplier = 1;  // 火力を元に戻す
								isDoublePowerActive = false;  // フラグをリセット
							}
							// ボタンの状態を更新
							updateButtonState(); 
							
							
						}
						if (cmd.equals("Double")) {
											
								
							// 火力倍増を有効にする
							powerMultiplier = 2;  // 火力倍増
							isDoublePowerActive = true;  // フラグを設定して、再度使用できないようにする
							messageLabel.setForeground(Color.YELLOW);  // 文字色を黄色に設定
							messageLabel.setText("火力倍増！次のターンは倍のダメージを与えます！");
							doublePowerButton.setEnabled(false);
							
						
						}
						// 自分のターンになった場合
						if (cmd.equals("TurnSwitch")) {
							turnCount++; // ターン数を1増加
							if(myTurn == 0){
							
								SwingUtilities.invokeLater(() -> {
									messageLabel.setText("あなたのターンです。");
									updateButtonState(); // ボタンを有効化
								});
						   }else{
							   SwingUtilities.invokeLater(() -> {
								messageLabel.setText("相手のターンです");
								updateButtonState(); // ボタンを無効化
							});
						   }
						}
						

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}


		
		private void updateButtonState() {
			// 自分のターンの場合（myTurn == 0）はボタンを有効にし、後攻の場合（myTurn == 1）は無効にする
			boolean isMyTurn = (myTurn == 0); // 自分のターンのみ有効

			// 各ボタンの有効・無効を設定
			watchButton.setEnabled(isMyTurn);
			myselfPistolButton.setEnabled(isMyTurn);
			shotButton.setEnabled(isMyTurn);
			doublePowerButton.setEnabled(isMyTurn);
		}

		



		private void ratio() {
			
			tmp = new ArrayList<>();
			Random rand = new Random();

			// 実弾、空弾を最低でも1つ配置
			tmp.add(1); // 実弾1つ
			tmp.add(0); // 空弾1つ

			// 残り4つをランダムに実弾または空弾に設定
			int remainingBulletCount = 4;
			for (int i = 0; i < remainingBulletCount; i++) {
				tmp.add(rand.nextInt(2)); // 0または1（空弾または実弾）
			}

			Collections.shuffle(tmp); // 弾の順番をランダムに並べ替え

			String message = "Chamber ";
			for (int j = 0; j < tmp.size(); j++) {
				message += Integer.toString(tmp.get(j));
			}

			out.println(message);
		}



		private void startGame() {

			// デバッグ出力
			System.out.println("chamber の内容: " + chamber);

			int realBullets = Collections.frequency(chamber, 1);
			int emptyBullets = Collections.frequency(chamber, 0);

			bulletCountLabel.setText("実弾: " + realBullets + " / 空弾: " + emptyBullets);

			startButton.setVisible(false);
			messageLabel.setText("ゲームが開始されました！");
			updateButtonState();
		}


   @Override
	public void mouseClicked(MouseEvent e) {
		JButton clickedButton = (JButton) e.getComponent();
		String action = clickedButton.getActionCommand();  // ActionCommandを取得

		if (myTurn == 0) { // 自分のターンの場合
			
			switch (action) {
				case "次の弾の確認":
					watchaction(); // 次の弾の確認処理を呼び出す
					break;
				case "自分に打つ":
					shootMyself(); // 自分に打つ処理
					break;
				case "相手に打つ":
					shootOpponent(); // 相手に打つ処理
					break;
				case "火力倍増":
					powerUp(); // 火力倍増の処理
					break;
				default:
					System.out.println("Unknown action: " + action);
					break;
			}
		} else {
			messageLabel.setText("相手のターンです");
		}
	}




		private void watchaction() {
			String message = "Watch ";
			 out.println(message);
			/*if (watchCount > 0) {
				playSound("look.wav"); // watchButtonが押された時にlook.wavを再生
				int nextBullet = chamber.get(0);
				if (nextBullet == 1) {
					messageLabel.setFont(new Font("MS Gothic", Font.BOLD, 16)); // 日本語対応フォント設定
					messageLabel.setText("次の弾は実弾です！");
				} else {
					messageLabel.setFont(new Font("MS Gothic", Font.BOLD, 16)); // 日本語対応フォント設定
					messageLabel.setText("次の弾は空弾です！");
				}
				watchCount--;
				if (watchCount == 0) {
					watchButton.setEnabled(false);
				}
			}*/
		}

	   //private void handleShot(boolean isSelf) {
		private void handleShot(){
			String message = "Myself ";
			out.println(message);
			 
			 
		/*if (chamber.isEmpty()) {
			messageLabel.setText("弾がなくなった！再補充中...");
			ratio(); // 新しい弾を補充
			return;
		}
		
		
		int bullet = chamber.remove(0); // 弾を1つ取り出す
		int damage = bullet == 1 ? 1 * powerMultiplier : 0;
		for(int i = 0; i<chamber.size();i++){
			mine += Integer.toString(chamber.get(i));
		}
		if (isSelf) { // 自分が攻撃手（自身に撃つ）
			
			if (bullet == 1) {
				playerHealth -= damage;
				
				// 自分に実弾命中
				playSound("burn.wav");  // 実弾の音
				SwingUtilities.invokeLater(() -> {
					messageLabel.setText("自分に実弾命中！残り体力: " + playerHealth);
				});
			} else {
				playSound("vacant.wav");  // 空弾の音
				SwingUtilities.invokeLater(() -> {
					messageLabel.setText("空弾！自分は無傷です。残り体力: " + playerHealth);
				});
			}
			out.println(mine);
		} else { // 相手が攻撃手（相手に撃つ）
			if (bullet == 1) { // 実弾の場合
				opponentHealth -= damage;
				playSound("burn.wav");  // 実弾の音
				SwingUtilities.invokeLater(() -> {
					messageLabel.setText("相手に実弾命中！相手の体力: " + opponentHealth);
				});
			} else { // 空弾の場合
				playSound("vacant.wav");  // 空弾の音
				SwingUtilities.invokeLater(() -> {
					messageLabel.setText("空弾発射！相手の体力は減少しません。");
				});
			}
			out.println(mine);
		}*/


		// 体力が0になった場合のゲーム終了処理
		/*if (playerHealth <= 0) {
			gameOver("あなたの負け！");
		} else if (opponentHealth <= 0) {
			gameOver("あなたの勝ち！");
		}*/

		// 体力ラベルを更新
		//updateHealthLabels();

		// 相手に撃った場合、ターンを終了する処理
		/*if (!isSelf) { // 相手に撃った場合
			myTurn = 1 - myTurn; // ターンを切り替える
			if (bullet == 1) { // 実弾の場合
				messageLabel.setText("実弾発射！相手のターンです。");
			} else { // 空弾の場合
				messageLabel.setText("空弾発射！相手のターンです。");
			}
			out.println("TurnSwitch"); // サーバーにターン切り替えを通知
			out.flush();
		} else { // 自分のターン（myself）
			if (bullet == 1) {
				messageLabel.setText("実弾発射！ターンが終了します。");
				myTurn = 1 - myTurn;
				out.println("TurnSwitch"); // サーバーにターン切り替えを通知
				out.flush();
			} else {
				messageLabel.setText("空弾発射！自分のターンが続きます。");
				// 空弾だった場合、ターンは続行
				return;
			}
			out.println("TurnSwitch"); // サーバーにターン切り替えを通知
			out.flush();
		}*/
		
		// 火力倍増を元に戻す処理
		/*if (isDoublePowerActive) {
			powerMultiplier = 1;  // 火力を元に戻す
			isDoublePowerActive = false;  // フラグをリセット
		}
		// ボタンの状態を更新
		updateButtonState(); */
		
	}
	
		private void powerShot(){
			 String message = "Shot ";
			 out.println(message);
		}

		
		
		

		private void updateHealthLabels() {
			playerHealthLabel.setText("Player Health: " + playerHealth);
			opponentHealthLabel.setText("Opponent Health: " + opponentHealth);
		}



    

		//private boolean isDoublePowerActive = false;  // 火力倍増が有効かどうかを管理するフラグ

		private void doublepower() {
			String message = "Double ";
			 out.println(message);
			
			/*if (isDoublePowerActive) {
				// 既に火力倍増が有効な場合
				messageLabel.setText("火力倍増は既に使用済みです！");
				return;  // 何もしない
			}
			
			// 火力倍増を有効にする
			powerMultiplier = 2;  // 火力倍増
			isDoublePowerActive = true;  // フラグを設定して、再度使用できないようにする
			messageLabel.setForeground(Color.YELLOW);  // 文字色を黄色に設定
			messageLabel.setText("火力倍増！次のターンは倍のダメージを与えます！");

			// ボタンを無効にする
			doublePowerButton.setEnabled(false);
			
			// 火力倍増が適用されたターンが終わった後に元に戻す
			
			// powerMultiplier = 1;  // 火力を元に戻す*/
		}
		



		

		
		
		private void endGame() {
			// メインボタンを無効化
			watchButton.setEnabled(false);
			myselfPistolButton.setEnabled(false);
			shotButton.setEnabled(false);
			doublePowerButton.setEnabled(false);

			// 再プレイと終了ボタンを表示
			replayButton.setVisible(true);
			exitButton.setVisible(true);
		}
		
		private void resetGame() {
			// ゲームの状態を初期化
			playerHealth = 10; // 初期体力
			opponentHealth = 10; // 初期体力
			powerMultiplier = 1; // 火力倍率の初期化
			watchCount = 2;
			turnCount = 0; // ターン数をリセット
			isLiveRound = true; // 弾の状態初期化
			chamber.clear(); // チェンバーをリセット
			ratio(); // チェンバーの初期化

			// ラベルをリセット
			messageLabel.setText("ゲームがリセットされました。新しいゲームを開始します！");
			updateHealthLabels();

			// ボタンの状態をリセット
			replayButton.setVisible(false); // 再プレイボタンを非表示
			exitButton.setVisible(false);   // 終了ボタンを非表示
			updateButtonState();            // メインボタンの有効化
		}


		
		


		private void updateBulletCountLabel() {
			int realBullets = Collections.frequency(chamber, 1);
			int emptyBullets = Collections.frequency(chamber, 0);
			bulletCountLabel.setText("実弾: " + realBullets + " / 空弾: " + emptyBullets);
		}
	   



    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    public static void main(String[] args) {
        new MyClient();
    }
}


	   
=======

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

public class MyClient extends JFrame implements MouseListener, MouseMotionListener {
    private JButton shotButton, myselfPistolButton, watchButton, doublePowerButton, startButton;
    private JLabel playerHealthLabel, opponentHealthLabel, messageLabel, bulletCountLabel;
    private int playerHealth = 3;
    private int opponentHealth = 3;
    private ArrayList<Integer> chamber;
    private int myTurn; // 0: 先攻, 1: 後攻
    private int watchCount = 2;
    private int powerMultiplier = 1; // 火力の倍率
    private PrintWriter out;
    private String serverIP;
    private String myName;
    private Socket socket;	
	 // 弾の状態を保持する変数
    private boolean isLiveRound = true;  // 実弾か空弾か（true: 実弾、false: 空弾）

    public MyClient() {
        // 名前の入力ダイアログを開く
        String myName = JOptionPane.showInputDialog(null, "名前を入力してください", "名前の入力", JOptionPane.QUESTION_MESSAGE);
        if (myName == null || myName.equals("")) {
            myName = "No name"; // nullまたは空文字列の場合はデフォルト名を設定
        }

        String serverIP = JOptionPane.showInputDialog(null, "サーバーの名前を入力してください", "サーバーの入力", JOptionPane.QUESTION_MESSAGE);
        if (serverIP == null || serverIP.equals("")) {
            serverIP = "localhost"; // nullまたは空文字列の場合はlocalhostを設定
        }

        // Setup JFrame with custom background
        setContentPane(new BackgroundPanel("/tabletop.png"));
        setLayout(null);

        // Player and opponent icons
        JLabel playerIcon = new JLabel(new ImageIcon(getClass().getResource("images.png")));
        playerIcon.setBounds(50, 50, 100, 100);
        add(playerIcon);

        JLabel opponentIcon = new JLabel(new ImageIcon(getClass().getResource("images.png")));
        opponentIcon.setBounds(650, 50, 100, 100);
        add(opponentIcon);

        // Start button
        startButton = new JButton("ゲーム開始");
        startButton.setBounds(300, 250, 200, 50);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        add(startButton);

        // Bullet count label (for displaying bullet counts)
        bulletCountLabel = new JLabel();
        bulletCountLabel.setBounds(250, 200, 300, 30);
        bulletCountLabel.setFont(new Font("MS Gothic", Font.BOLD, 16)); // 日本語対応フォント設定
        bulletCountLabel.setForeground(Color.YELLOW);
        add(bulletCountLabel);

        // Health labels
        playerHealthLabel = new JLabel("Player Health: " + playerHealth);
        playerHealthLabel.setBounds(50, 160, 200, 30);
        playerHealthLabel.setForeground(Color.WHITE);
        add(playerHealthLabel);

        opponentHealthLabel = new JLabel("Opponent Health: " + opponentHealth);
        opponentHealthLabel.setBounds(650, 160, 200, 30);
        opponentHealthLabel.setForeground(Color.WHITE);
        add(opponentHealthLabel);

        // Message label
        messageLabel = new JLabel("ゲーム開始", SwingConstants.CENTER);
        messageLabel.setBounds(200, 500, 400, 30);
        messageLabel.setFont(new Font("MS Gothic", Font.BOLD, 16)); // 日本語対応フォント設定
        messageLabel.setForeground(Color.YELLOW);
        add(messageLabel);

        // Buttons placed below the start button
       //ボタン作成
		watchButton = createButton("eye.jpg", 50, 350, "次の弾の確認");
		watchButton.setEnabled(false);
		watchButton.addActionListener(e -> {
			watchaction(); // 次の弾の確認処理
		});
		add(watchButton);


        myselfPistolButton = createButton("myself_pistol.jpg", 250, 350, "自分に打つ");
		myselfPistolButton.setEnabled(false);
        add(myselfPistolButton);

        shotButton = createButton("pistol.jpeg", 450, 350, "相手に打つ");
		shotButton.setEnabled(false);
        add(shotButton);

        doublePowerButton = createButton("double_power.jpg", 650, 350, "火力倍増");
		doublePowerButton.setEnabled(false);
        add(doublePowerButton);

        // ウィンドウを作成する
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setTitle("銃撃ゲーム");
        setVisible(true);

        // Connect to server
        Socket socket = null;
        try {
            socket = new Socket(serverIP, 10000);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            // MesgRecvThreadのコンストラクタに適切な引数を渡す
            new MesgRecvThread(socket, myName).start();
            out.println(new String(myName.getBytes("UTF-8"), "UTF-8")); // 名前もUTF-8で送信
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "サーバーへの接続に失敗しました: " + e.getMessage());
            System.exit(1);
        }

        // Initialize chamber
        ratio();
        MesgRecvThread mrt = new MesgRecvThread(socket, myName); //受信用のスレッドを作成する
        mrt.start(); //スレッドを動かす（Runが動く）
    }

    // Add methods outside the constructor
    private void shootMyself() {
        handleShot(true); // Call the existing `handleShot()` method with true to simulate self-shot
    }

    private void shootOpponent() {
        handleShot(false); // Call the existing `handleShot()` method with false to simulate shooting the opponent
    }

    private void doublePower() {
        doublepower(); // Call the existing `doublepower()` method to double the damage
    }
	
	 private void playSound(String soundFileName) {
		
		try {
			// ファイルのパスを指定してAudioInputStreamを取得
			File soundFile = new File(getClass().getResource("/" + soundFileName).toURI());
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start(); // 音を再生
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 



    private JButton createButton(String iconPath, int x, int y, String labelText) {
		JButton button = new JButton(new ImageIcon(getClass().getResource(iconPath)));
		button.setBounds(x, y, 150, 80); // ボタンのサイズを調整
		button.setFocusPainted(false);
		button.setBackground(new Color(60, 63, 65));
		button.setOpaque(false);
		button.addMouseListener(this);
		button.setActionCommand(labelText);

		// ボタンの下に文字を追加
		JLabel label = new JLabel(labelText, SwingConstants.CENTER);
		label.setBounds(x, y + 80, 150, 30);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("MS Gothic", Font.BOLD, 12)); // 日本語対応フォント
		add(label);

		// ボタンがクリックされたときの処理
		button.addActionListener(e -> {
		if (!"次の弾の確認".equals(labelText)) { // "次の弾の確認" 以外のボタンのみ音を再生
			if (isLiveRound) {
				playSound("burn.wav"); // 実弾
			} else {
				playSound("vacant.wav"); // 空弾
			}
			isLiveRound = !isLiveRound; // 弾の状態を切り替える
		}
	});


		return button;
	}

	

    // メッセージ受信のためのスレッド
    public class MesgRecvThread extends Thread {
        Socket socket;
        String myName;

        public MesgRecvThread(Socket s, String n) {
            socket = s;
            myName = n;
        }

        public void run() {
            try {
                InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
                BufferedReader br = new BufferedReader(sisr);
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println(myName);  // Send name at the start of the connection

                String myNumberStr = br.readLine();
                int myNumberInt = Integer.parseInt(myNumberStr);
                myTurn = myNumberInt % 2 == 0 ? 0 : 1;  // Determine if the player is first or second
                // 初期ターンメッセージを表示
                if (myTurn == 0) {
                    messageLabel.setText("あなたは先攻です！");
                } else {
                    messageLabel.setText("あなたは後攻です！");
                }

                while (true) {
                    String inputLine = br.readLine();
                    if (inputLine != null) {
                        String[] inputTokens = inputLine.split(" ");
                        String cmd = inputTokens[0];

                        if (cmd.equals("Watch")) {
                            // Handle "Watch" command to check the next bullet
                            if (watchCount > 0) {
                                int nextBullet = chamber.get(0);  // Look at the next bullet in the chamber
                                if (nextBullet == 1) {
                                    messageLabel.setText("次の弾は実弾です！");
                                } else {
                                    messageLabel.setText("次の弾は空弾です！");
                                }
                                watchCount--;  // Decrease watch count
                                updateBulletCountLabel();  // Update the bullet count on the UI
                                if (watchCount == 0) {
                                    watchButton.setEnabled(false);  // Disable watch button if no more watches left
                                }
                            } else {
                                messageLabel.setText("次の弾の確認はもうできません。");
                            }
                        }
                        if (cmd.equals("Myself")) {
                            // Handle "Myself" command when the player shoots themselves
                            handleShot(true);  // Invoke the shot method with 'true' to simulate a self-shot
                        }
                        if (cmd.equals("Opponent")) {
                            // Handle "Opponent" command when the player shoots the opponent
                            handleShot(false);  // Invoke the shot method with 'false' to simulate shooting the opponent
                        }
                        if (cmd.equals("Double")) {
                            // Handle "Double" command when the player uses double power
                            doublepower();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	
	 // Update button states based on the turn
	  private void updateButtonState() {
		// 自分のターンの場合（myTurn == 0）はボタンを有効にし、後攻の場合（myTurn == 1）は無効にする
		boolean isMyTurn = (myTurn == 0); // 自分のターンのみ有効

		// 各ボタンの有効・無効を設定
		watchButton.setEnabled(isMyTurn);
		myselfPistolButton.setEnabled(isMyTurn);
		shotButton.setEnabled(isMyTurn);
		doublePowerButton.setEnabled(isMyTurn);
	}



    private void ratio() {
        chamber = new ArrayList<>();
        Random rand = new Random();

        // 実弾を最低でも1つ配置
        chamber.add(1); // 実弾1つ

        // 残り5つをランダムに実弾または空弾に設定
        int remainingBulletCount = 5;
        for (int i = 0; i < remainingBulletCount; i++) {
            chamber.add(rand.nextInt(2)); // 0または1（空弾または実弾）
        }

        Collections.shuffle(chamber); // 弾の順番をランダムに並べ替え
    }

    private void startGame() {
        // ゲーム開始時に実弾と空弾の数を表示
        int realBullets = Collections.frequency(chamber, 1);
        int emptyBullets = Collections.frequency(chamber, 0);
        bulletCountLabel.setText("実弾: " + realBullets + " / 空弾: " + emptyBullets);

        // スタートボタンを非表示にする
        startButton.setVisible(false);

        // ゲーム開始メッセージを更新
        messageLabel.setText("ゲームが開始されました！");
		
		updateButtonState();
    }

   @Override
	public void mouseClicked(MouseEvent e) {
		JButton clickedButton = (JButton) e.getComponent();
		String action = clickedButton.getActionCommand();  // ActionCommandを取得
		System.out.println("Button clicked: " + action);  // Debug print

		if (myTurn == 0) { // 自分のターンの場合
			switch (action) {
				case "次の弾の確認":
					watchaction(); // 次の弾の確認処理を呼び出す
					break;
				case "自分に打つ":
					shootMyself(); // 自分に打つ処理
					break;
				case "相手に打つ":
					shootOpponent(); // 相手に打つ処理
					break;
				case "火力倍増":
					doublePower(); // 火力倍増の処理
					break;
				default:
					System.out.println("Unknown action: " + action);
					break;
			}
		} else {
			messageLabel.setText("相手のターンです");
		}
	}




    private void watchaction() {
    if (watchCount > 0) {
        playSound("look.wav"); // watchButtonが押された時にlook.wavを再生
        int nextBullet = chamber.get(0);
        if (nextBullet == 1) {
            messageLabel.setFont(new Font("MS Gothic", Font.BOLD, 16)); // 日本語対応フォント設定
            messageLabel.setText("次の弾は実弾です！");
        } else {
            messageLabel.setFont(new Font("MS Gothic", Font.BOLD, 16)); // 日本語対応フォント設定
            messageLabel.setText("次の弾は空弾です！");
        }
        watchCount--;
        if (watchCount == 0) {
            watchButton.setEnabled(false);
        }
    }
}

   private void handleShot(boolean isSelf) {
    if (chamber.isEmpty()) {
        messageLabel.setText("弾がなくなった！再補充中...");
        ratio(); // 新しい弾を補充
        return;
    }

    int bullet = chamber.remove(0); // 弾を1つ取り出す
    int damage = bullet == 1 ? 1 * powerMultiplier : 0;

    if (isSelf) { // 自分が攻撃手（自身に撃つ）
        playerHealth -= damage;
        if (bullet == 1) {
            // 自分に実弾命中
			playSound("burn.wav");  // 実弾の音
            messageLabel.setText("自分に実弾命中！残り体力: " + playerHealth);
        } else {
			playSound("vacant.wav");  // 空弾の音
            messageLabel.setText("空弾！自分は無傷です。残り体力: " + playerHealth);
        }
    } else { // 相手が攻撃手（相手に撃つ）
        if (bullet == 1) { // 実弾の場合
            opponentHealth -= damage;
			playSound("burn.wav");  // 実弾の音
            messageLabel.setText("相手に実弾命中！相手の体力: " + opponentHealth);
        } else { // 空弾の場合
			playSound("vacant.wav");  // 空弾の音
            messageLabel.setText("空弾発射！相手の体力は減少しません。");
        }
		out.println("TurnSwitch"); // サーバーにターン切り替えを通知
		out.flush();

    }

    // 体力が0になった場合のゲーム終了処理
    if (playerHealth <= 0) {
        gameOver("あなたの負け！");
    } else if (opponentHealth <= 0) {
        gameOver("あなたの勝ち！");
    }

    // 体力ラベルを更新
    updateHealthLabels();

    // 相手に撃った場合、ターンを終了する処理
    if (!isSelf) { // 相手に撃った場合
        myTurn = 1 - myTurn; // ターンを切り替える
        if (bullet == 1) { // 実弾の場合
            messageLabel.setText("実弾発射！相手のターンです。");
        } else { // 空弾の場合
            messageLabel.setText("空弾発射！相手のターンです。");
        }
        out.println("TurnSwitch"); // サーバーにターン切り替えを通知
        out.flush();
    } else { // 自分のターン（myself）
        if (bullet == 1) {
            messageLabel.setText("実弾発射！ターンが終了します。");
        } else {
            messageLabel.setText("空弾発射！自分のターンが続きます。");
            // 空弾だった場合、ターンは続行
            return;
        }
    }
	// ボタンの状態を更新
    updateButtonState(); 
}



private void updateHealthLabels() {
    playerHealthLabel.setText("Player Health: " + playerHealth);
    opponentHealthLabel.setText("Opponent Health: " + opponentHealth);
}


    private void gameOver(String message) {
        messageLabel.setText(message);
        shotButton.setEnabled(false);
        myselfPistolButton.setEnabled(false);
        watchButton.setEnabled(false);
        doublePowerButton.setEnabled(false);
        // ゲーム終了後の処理
        // 必要に応じて追加処理（リセットや再起動）など
    }

    

    private void doublepower() {
        powerMultiplier = 2;  // 火力倍増
        messageLabel.setForeground(Color.YELLOW);  // 文字色を黄色に設定
        messageLabel.setText("火力倍増！次のターンは倍のダメージを与えます！");
		 // ここで相手に対して攻撃を行い、相手の体力が0以下なら終了処理を行う
		if (opponentHealth <= 0) {
			gameOver("あなたの勝ち！");  // 相手の体力が0以下なら勝ちとしてゲーム終了
		} else if (playerHealth <= 0) {
			gameOver("あなたの負け！");  // 自分の体力が0以下なら負けとしてゲーム終了
		}
    }

    private void updateBulletCountLabel() {
        int realBullets = Collections.frequency(chamber, 1);
        int emptyBullets = Collections.frequency(chamber, 0);
        bulletCountLabel.setText("実弾: " + realBullets + " / 空弾: " + emptyBullets);
    }



    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    public static void main(String[] args) {
        new MyClient();
    }
}
>>>>>>> 0b71db5bf5f116252b22ea6bda1e5c0795b4a2be
