from text_justifier import TextJustifier

class DPJustifier(TextJustifier):

    def justify(self, text: str, max_width: int) -> list[str]:
        words = self._split_words(text)
        n = len(words)
        lengths = [len(w) for w in words]
        INF = 10**18

        cost = [[INF] * n for _ in range(n)]
        for i in range(n):
            total = lengths[i]
            for j in range(i, n):
                if j > i:
                    total += 1 + lengths[j]
                if total > max_width:
                    break
                cost[i][j] = 0 if j == n - 1 else (max_width - total) ** 3

        dp = [INF] * (n + 1)
        next_break = [None] * (n + 1)
        dp[n] = 0

        for i in range(n - 1, -1, -1):
            for j in range(i, n):
                if cost[i][j] == INF:
                    break
                val = cost[i][j] + dp[j + 1]
                if val < dp[i]:
                    dp[i] = val
                    next_break[i] = j + 1

        res = []
        i = 0
        while i < n:
            j = next_break[i]
            line_words = words[i:j]

            if j == n or len(line_words) == 1:
                line = " ".join(line_words).ljust(max_width)
            else:
                total_chars = sum(len(w) for w in line_words)
                spaces = max_width - total_chars
                gaps = len(line_words) - 1
                space_each, extra = divmod(spaces, gaps)

                line_parts = []
                for k, w in enumerate(line_words[:-1]):
                    line_parts.append(w)
                    line_parts.append(" " * (space_each + (1 if k < extra else 0)))
                line_parts.append(line_words[-1])
                line = "".join(line_parts)

            res.append(line)
            i = j

        return res
