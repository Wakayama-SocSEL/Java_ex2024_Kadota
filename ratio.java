import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ratio{
		
		//ämó¶ëÄçÏ
	public static void main(String[] args) {
		Random rand = new Random();
		int all_bullet = 6;
		int Gun_bullet = rand.nextInt(all_bullet) + 1;
		List<String>chamber = new ArrayList<String>();
		
	for(int i = 0; i < Gun_bullet; i++){
			chamber.add("1");
		}
		for(int i = 0; i < all_bullet - Gun_bullet; i++){
			chamber.add("0");
		}
		Collections.shuffle(chamber);
		int cnt = 0;
		for(String str: chamber){
			if("1".equals(str)){
				cnt++;			
			}
		}
		System.out.println("é¿íeÇÃå¬êî: " + cnt);
}

		
