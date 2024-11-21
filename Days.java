class Days {
	public static void main(String[] args) {
		int month, day;

		month = 8;

		switch (month) {
			case  1:
			case  3:
			case  5:
			case  7:
			case  8:
			case 10:
			case 12: day = 31;
			         break;
			case  2: day = 28;
			         break;
			default: day = 30;
		}
		System.out.print(month);
		System.out.print("Œ‚Í");
		System.out.print(day);
		System.out.println("“ú‚Å‚·B");
	}
}
