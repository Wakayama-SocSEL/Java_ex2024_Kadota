class HimanCheck {
	public static void main(String[] args) {
		double height, weight, weightAve, fat;

		height = 1.68;
		weight = 50;

		weightAve = 22 * Math.pow(height,2) ;
		fat = (weight - weightAve) / weightAve * 100;

		System.out.print("���Ȃ��̔얞����");
		System.out.print(fat);
		System.out.println("%�ł��B");
		if (fat >= 25) {
			System.out.println("���Ȃ��͑��肷���ł��B");
		} else if (fat <= -15){
			System.out.println("���Ȃ��͑��������ł��B");
		} else {
			System.out.println("���Ȃ��͑��肷���ł͂���܂���B");
		}
	}
}
