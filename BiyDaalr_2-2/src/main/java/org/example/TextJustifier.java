package org.example;

import java.util.List;

public interface TextJustifier {
    List<String> justify(String[] words, int maxWidth);

    // Merged from LineBuilder.java
    static String buildLine(String[] words, int from, int to, int maxWidth, boolean isLastLine) {
        int numWords = to - from;

        if (numWords == 1 || isLastLine) {
            StringBuilder sb = new StringBuilder();
            sb.append(words[from]);
            for (int i = from + 1; i < to; i++) {
                sb.append(' ');
                sb.append(words[i]);
            }
            while (sb.length() < maxWidth) {
                sb.append(' ');
            }
            return sb.toString();
        }

        int totalWordLen = 0;
        for (int i = from; i < to; i++) {
            totalWordLen += words[i].length();
        }

        int totalSpaces = maxWidth - totalWordLen;
        int gaps = numWords - 1;

        int baseSpaces = totalSpaces / gaps;
        int extraSpaces = totalSpaces % gaps;

        StringBuilder sb = new StringBuilder();

        for (int i = from; i < to; i++) {
            sb.append(words[i]);
            if (i == to - 1)
                break;

            int spacesToInsert = baseSpaces + (extraSpaces > 0 ? 1 : 0);
            if (extraSpaces > 0)
                extraSpaces--;

            for (int s = 0; s < spacesToInsert; s++) {
                sb.append(' ');
            }
        }

        return sb.toString();
    }
}