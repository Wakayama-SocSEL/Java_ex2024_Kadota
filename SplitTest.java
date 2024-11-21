public class SplitTest {
    public static void main(String[] args) {

		String text = "GET 5 5 1";

		// Splitを使って命令を分割する
		String[] data = text.split(" ");

		// 分割した個々の要素を１行ずつ表示する．
		// data配列の要素数は.lengthで表される
		for (int i=0; i<data.length; i++) {
			System.out.println(data[i]);
		}

   }
}