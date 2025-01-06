import java.util.Scanner;

class HimanCheck {
	public static void main(String[] args) {
		class Riddle {
    private int hp;          // 体力
    private String name;     // 名前

    public Riddle(int hp, String name) {
        this.hp = hp;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void attack(Enemy enemy) {
        System.out.println(name + "の攻撃！");
        int damage = 10; // ダメージ量は仮に10とする
        enemy.takeDamage(damage);
        System.out.println(enemy.getName() + "に" + damage + "のダメージを与えた！");
    }

    public void run() {
        System.out.println(name + "は逃げ出した！");
        System.exit(0); // プログラムを終了
    }
}
	class Enemy {
		private int hp;          // 敵の体力
		private String name;     // 名前

		public Enemy(int hp, String name) {
			this.hp = hp;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public int getHp() {
			return hp;
		}

		public void takeDamage(int damage) {
			hp -= damage; // ダメージを受けた分だけHPを減少させる
			if (hp < 0) {
				hp = 0;
			}
		}
	}
	}
}

