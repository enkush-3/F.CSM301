package org.example;

import java.util.List;
import java.util.Scanner;

public class TextJustifyApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Хэлээ сонгоно уу:");
        System.out.println("1 - Монгол (Hyphenation)");
        System.out.println("2 - English (Hyphenation)");
        System.out.print("Сонголт: ");
        int langChoice = sc.nextInt();
        sc.nextLine();

        System.out.print("Мөрийн дээд урт (character): ");
        int maxWidth = sc.nextInt();
        sc.nextLine();

        String promptText;
        if (langChoice == 1) {
            promptText = "Бичвэрээ оруулна уу (монгол текст):";
        } else {
            promptText = "Enter your text (English):";
        }

        System.out.println(promptText);
        String text = sc.nextLine();

        String[] words = text.trim().split("\\s+");

        if (langChoice == 1) {
            Hyphenator mnHyphenator = new Hyphenator("hyph_mn_MN.xml", null, 2, 2);
            TextJustifier greedy = new GreedyJustifier(mnHyphenator);
            TextJustifier dp = new DPJustifier(mnHyphenator);

            List<String> greedyLines = greedy.justify(words, maxWidth);
            List<String> dpLines = dp.justify(words, maxWidth);

            System.out.println("\n Greedy (Монгол, hyphenation-тай) ");
            for (String line : greedyLines) {
                System.out.println("|" + line + "|");
            }

            System.out.println("\nDP (Монгол, hyphenation-тай)");
            for (String line : dpLines) {
                System.out.println("|" + line + "|");
            }

        } else {
            Hyphenator enHyphenator = new Hyphenator("hyph_en_US.xml", null, 2, 2);
            TextJustifier hyphenGreedy = new GreedyJustifier(enHyphenator);
            TextJustifier dpHyphen = new DPJustifier(enHyphenator);

            List<String> hyphenGreedyLines = hyphenGreedy.justify(words, maxWidth);
            List<String> dpHyphenLines = dpHyphen.justify(words, maxWidth);

            System.out.println("\nGreedy + English Hyphenation (FOP)");
            for (String line : hyphenGreedyLines) {
                System.out.println("|" + line + "|");
            }

            System.out.println("\nDP + English Hyphenation (FOP)");
            for (String line : dpHyphenLines) {
                System.out.println("|" + line + "|");
            }
        }

        sc.close();
    }
}