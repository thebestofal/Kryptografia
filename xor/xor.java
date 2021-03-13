import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class xor
{
    static int LENGTH = 20; // 32 -1 
    private static String readLineByLineJava8(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();
        
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return contentBuilder.toString();
    }
    
    static String divideString(String str, int n)
    {
        
        int part_size;
        String all = "";
        str = str.replaceAll("\n", "");
        str = str.replaceAll("[,.!?:;'-0123456789]", "");
        str = str.toLowerCase();
        if (str.length() % n != 0)
        {
            str = str.substring(0, str.length()-(str.length()%n));
        }
        
        all += str.charAt(0);
        for (int i = 1; i< str.length(); i++)
        {
            if(i % n == 0)
                all += "\n";
            all += str.charAt(i);
        }
        return all;
    }
    public void prepare() throws IOException
    {
        String all = readLineByLineJava8("orig.txt");
        all = divideString(all, LENGTH-1);
        File plain = new File("plain.txt");
        Writer writer = new OutputStreamWriter(new FileOutputStream(plain), "UTF-8");
        BufferedWriter fout = new BufferedWriter(writer);
        fout.write(all);
        fout.flush();
        fout.close();
    }
    
    
    void encrypt() throws IOException
    {
        FileReader plain_f = new FileReader("plain.txt");
        BufferedReader plain = new BufferedReader(plain_f);
    
        File key_f = new File("key.txt");
        Scanner key_sc = new Scanner(key_f);
        String key = key_sc.nextLine();
        
        File crypto = new File("crypto.txt");
        Writer writer = new OutputStreamWriter(new FileOutputStream(crypto), "UTF-8");
        BufferedWriter fout = new BufferedWriter(writer);
        
        StringBuilder out = new StringBuilder();
        int ch;
        int i = 0;

        while ((ch = plain.read()) != -1)
        {
            if (ch == 10)
            {
                if( i < LENGTH - 1)
                    out.append(32^(int)key.charAt(i));
                out.append("\n");
                i = 0;
            }
            else if ((ch > 96 && ch < 123)|| ch == 32)
            {
                
                int xor_char = (int) ch ^ (int)key.charAt(i);
                i++;
                
                out.append(xor_char+"\t");
                
            }
        }
        
        if(plain.read() == -1 && i < LENGTH - 1)
        {
            out.append(32^(int)key.charAt(i));
        }
        fout.write(out.toString());
        fout.flush();
        fout.close();
    }

    boolean containsZero(byte [] arr)
    {
        for( byte b : arr)
        {
            if(b == 0)
                return true;
        }
        return  false;
    }
    
    byte [] findkey() throws IOException
    {
        FileReader crypto_f = new FileReader("crypto.txt");
        BufferedReader crypto = new BufferedReader(crypto_f);
    
        byte [] key = new byte[LENGTH-1];
        
        Scanner sc = new Scanner(crypto);
        
        while(sc.hasNextLine())
        {
            String line = sc.nextLine();
            String[] currentLine;
            currentLine = line.split("\t");
    
            int[] intLine = new int[LENGTH-1];
    
            for (int i = 0; i < currentLine.length; i++)
            {
                intLine[i] = Integer.parseInt(currentLine[i]);
            }
            
            if (containsZero(key))
            {
                for (int i = 0; i < intLine.length - 2; i++)
                {
                    int ch_1 = intLine[i];
                    int ch_2 = intLine[i + 1];
                    int ch_3 = intLine[i + 2];
    
                    String xor_1 = String.format("%8s", Integer.toBinaryString(ch_1^ch_2)).replace(" ", "0");
                    String xor_2 = String.format("%8s", Integer.toBinaryString(ch_2^ch_3)).replace(" ", "0");
                    boolean if_1 = xor_1.startsWith("010");
                    boolean if_2 = xor_2.startsWith("010");
    
                    if(if_1 && if_2)
                    {
                        //ch_2 is space
                        key[i+1] = (byte) (ch_2^32);
                    }
                    else if(if_1)
                    {
                        //ch is space
                        key[i] = (byte)(ch_1^32);
        
                    }
                    else if(if_2)
                    {
                        //ch_3 is space
                        key[i+2] = (byte)(ch_3^32);
                    }
                    
                }
            }
        }
        
        return key;
       
    }
    
    void decrypt() throws IOException
    {
        File decrypt_f = new File("decrypt.txt");
        FileWriter decrypt = new FileWriter(decrypt_f);
    
        FileReader crypto_f = new FileReader("crypto.txt");
        BufferedReader crypto = new BufferedReader(crypto_f);
    
        Scanner sc = new Scanner(crypto);
    
        byte [] key = findkey(); //szukanie klucza
        
        while(sc.hasNextLine())
        {
            String line = sc.nextLine();
            String[] currentLine;
            currentLine = line.split("\t");
    
            int[] intLine = new int[LENGTH-1];
    
            for (int i = 0; i < currentLine.length; i++)
            {
                intLine[i] = Integer.parseInt(currentLine[i]);
            }
            
            for(int i = 0; i < intLine.length; i++)
            {
                int xor = intLine[i]^(int)key[i];
                decrypt.append((char)xor);
            }
            
            decrypt.append("\n");
            
        }
        decrypt.flush();
        decrypt.close();
        
    }

    public static void main(String[] args) throws IOException
    {
        xor start = new xor();
        
		if(args[0].equals("-p"))
	        start.prepare();
		else if(args[0].equals("-e"))
	    	start.encrypt();
		else if(args[0].equals("-k"))
        	start.decrypt();
		else
			System.out.println("Podane niepoprawne opcje");
    }
}
