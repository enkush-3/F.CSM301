package Lab_2;

import ip.group.Lab_2.MaxKeySearch;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaxKeySearchTest {
    @Test
    void maxKeyTest() {
        int result = MaxKeySearch.maxKey(8, 9);
        assertEquals(9, result);
    }

    @Test
    void searchTestMaxKey() {
        int arr[] = {1, 2, 3};
        int result = MaxKeySearch.binaryMaxKey(arr, 0, arr.length - 1);
        System.out.println(result);
        assertEquals(3, result);
    }

    @Test
    void searchTestEmptyMaxKey() {
        int arr[] = {};
        int result = MaxKeySearch.binaryMaxKey(arr, 0, arr.length - 1);
        assertEquals(-1, result);
    }
}
