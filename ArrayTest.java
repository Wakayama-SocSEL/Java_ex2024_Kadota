class ArrayTest {
	public static void main(String[] args) {
		int[] a = {1, 2, 3};
		int[] b;
		b = a;
		a[0]=5;

		System.out.println(b[0]);
		System.out.println(b[1]);
		System.out.println(b[2]);

	}
}
