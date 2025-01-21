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
