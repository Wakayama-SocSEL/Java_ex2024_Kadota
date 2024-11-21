public class ScopeTest{

	static int a;

	public static void main(String[] args) {
		a=5;
		System.out.println("初期化直後 a="+a);
		scope1();

		System.out.println("scope1()を呼んだ後 a="+a);
		scope2();
		System.out.println("scope2()を呼んだ後 a="+a);

		for(int i=0;i<3;i++){
			int a = 0;
			a = a + i;
			System.out.println("for文の中 a="+a);
		}
		System.out.println("for文を抜けた後 a="+a);
	}
	
	public static void scope1(){
		a=10;
		System.out.println("scope1の中 a="+a);
	}
	
	public static void scope2(){
		int a=20;
		System.out.println("scope2の中 a="+a);
	}

}
