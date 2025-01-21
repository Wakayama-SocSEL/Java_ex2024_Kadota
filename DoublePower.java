 private void handleDoublePower() {
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
