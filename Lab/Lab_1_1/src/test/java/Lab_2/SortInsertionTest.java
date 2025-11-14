package Lab_2;

import ip.group.Lab_2.Insertion;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SortInsertionTest {

    @Test
    public void sortInsertionTest() {
        int[] arr = {2, 9, 4, 3, 5, 8, 7, 0};
        int[] result = Insertion.insertion(arr);

        assertArrayEquals(new int[]{0, 2, 3, 4, 5, 7, 8, 9}, result);
        IntStream.range(arr.length, result.length).forEach(i -> assertTrue(result[i] < result[i + 1]));
    }

    @Test
    public void sortInsertionEmpty() {
        int arr[] = {};
        int result[] = Insertion.insertion(arr);

        assertArrayEquals(new int[]{}, result);
    }

    @Test
    public void sortInsertionDuplicateValue() {
        int arr[] = {3, 3, 3, 3, 3, 1, 3, 3};
        int result[] = Insertion.insertion(arr);

        assertEquals("[1, 3, 3, 3, 3, 3, 3, 3]", Arrays.toString(result));
        IntStream.range(arr.length, result.length).forEach(i -> assertTrue(result[i] <= result[i + 1]));
    }

}
