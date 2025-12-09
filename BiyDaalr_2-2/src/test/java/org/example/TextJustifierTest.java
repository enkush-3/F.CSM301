package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextJustifierTest {

    private TextJustifier greedy;
    private TextJustifier dp;
    private TextJustifier hyphenGreedy;
    private TextJustifier hyphenDP;

    @BeforeEach
    void setUp() {
        // Use English for tests (or any valid config)
        // We reused GreedyJustifier/DPJustifier names for the hyphenating versions.
        // They REQUIRE a Hyphenator now.
        Hyphenator h = new Hyphenator("hyph_en_US.xml", null, 2, 2);

        greedy = new GreedyJustifier(h);
        dp = new DPJustifier(h);
        hyphenGreedy = new GreedyJustifier(h);
        hyphenDP = new DPJustifier(h);
    }

    private String[] words(String text) {
        return text.trim().split("\\s+");
    }

    private void assertWidths(List<String> lines, int maxWidth) {
        assertFalse(lines.isEmpty(), "Lines must not be empty");
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (i == lines.size() - 1) {
                assertTrue(line.length() <= maxWidth,
                        "Last line is longer than maxWidth: " + line.length());
            } else {
                assertEquals(maxWidth, line.length(),
                        "Non-last line must be exactly maxWidth wide");
            }
        }
    }

    private void assertNotAllBlank(List<String> lines) {
        boolean allBlank = lines.stream()
                .allMatch(s -> s.trim().isEmpty());
        assertFalse(allBlank, "All lines are blank – something is wrong");
    }

    @Test
    void simpleSentence_noWrap_allJustifiersSame() {
        String text = "Hello world!";
        int width = 30;

        var w = words(text);
        List<String> g = greedy.justify(w, width);
        List<String> d = dp.justify(w, width);
        List<String> hg = hyphenGreedy.justify(w, width);
        List<String> hd = hyphenDP.justify(w, width);

        assertEquals(1, g.size());
        assertEquals(g, d);
        assertEquals(g, hg);
        assertEquals(g, hd);
    }

    @Test
    void wrapping_withoutHyphenation_greedyAndDPShouldWrapSimilarly() {
        String text = "This is a simple test for wrapping without hyphenation.";
        int width = 20;

        var w = words(text);
        List<String> g = greedy.justify(w, width);
        List<String> d = dp.justify(w, width);

        assertWidths(g, width);
        assertWidths(d, width);
        assertNotAllBlank(g);
        assertNotAllBlank(d);

        assertEquals(g.size(), d.size(),
                "Greedy and DP should produce same number of lines for simple text");
    }

    @Test
    void longWord_requiresHyphenation_hyphenJustifiersInsertDash() {
        String text = "responsibility";
        int width = 8;

        var w = words(text);
        List<String> hg = hyphenGreedy.justify(w, width);
        List<String> hd = hyphenDP.justify(w, width);

        assertWidths(hg, width);
        assertWidths(hd, width);

        boolean hasHyphenGreedy = hg.stream().anyMatch(s -> s.contains("-"));
        boolean hasHyphenDP = hd.stream().anyMatch(s -> s.contains("-"));

        assertTrue(hasHyphenGreedy, "HyphenatingGreedyJustifier should use hyphen for long word");
        assertTrue(hasHyphenDP, "DPHyphenatingJustifier should use hyphen for long word");
    }

    @Test
    void verySmallWidth_stillNoLineExceedsWidth() {
        String text = "supercalifragilisticexpialidocious is a very long word";
        int width = 10;

        var w = words(text);

        List<String> hg = hyphenGreedy.justify(w, width);
        List<String> hd = hyphenDP.justify(w, width);

        assertWidths(hg, width);
        assertWidths(hd, width);
    }

    @Test
    void longParagraph_allImplementationsProduceNonEmptyLines() {
        String text = """
                like a bluebottle, and darted away again with a curving flight. It was the police patrol, snooping
                into people’s windows. The patrols did not matter, however. Only the Thought Police mattered.
                """;

        int width = 40;
        var w = words(text);

        List<String> g = greedy.justify(w, width);
        List<String> d = dp.justify(w, width);
        List<String> hg = hyphenGreedy.justify(w, width);
        List<String> hd = hyphenDP.justify(w, width);

        assertWidths(g, width);
        assertWidths(d, width);
        assertWidths(hg, width);
        assertWidths(hd, width);

        assertNotAllBlank(g);
        assertNotAllBlank(d);
        assertNotAllBlank(hg);
        assertNotAllBlank(hd);
    }

    @Test
    void emptyInput_returnsEmptyList() {
        String[] w = new String[0];
        int width = 20;

        assertTrue(greedy.justify(w, width).isEmpty());
        assertTrue(dp.justify(w, width).isEmpty());
        assertTrue(hyphenGreedy.justify(w, width).isEmpty());
        assertTrue(hyphenDP.justify(w, width).isEmpty());
    }

    @Test
    void singleVeryLongWord_doesNotCrash() {
        String text = "supercalifragilisticexpialidocious";
        String[] w = words(text);
        int width = 5;

        assertDoesNotThrow(() -> greedy.justify(w, width));
        assertDoesNotThrow(() -> dp.justify(w, width));
        assertDoesNotThrow(() -> hyphenGreedy.justify(w, width));
        assertDoesNotThrow(() -> hyphenDP.justify(w, width));
    }
}