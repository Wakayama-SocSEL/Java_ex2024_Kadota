import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RussianRouletteGUI {
    private JFrame frame;
    private JButton healButton, shotButton, passButton, watchButton, boostButton;
    private JLabel playerHealthLabel, opponentHealthLabel, messageLabel, bulletLabel;
    private int playerHealth = 3; // プレイヤー初期体力
    private int opponentHealth = 3; // 対戦相手初期体力
    private ArrayList<Integer> chamber; // 弾丸
    private int myTurn; // プレイヤーのターン
    private int watchCount = 2; // Watchの使用回数
    private int boostCount = 2; // 火力2倍アイテムの使用回数
    private boolean damageBoost = false; // 火力2倍の状態
    private boolean actionTaken = false; // 1ターン内で行動済みかどうか


	//passボタンの作成
		passButton = new JButton("相手に打つ");
		passButton.setBounds(525,225,150,80);
		passButton.addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
		passButton.setActionCommand("PASS");
		c.add(passButton);

	public class pass{
			
		public static void main(String[] args) {
			public static void main(String[] args) {
				 while (true) {
					try {
						System.out.print("コマンドを入力してください: ");
						String inputLine = br.readLine(); // データを一行分だけ読み込む
						if (inputLine != null) {
							String[] inputTokens = inputLine.split(" "); // 入力をスペースで分割
							String cmd = inputTokens[0]; // 最初のトークンをコマンドとして扱う
							if (cmd.equals("PASS")) { // cmdが"PASS"の場合の処理
								// 弾丸を1発取り出す
								int bullet = chamber.remove(0); // リストから最初の弾を取り出す
								if (bullet == 1) {
									// 実弾だった場合
									playerHealth -= 1; // プレイヤーの体力を1減らす
									System.out.println("実弾！体力が減少しました。現在の体力: " + playerHealth);
									myTurn--; // 自分のターンを終了
								} else {
									// 空弾だった場合
									System.out.println("セーフ！もう一度引いてください。");
									// myTurnを維持して再度試行
									myTurn += 0;
								}
							}
						}
					}
			 }
			}
		}
	}
}
