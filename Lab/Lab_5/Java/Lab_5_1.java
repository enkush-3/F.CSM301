import java.util.Scanner;

public class Lab_5_1{

    public static String longestNiceSubstring(String str) {
        
        if(str.isEmpty())
            return "";
        
        boolean lower[] = new boolean[26], upper[] = new boolean[26];

        for(char i : str.toCharArray()){
            if(i < 'a')
                upper[i - 'A'] = true;
            else
                lower[i - 'a'] = true;
        }

        for(int i=0; i<str.length(); i++)
        {
            int boolIndex = str.charAt(i) < 'a' ? str.charAt(i) - 'A' : str.charAt(i) - 'a';

            if(lower[boolIndex] != upper[boolIndex])
            {
                String left = longestNiceSubstring(str.substring(0,i));
                String right = longestNiceSubstring(str.substring(i + 1));

                return left.length() < right.length() ? right : left;
            }
        }
        return str;
    }

    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        
        String str = scan.nextLine();
        System.out.println(longestNiceSubstring(str));
    }
}