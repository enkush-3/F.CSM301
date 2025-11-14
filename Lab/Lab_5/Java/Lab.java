import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Number {
    int value;
    int deep;

    Number(int value, int deep) {
        this.value = value;
        this.deep = deep;
    }
}

public class Lab {
    public static int maxNumber(List<Number> myVec, int start, int end) {
        if (start >= end)
            return start;

        int mid = start + (end - start) / 2;

        int leftMax = maxNumber(myVec, start, mid);
        int rightMax = maxNumber(myVec, mid + 1, end);

        return myVec.get(leftMax).value < myVec.get(rightMax).value ? rightMax : leftMax;
    }

    public static List<Number> divide(List<Number> myVec, int start, int end, int deep) {
        if (start > end)
            return myVec;

        int maxIndex = maxNumber(myVec, start, end);
        myVec.get(maxIndex).deep = deep;

        divide(myVec, start, maxIndex - 1, deep + 1);
        divide(myVec, maxIndex + 1, end, deep + 1);

        return myVec;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int n = scan.nextInt();

        while (n-- > 0) {
            int n1 = scan.nextInt();

            List<Number> myVec = new ArrayList<>();

            for (int i = 0; i < n1; i++) {
                int val = scan.nextInt();
                myVec.add(new Number(val, 0));
            }

            List<Number> resultVec = divide(myVec, 0, myVec.size() - 1, 0);

            for (Number num : resultVec) {
                System.out.print(num.deep + " ");
            }
            System.out.println();
        }

        scan.close();
    }
}
