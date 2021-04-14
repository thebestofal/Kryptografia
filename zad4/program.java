//author: Marek Skrzypkowski
import java.nio.file.Files;
import java.io.*;
import java.util.*;

class Main
{
	public static void runHashFunc(String function) throws IOException, InterruptedException
	{
		Process p;
		String[] cmd = 
					{"/bin/sh",
					"-c",
					"cat hash.pdf personal.txt | "+function+" >> hash.txt;"+
					"cat hash.pdf personal_.txt | "+function+" >> hash.txt"};
		p = Runtime.getRuntime().exec(cmd, null, new File("."));
		p.waitFor();
		p.destroy();
	}

	public static void main(String[] args) throws IOException, InterruptedException
	{

		File f = new File("hash.txt");
		f.createNewFile();
		FileReader hash_f = new FileReader(f);
		BufferedReader hash = new BufferedReader(hash_f);

		String[] functions = {"md5sum", "sha1sum", "sha224sum", "sha256sum", 
							  "sha384sum", "sha512sum", "b2sum"};
		for(String function : functions)
		{
			runHashFunc(function);
		}

		for(int iter = 0; iter < 6; iter++)
		{
			String personal = hash.readLine().replaceAll("  -", "");
			String personal_ = hash.readLine().replaceAll("  -", "");
			
			byte[] b_personal = personal.getBytes();
			byte[] b_personal_ = personal_.getBytes();
			int diff_bits = 0;
			for(int i = 0; i < b_personal.length; i++)
			{
				long a = Character.getNumericValue(personal.charAt(i));
				long b = Character.getNumericValue(personal_.charAt(i));
				diff_bits += Long.bitCount( a ^ b);
			}
			
			System.out.println(personal);
			System.out.println(personal_);
			int same_bits = b_personal.length*4 - diff_bits;
			System.out.print("Liczba rozniacych sie bitow: " + diff_bits
			+" z "+b_personal.length*4 + ", procentowo: " +
			((double)diff_bits / (b_personal.length*4))*100 + "%.");
			System.out.println();
		}
	}
}
