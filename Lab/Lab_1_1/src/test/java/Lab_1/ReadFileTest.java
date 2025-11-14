package Lab_1;

import ip.group.Lab_1.ReadFile;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ReadFileTest {

    @Test
    public void splitNumberTest() {
        String str = "1 2 3 4 5 6";

        int[] resultArr = ReadFile.toNumberSplit(str);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, resultArr);
    }

    @Test
    public void splitNumberNewLineSpace() {
        String str = "1 \n 2 \n 3   4 5 6";
        int[] resultArr = ReadFile.toNumberSplit(str);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, resultArr);
    }

    @Test
    public void splitNumberInvalidFormat() {
        String str = "1 2 3 a 4 5 6";

        assertThrows(NumberFormatException.class, () -> {
            ReadFile.toNumberSplit(str);
        });
    }


    File emptyFile = new File("src/main/resources/EmptyTest.txt");
    File nonEmptyFile = new File("src/main/resources/TextTest.txt");
    File zipFile = new File("src/main/resources/Test.zip");

    @Test
    void isFileEmptyShouldThrowExceptionForZipFile() {
        assertThrows(IllegalArgumentException.class, () -> {
            ReadFile.isFileEmpty(zipFile.getAbsolutePath());
        });
    }

    @Test
    void fileExistsShouldReturnFalseForMissingFile() {
        assertFalse(ReadFile.fileExists("Baihgui_File.txt"));
    }

    @Test
    void isFileEmptyShouldReturnTrueForEmptyTestFile() {
        assertTrue(ReadFile.isFileEmpty(emptyFile.getAbsolutePath()));
    }

    @Test
    void isFileEmptyShouldReturnFalseForNonEmptyTestFile() {
        assertFalse(ReadFile.isFileEmpty(nonEmptyFile.getAbsolutePath()));
    }
}
