import java.util.Random;

public class Hello {
    public static void main(String[] args) {
		Random rand = new Random();
		int num = rand.nextInt(5) + 1;
		System.out.println(num);
    }
}