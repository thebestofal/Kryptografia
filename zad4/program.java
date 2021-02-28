//author: Marek Skrzypkowski
class Main
{
	public static void main(String[] args)
	{
		String personal = args[0];
		String personal_ = args[1];
		
		byte[] b_personal = personal.getBytes();
		byte[] b_personal_ = personal_.getBytes();
		int diff_bits = 0;
		for(int i = 0; i < b_personal.length; i++)
		{
			long a = Character.getNumericValue(args[0].charAt(i));
			long b = Character.getNumericValue(args[1].charAt(i));
			diff_bits += Long.bitCount( a ^ b);
		}
		
		System.out.println("******* "+diff_bits+" *********");
		int same_bits = b_personal.length*4 - diff_bits;
		System.out.print("Liczba rozniacych sie bitow: " + diff_bits
		+" z "+b_personal.length*4 + ", procentowo: " +
		((double)diff_bits / (b_personal.length*4))*100 + "%.");
	}
}
