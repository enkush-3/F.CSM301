package ip.group.Lab_2;

import ip.group.Lab_1.ReadFile;

import java.util.Arrays;

import static ip.group.Lab_1.ReadFile.fistMain;

public class MergeSort {

    public static int[] sort(int[] arr, int start, int end) {
        if (start < end) {

            int mid = start + (end - start) / 2;
            sort(arr, start, mid);
            sort(arr, mid + 1, end);
            merge(arr, start, mid, end);
        }
        return arr;
    }

    public static int[] merge(int[] arr, int start, int mid, int end) {
        if (arr.length == 0)
            return arr;

        int arrL = mid - start + 1;
        int arrR = end - mid;
        int k = start;

        int[] arr1 = new int[arrL];
        int[] arr2 = new int[arrR];

        for (int i = 0; i < arrL; i++)
            arr1[i] = arr[start + i];
        for (int j = 0; j < arrR; j++)
            arr2[j] = arr[mid + 1 + j];

        int i = 0, j = 0;
        while (i < arrL && j < arrR)
            if (arr1[i] <= arr2[j])
                arr[k++] = arr1[i++];
            else
                arr[k++] = arr2[j++];

        while (i < arrL)
            arr[k++] = arr1[i++];
        while (j < arrR)
            arr[k++] = arr2[j++];

        return arr;
    }

    public static void main(String[] args) {
        //Scanner scan = new Scanner(System.in);
        //String strArr = scan.nextLine();

        ReadFile readFile = new ReadFile();

        int[] arrayInt = readFile.toNumberSplit(fistMain());
        System.out.println(Arrays.toString(sort(arrayInt, 0, arrayInt.length - 1)));
    }
}
