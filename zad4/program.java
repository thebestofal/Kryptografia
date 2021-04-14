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
		Files.deleteIfExists(f.toPath());
		f.createNewFile();
		BufferedReader hash = new BufferedReader(new FileReader(f));
		
        Writer writer = new OutputStreamWriter(new FileOutputStream(new File("diff.txt")), "UTF-8");
        BufferedWriter diff = new BufferedWriter(writer);

		String[] functions = {"md5sum", "sha1sum", "sha224sum", "sha256sum", 
							  "sha384sum", "sha512sum", "b2sum"};
		for(String function : functions)
		{
			runHashFunc(function);
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

			
			int same_bits = b_personal.length*4 - diff_bits;

			diff.append(function+"\n");
			diff.append(personal+"\n");
			diff.append(personal_+"\n");
			
			diff.append("Liczba rozniacych sie bitow: " + diff_bits
			+" z "+b_personal.length*4 + ", procentowo: " +
			((double)diff_bits / (b_personal.length*4))*100 + "%.");
			diff.append("\n\n");
			diff.flush();

		}
			diff.close();
	}
}
