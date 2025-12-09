package org.example;

import java.util.ArrayList;
import java.util.List;

public class GreedyJustifier implements TextJustifier {

    private final Hyphenator hyphenator;

    public GreedyJustifier(Hyphenator hyphenator) {
        this.hyphenator = hyphenator;
    }

    @Override
    public List<String> justify(String[] words, int maxWidth) {

        List<String> wordList = new ArrayList<>(List.of(words));
        List<String> lines = new ArrayList<>();

        int i = 0;
        while (i < wordList.size()) {
            int n = wordList.size();

            // Check first word
            if (wordList.get(i).length() > maxWidth) {
                splitFirstWordIfTooLong(wordList, i, maxWidth);
            }

            int lineLen = wordList.get(i).length();
            int j = i + 1;

            while (j < n) {
                String w = wordList.get(j);
                int need = lineLen + 1 + w.length();

                if (need <= maxWidth) {
                    lineLen = need;
                    j++;
                } else {
                    int remaining = maxWidth - (lineLen + 1);
                    if (remaining > 0) {
                        String[] split = bestHyphenSplit(w, remaining);

                        if (split != null) {
                            String left = split[0];
                            String right = split[1];

                            wordList.set(j, left);
                            wordList.add(j + 1, right);

                            lineLen = lineLen + 1 + left.length();
                            j++;
                        }
                    }
                    break;
                }
            }

            boolean isLastLine = (j == wordList.size());
            String[] currentWords = wordList.toArray(new String[0]);
            lines.add(TextJustifier.buildLine(currentWords, i, j, maxWidth, isLastLine));
            i = j;
        }

        return lines;
    }

    private void splitFirstWordIfTooLong(List<String> wordList, int index, int maxWidth) {
        String w = wordList.get(index);
        String[] split = bestHyphenSplit(w, maxWidth);
        if (split == null) {
            // Fallback hard split if no hyphenation possible but word is too long
            if (w.length() > maxWidth) {
                String forcedLeft = w.substring(0, maxWidth - 1) + "-";
                String forcedRight = w.substring(maxWidth - 1);
                wordList.set(index, forcedLeft);
                wordList.add(index + 1, forcedRight);
            }
        } else {
            wordList.set(index, split[0]);
            wordList.add(index + 1, split[1]);
        }
    }

    private String[] bestHyphenSplit(String word, int remainingWidth) {
        Hyphenator.Hyphenation h = hyphenator.hyphenate(word);
        if (h == null)
            return null;

        String bestLeft = null;
        String bestRight = null;

        StringBuilder currentLeft = new StringBuilder();

        for (int k = 0; k < h.length() - 1; k++) {
            currentLeft.append(h.getPart(k));

            String candidateLeft = currentLeft.toString() + "-";
            if (candidateLeft.length() <= remainingWidth) {
                bestLeft = candidateLeft;
                StringBuilder rightSb = new StringBuilder();
                for (int m = k + 1; m < h.length(); m++) {
                    rightSb.append(h.getPart(m));
                }
                bestRight = rightSb.toString();
            } else {
                break;
            }
        }

        if (bestLeft != null) {
            return new String[] { bestLeft, bestRight };
        }
        return null;
    }
}