package org.example;

import org.apache.fop.hyphenation.Hyphenation;
import org.apache.fop.hyphenation.Hyphenator;

public class HyphenExample {
    public static void main(String[] args) {
        // 3 аргумент: xml зам, minPrefix, minSuffix
        Hyphenator hyphenator = new Hyphenator("hyph_mn_MN.xml", null, 2, 2);

        String word = "Монголынх";
        Hyphenation h = hyphenator.hyphenate(word);

        if (h != null) {
            for (int i = 0; i < h.length(); i++) {
                System.out.print(h.getPart(i));
                if (i < h.length() - 1) System.out.print("-");
            }
        } else {
            System.out.println(word); // таслалгүй хэвлэх
        }
    }
}
