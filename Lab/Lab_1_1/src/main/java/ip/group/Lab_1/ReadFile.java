package ip.group.Lab_1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class ReadFile {

    public static boolean fileExists(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path);
    }

    public static boolean isFileEmpty(String filePath) {
        if (!filePath.endsWith(".txt")) {
            throw new IllegalArgumentException("Only Txt File: " + filePath);
        }

        try {
            return Files.size(Paths.get(filePath)) == 0;
        } catch (IOException e) {
            throw new RuntimeException("Can't Read File Size: " + e.getMessage(), e);
        }
    }

    public static String readWholeFile(String filePath) throws IOException {
        return Files.readString(Paths.get(filePath));
    }

    public static String printFileContent(String filePath) {
        if (!fileExists(filePath)) {
            System.out.println("Can't Find File: " + filePath);
            return null;
        }

        if (isFileEmpty(filePath)) {
            System.out.println("Empty File: " + filePath);
            return null;
        }

        try {
            String content = readWholeFile(filePath);
            return content;
        } catch (IOException e) {
            System.out.println("Read File Error: " + e.getMessage());
        }
        return null;
    }

    public static int[] toNumberSplit(String str) {
        return Arrays.stream(str.trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
    }

    public static String fistMain() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Insert File Path:");
        String filePath = scan.nextLine();

        String str = printFileContent(filePath);
        return str;
    }


    public static void main(String[] args) {
        ReadFile readFile = new ReadFile();
        System.out.println(Arrays.toString(toNumberSplit(fistMain())));
    }
}
