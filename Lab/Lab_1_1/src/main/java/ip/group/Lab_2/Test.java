package ip.group.Lab_2;

import java.util.Arrays;

public class Test {
    public static int[] sort(int[] arr, int start, int end) {
        if (start < end) {
            int mid = start + (end - start) / 2;
            sort(arr, start, mid);
            sort(arr, mid + 1, end);
            arr = merge(arr, start, mid, end);
        }
        return arr;
    }

    public static int[] merge(int arr[], int start, int mid, int end) {
        int arrLlength = mid - start + 1;
        int arrRlength = end - mid;

        int[] arrL = new int[arrLlength];
        int[] arrR = new int[arrRlength];


        for (int i = 0; i < arrLlength; i++) {
            arrL[i] = arr[i + start];
        }
        for (int i = 0; i < arrRlength; i++) {
            arrR[i] = arr[mid + 1 + i];
        }

        int k = start;
        int i = 0;
        int j = 0;

        while (i < arrLlength && j < arrRlength) {
            if (arrL[i] < arrR[j]) {
                arr[k++] = arrL[i++];
            } else {
                arr[k++] = arrR[j++];
            }
        }

        while (i < arrLlength) {
            arr[k++] = arrL[i++];
        }
        while (j < arrRlength) {
            arr[k++] = arrR[j++];
        }
        return arr;
    }

    public static void main(String args[]) {
        int[] arr = {1, 6, 3, 9, 12, 5, 2, 9, 5, 5};

        System.out.println(Arrays.toString(sort(arr, 0, arr.length - 1)));


    }

}
