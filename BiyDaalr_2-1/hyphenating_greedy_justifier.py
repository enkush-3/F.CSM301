from typing import List
from text_justifier import TextJustifier

try:
    import pyphen
except ImportError:
    pyphen = None

class HyphenatingGreedyJustifier(TextJustifier):
    def __init__(self, lang: str = "en_US"):
        if pyphen is None:
            print("pyphen not installed, hyphenation will be very limited.")
            self.dic = None
        else:
            self.dic = pyphen.Pyphen(lang=lang)

    def justify(self, text: str, max_width: int) -> List[str]:

        word_list: List[str] = text.split()
        lines: List[str] = []

        i = 0
        while i < len(word_list):
            n = len(word_list)

            if len(word_list[i]) > max_width:
                self._split_first_word_if_too_long(word_list, i, max_width)

            line_len = len(word_list[i])
            j = i + 1

            while j < n:
                w = word_list[j]
                need = line_len + 1 + len(w)

                if need <= max_width:
                    line_len = need
                    j += 1
                else:
                    remaining = max_width - (line_len + 1)
                    if remaining > 0:
                        split = self._best_hyphen_split(w, remaining)
                        if split is not None:
                            left, right = split
                            word_list[j] = left
                            word_list.insert(j + 1, right)

                            line_len = line_len + 1 + len(left)
                            j += 1
                    break

            is_last_line = (j == len(word_list))
            lines.append(self._build_line(word_list, i, j, max_width, is_last_line))
            i = j

        return lines

    def _split_first_word_if_too_long(self, word_list: List[str], index: int, max_width: int) -> None:

        w = word_list[index]

        split = self._best_hyphen_split(w, max_width)
        if split is None:

            if max_width > 1 and len(w) > max_width:
                forced_left = w[: max_width - 1] + "-"
                forced_right = w[max_width - 1 :]
                word_list[index] = forced_left
                word_list.insert(index + 1, forced_right)
        else:
            left, right = split
            word_list[index] = left
            word_list.insert(index + 1, right)

    def _best_hyphen_split(self, word: str, remaining_width: int):
        if remaining_width <= 1:
            return None
        if self.dic is None:
            return None

        inserted = self.dic.inserted(word)
        parts = inserted.split("-")

        if len(parts) == 1:
            return None

        best_left = None
        best_right = None

        current = parts[0]
        for p in parts[1:]:
            current += p
            left = current
            right = word[len(left) :]

            len_with_hyphen = len(left) + 1
            if len_with_hyphen <= remaining_width:
                if best_left is None or len(left) > len(best_left):
                    best_left = left
                    best_right = right

        if best_left is None:
            return None

        return best_left + "-", best_right

    def _build_line(
        self,
        words: List[str],
        start: int,
        end: int,
        max_width: int,
        is_last_line: bool,
    ) -> str:

        num_words = end - start
        line_words = words[start:end]

        if num_words == 1 or is_last_line:
            line = " ".join(line_words)
            return line.ljust(max_width)

        total_chars = sum(len(w) for w in line_words)
        spaces = max_width - total_chars
        gaps = num_words - 1

        space_each, extra = divmod(spaces, gaps)

        parts = []
        for idx, w in enumerate(line_words[:-1]):
            parts.append(w)
            # first extra gaps get +1 space
            spaces_here = space_each + (1 if idx < extra else 0)
            parts.append(" " * spaces_here)
        parts.append(line_words[-1])

        return "".join(parts)
