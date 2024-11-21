public class ScopeTest{

	static int a;

	public static void main(String[] args) {
		a=5;
		System.out.println("‰Šú‰»’¼Œã a="+a);
		scope1();

		System.out.println("scope1()‚ğŒÄ‚ñ‚¾Œã a="+a);
		scope2();
		System.out.println("scope2()‚ğŒÄ‚ñ‚¾Œã a="+a);

		for(int i=0;i<3;i++){
			int a = 0;
			a = a + i;
			System.out.println("for•¶‚Ì’† a="+a);
		}
		System.out.println("for•¶‚ğ”²‚¯‚½Œã a="+a);
	}
	
	public static void scope1(){
		a=10;
		System.out.println("scope1‚Ì’† a="+a);
	}
	
	public static void scope2(){
		int a=20;
		System.out.println("scope2‚Ì’† a="+a);
	}

}
