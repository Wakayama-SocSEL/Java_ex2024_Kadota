import java.util.Scanner;

class HimanCheck {
	public static void main(String[] args) {
		class Riddle {
    private int hp;          // �̗�
    private String name;     // ���O

    public Riddle(int hp, String name) {
        this.hp = hp;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void attack(Enemy enemy) {
        System.out.println(name + "�̍U���I");
        int damage = 10; // �_���[�W�ʂ͉���10�Ƃ���
        enemy.takeDamage(damage);
        System.out.println(enemy.getName() + "��" + damage + "�̃_���[�W��^�����I");
    }

    public void run() {
        System.out.println(name + "�͓����o�����I");
        System.exit(0); // �v���O�������I��
    }
}
	class Enemy {
		private int hp;          // �G�̗̑�
		private String name;     // ���O

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
			hp -= damage; // �_���[�W���󂯂�������HP������������
			if (hp < 0) {
				hp = 0;
			}
		}
	}
	}
}

