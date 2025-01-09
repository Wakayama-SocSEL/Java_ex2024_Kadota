import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
private int watchCount = 2; // Watchの使用回数

public class watch{
		
	public static void main(String[] args) {
		 while (true) {
            try {
                System.out.print("コマンドを入力してください: ");
                String inputLine = br.readLine(); // データを一行分だけ読み込む
                if (inputLine != null) {
                    String[] inputTokens = inputLine.split(" "); // 入力をスペースで分割
                    String cmd = inputTokens[0]; // 最初のトークンをコマンドとして扱う

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
				}else{
					watchButton.setEnabled(false);
				}
					}
				}
			}
		 }
	}
}
