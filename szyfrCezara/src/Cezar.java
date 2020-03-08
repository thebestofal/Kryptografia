import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class Cezar
{
    static void szyfrujCezar() throws IOException
    {
        File tekstZaszyfrowany_f = new File("crypto.txt");
        FileWriter tekstZaszyfrowany = new FileWriter(tekstZaszyfrowany_f);
        
        File klucz_f = new File("key.txt");
        Scanner reader = new Scanner(klucz_f);
        int klucz = reader.nextInt();//od 1 do 25!!!!!!!!!!!!
        if(klucz < 1 || klucz > 25)
        {
            System.out.println("błędny klucz");
            return;
        }
        File tekstJawny_f = new File("plain.txt");
        FileReader fr = new FileReader(tekstJawny_f);
        BufferedReader tekstJawny = new BufferedReader(fr);
    
        int c;
        while((c = tekstJawny.read()) != -1)
        {
            if  (c > 96 && c < 123)
            {
                c = Math.floorMod((c + klucz - 97),  26); // operator % w javie zwraca ujemne wartości
                tekstZaszyfrowany.write(c + 97);       // dla -1%26 = -1
            }
            else if (c > 64 && c < 97)
            {
                c = Math.floorMod((c + klucz - 65),  26);
                tekstZaszyfrowany.write(c + 65);
            }
            else
                tekstZaszyfrowany.write(c);
            tekstZaszyfrowany.flush();
        }
        tekstZaszyfrowany.close();
    }
    static void odszyfrujCezar() throws IOException
    {
        File klucz_f = new File("key.txt");
        Scanner reader = new Scanner(klucz_f);
        int klucz = reader.nextInt();//od 1 do 25!!!!!!!!!!!!
        if(klucz < 1 || klucz > 25)
        {
            System.out.println("błędny klucz");
            return;
        }
    
        File tekstOdszyfrowany_f = new File("decrypt.txt");
        FileWriter tekstOdszyfrowany = new FileWriter(tekstOdszyfrowany_f);
        
        File tekstZaszyfrowany_f = new File("crypto.txt");
        FileReader fr = new FileReader(tekstZaszyfrowany_f);
        BufferedReader tekstZaszyfrowany = new BufferedReader(fr);
        
        int c;
        while((c = tekstZaszyfrowany.read()) != -1)
        {
            if  (c > 96 && c < 123)
            {
                c = Math.floorMod((c - klucz - 97),  26);
                tekstOdszyfrowany.write(c + 97);
            }
            else if (c > 64 && c < 97)
            {
                c = Math.floorMod((c - klucz - 65),  26);
                tekstOdszyfrowany.write(c + 65);
            }
            else
                tekstOdszyfrowany.write(c);
            tekstOdszyfrowany.flush();
        }
        tekstOdszyfrowany.close();
    }
    
    static void kryptoanalizaCezarKryptogram() throws IOException
    {
        File tekstZaszyfrowany_f = new File("crypto.txt");
        FileReader fr = new FileReader(tekstZaszyfrowany_f);
        BufferedReader tekstZaszyfrowany = new BufferedReader(fr);
        tekstZaszyfrowany.mark(100000);
    
        File tekstOdszyfrowany_f = new File("decrypt.txt");
        FileWriter tekstOdszyfrowany = new FileWriter(tekstOdszyfrowany_f);
        for(int j = 1; j < 26; j++)
        {
            tekstOdszyfrowany.write("\n");//dodatkowy tekst?
            int c;
            while((c = tekstZaszyfrowany.read()) != -1)
            {
                if (c > 96 && c < 123)
                {
                    c = Math.floorMod((c + j - 97), 26);
                    tekstOdszyfrowany.write(c + 97);
                } else if (c > 64 && c < 97)
                {
                    c = Math.floorMod((c + j - 65), 26);
                    tekstOdszyfrowany.write(c + 65);
                } else
                    tekstOdszyfrowany.write(c);
                tekstOdszyfrowany.flush();
            }
            tekstOdszyfrowany.write("\n");
            tekstZaszyfrowany.reset();
        }
        tekstZaszyfrowany.close();
    }
    
    static void szyfrujAfiniczny() throws IOException
    {
        File klucz_f = new File("key.txt");
        Scanner reader = new Scanner(klucz_f);
        int klucz_a, klucz_b;
        if(reader.hasNextInt())
            klucz_a = reader.nextInt();// dwie liczby
        else
        {
            System.out.println("Nie podano kluczy w pliku key.txt!");
            return;
        }
        if(reader.hasNextInt())
            klucz_b = reader.nextInt();// dwie liczby
        else
        {
            System.out.println("Nie podano dwóch liczb w pliku key.txt!");
            return;
        }
        File tekstZaszyfrowany_f = new File("crypto.txt");
        FileWriter tekstZaszyfrowany = new FileWriter(tekstZaszyfrowany_f);
 
        File tekstJawny_f = new File("plain.txt");
        FileReader fr = new FileReader(tekstJawny_f);
        BufferedReader tekstJawny = new BufferedReader(fr);
    
        int c;
        while((c = tekstJawny.read()) != -1)
        {
            if  (c > 96 && c < 123)
            {
                c = Math.floorMod(klucz_a*(c - 97) + klucz_b,  26);
                tekstZaszyfrowany.write(c + 97);
            }
            else if (c > 64 && c < 97)
            {
                c = Math.floorMod(klucz_a*(c - 65) + klucz_b,  26);
                tekstZaszyfrowany.write(c + 65);
            }
            else
                tekstZaszyfrowany.write(c);
            tekstZaszyfrowany.flush();
        }
        tekstZaszyfrowany.close();
    }
    static int odwrotneModulo(int a, int count)
    {
        int temp = (a * count) % 26;
        if (temp == 1)
            return count;
        else
        {
            count++;
            return odwrotneModulo(a, count);
        }
    }
    static void odszyfrujAfiniczny() throws IOException
    {
        File klucz_f = new File("key.txt");
        Scanner reader = new Scanner(klucz_f);
       
        int klucz_a, klucz_b;
        if(reader.hasNextInt())
            klucz_a = reader.nextInt();// dwie liczby
        else
        {
            System.out.println("Nie podano kluczy w pliku key.txt!");
            return;
        }
        if(reader.hasNextInt())
            klucz_b = reader.nextInt();// dwie liczby
        else
        {
            System.out.println("Nie podano dwóch liczb w pliku key.txt!");
            return;
        }

        int klucz_a_inverse = odwrotneModulo(klucz_a, 1);
        System.out.println(klucz_a_inverse);
        
        
        File tekstOdszyfrowany_f = new File("decrypt.txt");
        FileWriter tekstOdszyfrowany = new FileWriter(tekstOdszyfrowany_f);
    
        File tekstZaszyfrowany_f = new File("crypto.txt");
        FileReader fr = new FileReader(tekstZaszyfrowany_f);
        BufferedReader tekstZaszyfrowany = new BufferedReader(fr);
    
        int c;
        while((c = tekstZaszyfrowany.read()) != -1)
        {
            if  (c > 96 && c < 123)
            {
                c = Math.floorMod(klucz_a_inverse*(c - 97 - klucz_b),  26);
                tekstOdszyfrowany.write(c + 97);
            }
            else if (c > 64 && c < 97)
            {
                c = Math.floorMod(klucz_a_inverse*(c - 65 - klucz_b),  26);
                tekstOdszyfrowany.write(c + 65);
            }
            else
                tekstOdszyfrowany.write(c);
            tekstOdszyfrowany.flush();
        }
        tekstOdszyfrowany.close();
    }
    
    static void kryptoanalizaAnificznyKryptogram() throws IOException
    {
        File tekstZaszyfrowany_f = new File("crypto.txt");
        FileReader fr = new FileReader(tekstZaszyfrowany_f);
        BufferedReader tekstZaszyfrowany = new BufferedReader(fr);
        tekstZaszyfrowany.mark(100000);
    
        File tekstOdszyfrowany_f = new File("decrypt.txt");
        FileWriter tekstOdszyfrowany = new FileWriter(tekstOdszyfrowany_f);
        int counter = 1;
        for(int klucz_a = 1; klucz_a < 26; klucz_a++)
        {
            if((klucz_a % 2==0) || (klucz_a % 13==0))
                continue;
            else
            {
                for (int klucz_b = 0; klucz_b < 26; klucz_b++)
                {
                    tekstOdszyfrowany.write(  "\n");
                    int c;
                    while ((c = tekstZaszyfrowany.read()) != -1)
                    {
                        if (c > 96 && c < 123)
                        {
                            c = Math.floorMod(klucz_a * (c - 97 - klucz_b), 26);
                            tekstOdszyfrowany.write(c + 97);
                        } else if (c > 64 && c < 97)
                        {
                            c = Math.floorMod(klucz_a * (c - 65 - klucz_b), 26);
                            tekstOdszyfrowany.write(c + 65);
                        } else
                            tekstOdszyfrowany.write(c);
                        tekstOdszyfrowany.flush();
                    }
                    tekstOdszyfrowany.write("\n");
                    tekstZaszyfrowany.reset();
                    counter++;
                }
            }
        }
        tekstZaszyfrowany.close();
    }
    
    public static void main(String[] args) throws IOException
    {
        if(args[0].equals("-c") && args[1].equals("-e"))
         szyfrujCezar();
        //odszyfrujCezar();
        //kryptoanalizaCezarKryptogram();
        
        //szyfrujAfiniczny();
        //odszyfrujAfiniczny();
        kryptoanalizaAnificznyKryptogram();
    }
}
