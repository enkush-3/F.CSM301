package Lab_2;

import ip.group.Lab_2.BinarySearch;
import ip.group.Lab_2.MaxKeySearch;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchTest {

    @Test
    void searchTestNotFound() {
        int arr[] = {0, 2, 3, 4, 5, 7, 8, 9};
        int result = BinarySearch.binary(arr, 0, arr.length, 10);
        assertEquals(-1, result);
    }

    @Test
    void searchTestEmpty() {
        int arr[] = {};
        int result = BinarySearch.binary(arr, 0, arr.length, 5);
        assertEquals(-1, result);
    }

    @Test
    void searchTestFound() {
        int arr[] = {0, 2, 3, 4, 5, 7, 8, 9};
        int result = BinarySearch.binary(arr, 0, arr.length, 9);
        assertEquals(7, result);
    }

    @Test
    void searchTestFistValue() {
        int arr[] = {3, 3, 3, 3, 3, 3, 3, 3};
        int result = BinarySearch.binary(arr, 0, arr.length, 3);
        assertEquals(4, result);
    }
}
