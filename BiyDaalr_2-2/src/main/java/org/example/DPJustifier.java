package org.example;

import java.util.*;

public class DPJustifier implements TextJustifier {

    private final Hyphenator hyphenator;
    private final Map<String, List<String[]>> hyphenCache = new HashMap<>();
    private static final double HYPHEN_PENALTY = 1.0;
    private static final double OVERFLOW_PENALTY = 10000.0;
    private static final double FALLBACK_PENALTY = 20000.0;

    public DPJustifier(Hyphenator hyphenator) {
        this.hyphenator = hyphenator;
    }

    private static class State {
        final int index;
        final String carry;

        State(int index, String carry) {
            this.index = index;
            this.carry = carry;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof State))
                return false;
            State s = (State) o;
            return index == s.index && Objects.equals(carry, s.carry);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, carry);
        }
    }

    private static class Result {
        final double cost;
        final List<String> lines;

        Result(double cost, List<String> lines) {
            this.cost = cost;
            this.lines = lines;
        }
    }

    @Override
    public List<String> justify(String[] words, int maxWidth) {
        return solve(words, maxWidth, 0, null, new HashMap<>()).lines;
    }

    private Result solve(String[] words, int maxWidth, int i, String carry, Map<State, Result> memo) {
        State state = new State(i, carry);
        if (memo.containsKey(state))
            return memo.get(state);

        // Base case: хоосон
        if (i >= words.length && carry == null) {
            return memoize(state, memo, new Result(0.0, new ArrayList<>()));
        }

        // Carry хэт урт бол хэсэглэх
        if (carry != null && carry.length() > maxWidth) {
            return memoize(state, memo, handleOverflowCarry(words, maxWidth, i, carry, memo));
        }

        // Хамгийн сайн шийдлийг олох
        Result best = findBestLayout(words, maxWidth, i, carry, memo);

        // Хэрвээ шийдэл олдоогүй бол fallback
        if (best == null) {
            best = createFallback(words, maxWidth, i, carry);
        }

        return memoize(state, memo, best);
    }

    private Result handleOverflowCarry(String[] words, int maxWidth, int i, String carry, Map<State, Result> memo) {
        List<String> lines = new ArrayList<>();
        String remaining = carry;

        while (remaining.length() > maxWidth) {
            lines.add(remaining.substring(0, maxWidth));
            remaining = remaining.substring(maxWidth);
        }

        Result rest = solve(words, maxWidth, i, remaining, memo);
        lines.addAll(rest.lines);
        return new Result(OVERFLOW_PENALTY + rest.cost, lines);
    }

    private Result findBestLayout(String[] words, int maxWidth, int i, String carry, Map<State, Result> memo) {
        double bestCost = Double.POSITIVE_INFINITY;
        List<String> bestLines = null;

        List<String> lineTokens = new ArrayList<>();
        List<Integer> tokenIndices = new ArrayList<>();
        int lineLen = 0;
        int nextWord = i;
        boolean firstToken = false;

        while (true) {
            // Дараагийн токен авах
            TokenInfo token = getNextToken(words, carry, nextWord, firstToken, i);
            if (token == null)
                break;
            firstToken = true;

            // Мөрөнд багтах эсэхийг шалгах
            int newLen = lineTokens.isEmpty() ? token.text.length() : lineLen + 1 + token.text.length();
            if (newLen > maxWidth)
                break;

            lineTokens.add(token.text);
            tokenIndices.add(token.wordIndex);
            lineLen = newLen;
            if (token.wordIndex >= 0)
                nextWord++;

            // Энэ мөрний cost-ийг тооцоолох
            Result candidate = evaluateLine(words, maxWidth, i, lineTokens, tokenIndices, false, memo);
            if (candidate.cost < bestCost) {
                bestCost = candidate.cost;
                bestLines = candidate.lines;
            }

            // Hyphenation оролдох
            if (token.wordIndex >= 0) {
                Result hyphenated = tryHyphenation(words, maxWidth, i, lineTokens, tokenIndices,
                        lineLen, token.text, memo);
                if (hyphenated != null && hyphenated.cost < bestCost) {
                    bestCost = hyphenated.cost;
                    bestLines = hyphenated.lines;
                }
            }
        }

        // Хэрвээ мөр хоосон бол дан үг/carry-г hyphenate хийх оролдлого
        if (lineTokens.isEmpty()) {
            Result single = handleSingleToken(words, maxWidth, i, carry, memo);
            if (single != null && (bestLines == null || single.cost < bestCost)) {
                return single;
            }
        }

        return bestLines != null ? new Result(bestCost, bestLines) : null;
    }

    private static class TokenInfo {
        String text;
        int wordIndex;

        TokenInfo(String text, int wordIndex) {
            this.text = text;
            this.wordIndex = wordIndex;
        }
    }

    private TokenInfo getNextToken(String[] words, String carry, int nextWord, boolean firstToken, int i) {
        if (!firstToken && carry != null) {
            return new TokenInfo(carry, -1);
        }
        if (nextWord >= words.length)
            return null;
        return new TokenInfo(words[nextWord], nextWord);
    }

    private Result evaluateLine(String[] words, int maxWidth, int i, List<String> tokens,
            List<Integer> indices, boolean isHyphen, Map<State, Result> memo) {
        int consumed = countConsumed(indices);
        int nextI = i + consumed;
        boolean isLast = (nextI >= words.length);

        double lineCost = isLast ? 0.0 : computeLineCost(tokens, maxWidth);
        if (isHyphen)
            lineCost += HYPHEN_PENALTY;

        Result rest = solve(words, maxWidth, nextI, null, memo);
        List<String> lines = new ArrayList<>();
        lines.add(TextJustifier.buildLine(tokens.toArray(new String[0]), 0, tokens.size(), maxWidth, isLast));
        lines.addAll(rest.lines);

        return new Result(lineCost + rest.cost, lines);
    }

    private Result tryHyphenation(String[] words, int maxWidth, int i, List<String> baseTokens,
            List<Integer> baseIndices, int baseLen, String word,
            Map<State, Result> memo) {
        Result best = null;

        for (String[] split : getHyphenSplits(word)) {
            String left = split[0] + "-";
            String right = split[1];

            int hyphLen = baseLen - word.length() + left.length();
            if (hyphLen > maxWidth)
                continue;

            List<String> hyphTokens = new ArrayList<>(baseTokens);
            hyphTokens.set(hyphTokens.size() - 1, left);

            int consumed = countConsumed(baseIndices);
            int nextI = i + consumed;
            boolean isLast = (nextI >= words.length && right.isEmpty());

            double lineCost = isLast ? 0.0 : computeLineCost(hyphTokens, maxWidth) + HYPHEN_PENALTY;
            Result rest = solve(words, maxWidth, nextI, right, memo);

            List<String> lines = new ArrayList<>();
            lines.add(
                    TextJustifier.buildLine(hyphTokens.toArray(new String[0]), 0, hyphTokens.size(), maxWidth, isLast));
            lines.addAll(rest.lines);

            Result candidate = new Result(lineCost + rest.cost, lines);
            if (best == null || candidate.cost < best.cost) {
                best = candidate;
            }
        }

        return best;
    }

    private Result handleSingleToken(String[] words, int maxWidth, int i, String carry, Map<State, Result> memo) {
        String text = carry != null ? carry : (i < words.length ? words[i] : null);
        if (text == null)
            return new Result(0.0, new ArrayList<>());

        boolean isCarry = (carry != null);
        Result best = null;

        // Hyphenation оролдох
        if (!isCarry) {
            for (String[] split : getHyphenSplits(text)) {
                String left = split[0] + "-";
                if (left.length() > maxWidth)
                    continue;

                Result rest = solve(words, maxWidth, i + 1, split[1], memo);
                List<String> lines = new ArrayList<>();
                lines.add(TextJustifier.buildLine(new String[] { left }, 0, 1, maxWidth, false));
                lines.addAll(rest.lines);

                Result candidate = new Result(computeLineCost(List.of(left), maxWidth) + HYPHEN_PENALTY + rest.cost,
                        lines);
                if (best == null || candidate.cost < best.cost) {
                    best = candidate;
                }
            }
        }

        // Албадан таслах
        if (best == null) {
            String chunk = text.substring(0, Math.min(text.length(), maxWidth));
            String nextCarry = chunk.length() < text.length() ? text.substring(chunk.length()) : null;
            int nextI = isCarry ? i : i + 1;

            Result rest = solve(words, maxWidth, nextI, nextCarry, memo);
            List<String> lines = new ArrayList<>();
            lines.add(TextJustifier.buildLine(new String[] { chunk }, 0, 1, maxWidth,
                    nextCarry == null && nextI >= words.length));
            lines.addAll(rest.lines);

            best = new Result(OVERFLOW_PENALTY + rest.cost, lines);
        }

        return best;
    }

    private Result createFallback(String[] words, int maxWidth, int i, String carry) {
        List<String> lines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (carry != null)
            sb.append(carry).append(' ');
        for (int k = i; k < words.length; k++) {
            if (sb.length() > 0)
                sb.append(' ');
            sb.append(words[k]);
        }

        String text = sb.toString().trim();
        while (text.length() > 0) {
            int len = Math.min(text.length(), maxWidth);
            lines.add(TextJustifier.buildLine(new String[] { text.substring(0, len) }, 0, 1, maxWidth, false));
            text = text.substring(len).trim();
        }

        return new Result(FALLBACK_PENALTY, lines);
    }

    private int countConsumed(List<Integer> indices) {
        return (int) indices.stream().filter(idx -> idx != null && idx >= 0).count();
    }

    private double computeLineCost(List<String> tokens, int maxWidth) {
        if (tokens.isEmpty())
            return 0.0;
        int totalLen = tokens.stream().mapToInt(String::length).sum();
        int gaps = Math.max(0, tokens.size() - 1);
        int extra = maxWidth - totalLen - gaps;
        return extra < 0 ? Double.POSITIVE_INFINITY : Math.pow(extra, 3);
    }

    private List<String[]> getHyphenSplits(String word) {
        return hyphenCache.computeIfAbsent(word, w -> {
            List<String[]> splits = new ArrayList<>();
            Hyphenator.Hyphenation h = hyphenator.hyphenate(w);
            if (h == null || h.length() < 2)
                return splits;

            StringBuilder left = new StringBuilder();
            for (int k = 0; k < h.length() - 1; k++) {
                left.append(h.getPart(k));
                StringBuilder right = new StringBuilder();
                for (int m = k + 1; m < h.length(); m++) {
                    right.append(h.getPart(m));
                }
                splits.add(new String[] { left.toString(), right.toString() });
            }
            return splits;
        });
    }

    private Result memoize(State state, Map<State, Result> memo, Result result) {
        memo.put(state, result);
        return result;
    }
}