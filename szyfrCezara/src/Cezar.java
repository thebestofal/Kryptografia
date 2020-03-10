import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class cezar
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
    static void odszyfrujCezar(String pathName) throws IOException
    {
        File klucz_f = new File(pathName);
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
    
    static void kryptoanalizaCezarTekstJawny() throws IOException
    {
        File tekstZaszyfrowany_f = new File("crypto.txt");
        FileReader fr = new FileReader(tekstZaszyfrowany_f);
        BufferedReader tekstZaszyfrowany = new BufferedReader(fr);
        
        
        File tekstPomocniczy_f = new File("extra.txt");
        FileReader fr1 = new FileReader(tekstPomocniczy_f);
        BufferedReader tekstPomocniczy = new BufferedReader(fr1);
        
        try
        {
            int crypto = tekstZaszyfrowany.read();
            int extra = tekstPomocniczy.read();
            int key = Math.abs(crypto - extra);
            System.out.println(key);
            
            File found_key_f = new File("key-found.txt");
            FileWriter found_key = new FileWriter(found_key_f);
            found_key.write(Integer.toString(key));
            found_key.flush();
            found_key.close();
            odszyfrujCezar("key-found.txt");
        }catch (Exception e){
            System.out.println("Nie podano tekstu pomocniczego w pliku extra.txt");
            e.printStackTrace();
        }
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
    
    static int gcd(int n1, int n2) {
        int gcd = 1;
        for (int i = 1; i <= n1 && i <= n2; i++) {
            if (n1 % i == 0 && n2 % i == 0) {
                gcd = i;
            }
        }
        return gcd;
    }
    static int odwrotneModulo(int a, int count)
    {
         if(gcd(a, 26)==1)
         {
             int temp = Math.floorMod((a * count), 26);
             if (temp == 1)
                 return count;
             else
             {
                 count++;
                 return odwrotneModulo(a, count);
             }
         }
         else return -1;
    }
    static void odszyfrujAfiniczny(String path) throws IOException
    {
        File klucz_f = new File(path);
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
        //System.out.println(klucz_a_inverse);
        
        
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
    
    /*static void kryptoanalizaAnificznyTekstJawny() throws FileNotFoundException
    {
        File tekstZaszyfrowany_f = new File("crypto.txt");
        FileReader fr = new FileReader(tekstZaszyfrowany_f);
        BufferedReader tekstZaszyfrowany = new BufferedReader(fr);
    
    
        File tekstPomocniczy_f = new File("extra.txt");
        FileReader fr1 = new FileReader(tekstPomocniczy_f);
        BufferedReader tekstPomocniczy = new BufferedReader(fr1);
    
        try
        {
            BigInteger crypto1 = new BigInteger(String.valueOf(tekstZaszyfrowany.read()));
            BigInteger crypto2 = new BigInteger(String.valueOf(tekstZaszyfrowany.read()));
            BigInteger mod = new BigInteger("26");
    
            BigInteger extra1 = new BigInteger(String.valueOf(tekstPomocniczy.read()));
            BigInteger extra2 = new BigInteger(String.valueOf(tekstPomocniczy.read()));

            int key_a;
            int key_b;
            
            //crypto1 = Alfa*extra1 + Beta
            //crypto2 = Alfa*extra2 + Beta
            
            BigInteger alfa_number = extra1.subtract(extra2);
            
            try
            {
                BigInteger alfa = (crypto1.subtract(crypto2)).modInverse(mod).multiply(alfa_number.modInverse(mod).mod(mod)).mod(mod);
                //int alfa = Math.floorMod(( odwrotneModulo((crypto1 - crypto2), 1) * odwrotneModulo(alfa_number, 1) ), 26);
                //int beta = Math.floorMod((crypto1 - 97 - alfa * (extra1 - 97)), 26);
    
                File found_key_f = new File("key-found.txt");
                FileWriter found_key = new FileWriter(found_key_f);
                found_key.write(String.valueOf(alfa));
                System.out.println("looool"+alfa);
                //found_key.write(Integer.toString(alfa));
                //found_key.write(" "+ beta);
                found_key.flush();
                found_key.close();
                odszyfrujAfiniczny("key-found.txt");
            }catch (Exception e){
                System.out.println("Niemożliwe znalezienie klucza");
                e.printStackTrace();
            }
            
        }catch (Exception e){
            System.out.println("Nie podano tekstu pomocniczego w pliku extra.txt");
//            e.printStackTrace();
        }
    }*/
    
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
        else if (args[0].equals("-c") && args[1].equals("-d"))
            odszyfrujCezar("key.txt");
        else if (args[0].equals("-c") && args[1].equals("-j"))
            kryptoanalizaCezarTekstJawny();
        else if (args[0].equals("-c") && args[1].equals("-k"))
            kryptoanalizaCezarKryptogram();
        else if (args[0].equals("-a") && args[1].equals("-e"))
            szyfrujAfiniczny();
        else if (args[0].equals("-a") && args[1].equals("-d"))
            odszyfrujAfiniczny("key.txt");
        else if (args[0].equals("-a") && args[1].equals("-k"))
            kryptoanalizaAnificznyKryptogram();
        //else if (args[0].equals("-a") && args[1].equals("-j"))
            //kryptoanalizaAnificznyTekstJawny();
        else
            System.out.println("Podane niepoprawne opcje");
    
    }
}
