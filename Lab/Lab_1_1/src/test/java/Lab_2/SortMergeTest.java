package Lab_2;

import ip.group.Lab_2.MergeSort;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SortMergeTest {

    @Test
    public void sortMergeTest() {
        int[] arr = {2, 9, 4, 3, 5, 8, 7, 0};
        int[] result = MergeSort.sort(arr, 0, arr.length - 1);

        assertArrayEquals(new int[]{0, 2, 3, 4, 5, 7, 8, 9}, result);
        IntStream.range(arr.length, result.length).forEach(i -> assertTrue(result[i] < result[i + 1]));
    }

    @Test
    public void sortMergeEmpty() {
        int arr[] = {};
        int result[] = MergeSort.sort(arr, 0, arr.length - 1);

        assertArrayEquals(new int[]{}, result);
    }

    @Test
    public void sortMergeDuplicateValue() {
        int arr[] = {3, 3, 3, 3, 3, 1, 3, 3};
        int result[] = MergeSort.sort(arr, 0, arr.length - 1);

        assertEquals(new int[]{1, 3, 3, 3, 3, 3, 3, 3}, result);
        IntStream.range(arr.length, result.length).forEach(i -> assertTrue(result[i] <= result[i + 1]));
    }

    @Test
    public void mergeTest() {
        int arr[] = {3, 2, 5, 6, 1, 2, 4, 9};
        int start = 0;
        int end = arr.length - 1;
        int mid = start + (end - start) / 2;

        int result[] = MergeSort.merge(arr, start, mid, end);

        System.out.println(Arrays.toString(result));
        assertArrayEquals(new int[]{1, 2, 3, 2, 4, 5, 6, 9}, result);
    }

    @Test
    public void mergeEmptyTest() {
        int arr[] = {};
        int start = 0;
        int end = arr.length - 1;
        int mid = start + (end - start) / 2;

        int result[] = MergeSort.merge(arr, start, mid, end);

        System.out.println(Arrays.toString(result));
        assertArrayEquals(new int[]{}, result);
    }
}
