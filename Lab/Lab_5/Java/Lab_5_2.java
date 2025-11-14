import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;

public class Lab_5_2 {
    public static int MOD = 1337;

    public static int powMe(int a, int b)
    {
        int oddResult = 1;
        a %= MOD;

        while (b > 0) {
            if(b % 2 == 1)
                oddResult = (oddResult * a) % MOD;
            
            a = (a * a) % MOD;
            b /= 2;
        } 
        return oddResult;
    }
    public static int superPowMe(int a, Vector<Integer> b) {
        if(b.isEmpty())
            return 1;

        int last = b.lastElement();
        Vector<Integer> newVec = new Vector<>(b);
        newVec.remove(newVec.size() - 1);
        
        int lastPow = powMe(a, last);
        int leftPows = powMe(superPowMe(a, newVec), 10);

        return (lastPow * leftPows) % MOD;
    }

    public static int superPow(int a, int[] b) {
        Vector<Integer> myvec = new Vector<>(Arrays.stream(b).boxed().collect(Collectors.toList()));
        return superPowMe(a, myvec);
    }

    public static void main(String[] args) {

        int[] b = {1, 0};

        System.out.println(superPow(2, b));
    }
}
