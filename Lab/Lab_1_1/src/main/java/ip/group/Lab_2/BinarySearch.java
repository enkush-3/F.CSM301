package ip.group.Lab_2;

public class BinarySearch {

    public static int binary(int arr[], int start, int end, int key) {
        if (start < end) {
            int mid = start + (end - start) / 2;
            if (arr[mid] == key)
                return mid;
            else if (arr[mid] > key)
                return binary(arr, start, mid, key);
            else
                return binary(arr, mid + 1, end, key);
        }
        return -1;
    }

    public static void main(String[] args) {

        BinarySearch binarySearch = new BinarySearch();

        int arr[] = {0, 2, 3, 4, 5, 7, 8, 9};

        int result = binarySearch.binary(arr, 0, arr.length, 0);
        System.out.println(result);
    }
}