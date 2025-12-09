package org.example;

import org.apache.fop.hyphenation.HyphenationTree;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Hyphenator {
    private final HyphenationTree hyphenationTree;
    private final int leftMin;
    private final int rightMin;

    public Hyphenator(String xmlParam, String unused, int leftMin, int rightMin) {
        this.leftMin = leftMin;
        this.rightMin = rightMin;
        this.hyphenationTree = new HyphenationTree();

        String[] prefixes = { "Dict-mn/", "Dict-en/", "" };
        InputStream is = null;

        for (String prefix : prefixes) {
            String path = prefix + xmlParam;
            is = getClass().getClassLoader().getResourceAsStream(path);
            if (is != null)
                break;
        }

        if (is != null) {
            try {
                hyphenationTree.loadPatterns(new InputSource(is));
            } catch (Exception e) {
                System.err.println("Error loading hyphenation patterns: " + e.getMessage());
            }
        } else {
            System.err.println("Hyphenation pattern file not found: " + xmlParam);
        }
    }

    public Hyphenation hyphenate(String word) {
        try {
            org.apache.fop.hyphenation.Hyphenation fopHyphenation = hyphenationTree.hyphenate(word, leftMin, rightMin);
            if (fopHyphenation == null) {
                return null;
            }

            int[] points = fopHyphenation.getHyphenationPoints();
            if (points == null || points.length == 0) {
                return null;
            }

            List<String> parts = new ArrayList<>();
            int start = 0;
            for (int point : points) {
                parts.add(word.substring(start, point));
                start = point;
            }
            parts.add(word.substring(start));

            return new Hyphenation(parts);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Inner class merged from Hyphenation.java
    public static class Hyphenation {
        private final List<String> parts;

        public Hyphenation(List<String> parts) {
            this.parts = parts;
        }

        public int length() {
            return parts.size();
        }

        public String getPart(int i) {
            return parts.get(i);
        }
    }
}
