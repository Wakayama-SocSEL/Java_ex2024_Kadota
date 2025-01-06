if (cmd.equals("PASS")) { // cmdが"PASS"の場合の処理
    // 弾丸を1発取り出す
    int consume = all_bullet.remove(0); // リストから最初の弾を取り出す
    if (consume == 1) {
        // 実弾だった場合
        health[player] -= 1; // プレイヤーの体力を1減らす
        System.out.println("実弾！体力が減少しました。現在の体力: " + health[player]);
        myTurn--; // 自分のターンを終了
    } else {
        // 空弾だった場合
        System.out.println("セーフ！もう一度引いてください。");
        // myTurnを維持して再度試行
    }
}
