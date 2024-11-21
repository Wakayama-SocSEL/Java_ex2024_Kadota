public class MethodTest{
	
	public static void main(String[] args) {
		double x=0;
		System.out.println("x\tx^2\tx^3");
		for (int i=0;i<21;i++){
			System.out.print(x+"\t");
			System.out.print(square(x)+"\t");
			System.out.println(cube(x)+"\t");
			x+=0.5;
		}
	}
	
	//二乗を行う関数（int版）
	public static int square(int num){
		return num*num;
	}
	
	//二乗を行う関数（double版）
	public static double square(double num){
		return num*num;
	}
	
	public static double cube(double num){
		return num*num*num;
		
	}
}