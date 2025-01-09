public class shot{		
		
	if(cmd.equals("Shot")){//cmdの文字と"Shot"が同じか調べる．同じ時にtrueとなる
							//Shotの時の処理
		String theBName = inputTokens[1];//ボタンの名前（番号）の取得
		int theBnum = Integer.parseInt(theBName);//ボタンの名前を数値に変換する
		int consume = all_bullet.remove(0); // リストから最初の弾を取り出す
		if (consume == 1){
			health[player] -= 1; // プレイヤーの体力を1減らす
			System.out.println("実弾！体力が減少しました。現在の体力: " + health[player]);
			myTurn--; // 自分のターンを終了
			if(cmd.equals("third")){
				health[player] -= 3; // プレイヤーの体力を1減らす
				System.out.println("実弾！体力が減少しました。現在の体力: " + health[player]);
				myTurn--; // 自分のターンを終了
			}
		}
		else{
			health[player] -= 0; // プレイヤーの体力を1減らす
			myTurn--; // 自分のターンを終了
		}
	}
}