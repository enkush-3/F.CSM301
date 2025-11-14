package ip.group.Lab_2;

import java.util.Arrays;
import java.util.Scanner;

public class MaxKeySearch {

    public static int binaryMaxKey(int arr[], int start, int end) {
        if (arr.length == 0)
            return -1;
        if (start < end) {
            int mid = start + (end - start) / 2;

            int maxkeyL = binaryMaxKey(arr, start, mid);
            int maxkeyR = binaryMaxKey(arr, mid + 1, end);

            return maxKey(maxkeyL, maxkeyR);
        }
        return arr[start];
    }

    public static int maxKey(int i, int j) {
        return (i < j) ? j : i;
    }

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        String str = scan.nextLine().trim();
        int[] strInt = Arrays.stream(str.split(" ")).mapToInt(Integer::parseInt).toArray();

        int result = binaryMaxKey(strInt, 0, strInt.length - 1);
        System.out.println(result);
    }
}
