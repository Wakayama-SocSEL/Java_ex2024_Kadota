class HimanCheck {
	public static void main(String[] args) {
		double height, weight, weightAve, fat;

		height = 1.68;
		weight = 50;

		weightAve = 22 * Math.pow(height,2) ;
		fat = (weight - weightAve) / weightAve * 100;

		System.out.print("あなたの肥満率は");
		System.out.print(fat);
		System.out.println("%です。");
		if (fat >= 25) {
			System.out.println("あなたは太りすぎです。");
		} else if (fat <= -15){
			System.out.println("あなたは痩せすぎです。");
		} else {
			System.out.println("あなたは太りすぎではありません。");
		}
	}
}
