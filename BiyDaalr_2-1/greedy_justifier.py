from text_justifier import TextJustifier

class GreedyJustifier(TextJustifier):

    def justify(self, text: str, max_width: int) -> list[str]:
        words = self._split_words(text)
        res = []
        i = 0

        while i < len(words):
            line_len = len(words[i])
            j = i + 1

            while j < len(words) and line_len + 1 + len(words[j]) <= max_width:
                line_len += 1 + len(words[j])
                j += 1

            num_words = j - i

            if j == len(words) or num_words == 1:
                line = " ".join(words[i:j]).ljust(max_width)
            else:
                total_chars = sum(len(w) for w in words[i:j])
                spaces = max_width - total_chars
                gaps = num_words - 1
                space_each, extra = divmod(spaces, gaps)

                line_parts = []
                for k in range(i, j - 1):
                    line_parts.append(words[k])
                    line_parts.append(" " * (space_each + (1 if (k - i) < extra else 0)))

                line_parts.append(words[j - 1])
                line = "".join(line_parts)

            res.append(line)
            i = j

        return res