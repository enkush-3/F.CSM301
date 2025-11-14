package ip.group.Lab_2;

import ip.group.Lab_1.ReadFile;

import java.util.Arrays;

import static ip.group.Lab_1.ReadFile.fistMain;

public class Insertion {

    public static int[] insertion(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int temp = arr[i];
            int j = i;

            while (j > 0 && temp < arr[j - 1]) {
                arr[j] = arr[j - 1];
                j--;
            }
            arr[j] = temp;
        }
        return arr;
    }


    public static void main(String[] args) {
        //Scanner scan = new Scanner(System.in);
        //String strArr = scan.nextLine();

        ReadFile readFile = new ReadFile();

        int[] arrInt = readFile.toNumberSplit(fistMain());

        System.out.println(Arrays.toString(insertion(arrInt)));
    }
}
