from __future__ import annotations
from dataclasses import dataclass
from functools import lru_cache
from typing import List, Optional, Tuple

from text_justifier import TextJustifier

try:
    import pyphen
except ImportError:
    pyphen = None


@dataclass(frozen=True)
class State:
    index: int
    carry: Optional[str]


class HyphenatingDPJustifier(TextJustifier):

    def __init__(self, lang: str = "en_US",
                 hyphen_penalty: float = 1.0):
        if pyphen is None:
            print("⚠ pyphen суулгаагүй байна, hyphenation боломж хязгаартай болно.")
            self.dic = None
        else:
            self.dic = pyphen.Pyphen(lang=lang)
        self.hyphen_penalty = hyphen_penalty

    def justify(self, text: str, max_width: int) -> List[str]:
        words = text.split()
        if not words:
            return []

        @lru_cache(maxsize=None)
        def solve(i: int, carry: Optional[str]) -> Tuple[float, List[str]]:
            if i == len(words) and (carry is None or carry == ""):
                return 0.0, []

            if carry is not None and len(carry) > max_width:
                first = carry[:max_width]
                rest = carry[max_width:]
                cost_rest, lines_rest = solve(i, rest if rest else None)
                cost = cost_rest + 10000.0
                line = self._format_line([first], max_width, False)
                return cost, [line] + lines_rest

            best_cost = float("inf")
            best_lines: Optional[List[str]] = None

            line_tokens: List[str] = []
            token_sources: List[Optional[int]] = []

            line_len = 0
            used_carry = False
            next_word_idx = i

            while True:
                if not used_carry and carry:
                    token_text = carry
                    src_idx: Optional[int] = None
                    used_carry = True
                else:
                    if next_word_idx >= len(words):
                        break
                    token_text = words[next_word_idx]
                    src_idx = next_word_idx
                    next_word_idx += 1

                token_len = len(token_text)
                new_len = token_len if not line_tokens else line_len + 1 + token_len
                if new_len > max_width:
                    break

                line_tokens.append(token_text)
                token_sources.append(src_idx)
                line_len = new_len

                consumed_words = sum(1 for s in token_sources if s is not None)
                next_i = i + consumed_words
                next_carry = None

                is_last_line = (next_i == len(words) and next_carry is None)
                line_cost = 0.0 if is_last_line else self._line_cost(line_tokens, max_width)

                rest_cost, rest_lines = solve(next_i, next_carry)
                total_cost = line_cost + rest_cost

                if total_cost < best_cost:
                    best_cost = total_cost
                    line = self._format_line(line_tokens, max_width, is_last_line)
                    best_lines = [line] + rest_lines

                last_src = token_sources[-1]
                last_token = line_tokens[-1]
                if last_src is not None and self.dic is not None:
                    for left, right in self._hyphen_splits(last_token):
                        left_with_hyphen = left + "-"
                        new_line_len = line_len - len(last_token) + len(left_with_hyphen)
                        if new_line_len > max_width:
                            continue

                        hyph_line_tokens = list(line_tokens)
                        hyph_line_tokens[-1] = left_with_hyphen

                        consumed_words_h = sum(1 for s in token_sources if s is not None)
                        next_i_h = i + consumed_words_h
                        next_carry_h = right

                        is_last_line_h = (next_i_h == len(words) and not next_carry_h)
                        hyph_line_cost = 0.0 if is_last_line_h else (
                            self._line_cost(hyph_line_tokens, max_width) + self.hyphen_penalty
                        )

                        rest_cost_h, rest_lines_h = solve(next_i_h, next_carry_h)
                        total_cost_h = hyph_line_cost + rest_cost_h

                        if total_cost_h < best_cost:
                            best_cost = total_cost_h
                            line_h = self._format_line(hyph_line_tokens, max_width, is_last_line_h)
                            best_lines = [line_h] + rest_lines_h

            if not line_tokens:
                if carry is None and i < len(words) and self.dic is not None:
                    w = words[i]
                    for left, right in self._hyphen_splits(w):
                        left_with_hyphen = left + "-"
                        if len(left_with_hyphen) > max_width:
                            continue

                        line_tokens_h = [left_with_hyphen]
                        next_i_h = i + 1
                        next_carry_h = right
                        is_last_h = (next_i_h == len(words) and not next_carry_h)

                        line_cost_h = 0.0 if is_last_h else (
                            self._line_cost(line_tokens_h, max_width) + self.hyphen_penalty
                        )
                        rest_cost_h, rest_lines_h = solve(next_i_h, next_carry_h)
                        total_cost_h = line_cost_h + rest_cost_h

                        if total_cost_h < best_cost:
                            best_cost = total_cost_h
                            line_h = self._format_line(line_tokens_h, max_width, is_last_h)
                            best_lines = [line_h] + rest_lines_h


                if best_lines is None:
                    token_text = carry if carry is not None else words[i]
                    forced = token_text[:max_width]
                    rest = token_text[max_width:] or None
                    next_i_f = i if carry is not None else i + 1
                    is_last_f = (next_i_f == len(words) and not rest)

                    line_f = self._format_line([forced], max_width, is_last_f)
                    if is_last_f:
                        best_cost = 0.0
                        best_lines = [line_f]
                    else:
                        rest_cost_f, rest_lines_f = solve(next_i_f, rest)
                        best_cost = rest_cost_f + 10000.0
                        best_lines = [line_f] + rest_lines_f

            if best_lines is None:

                remaining_parts = []
                if carry:
                    remaining_parts.append(carry)
                if i < len(words):
                    remaining_parts.extend(words[i:])
                text_rem = " ".join(remaining_parts).strip()

                raw_lines: List[str] = []
                while text_rem:
                    chunk = text_rem[:max_width]
                    raw_lines.append(chunk)
                    text_rem = text_rem[max_width:].lstrip()

                lines_final = [
                    self._format_line([ln], max_width, False) for ln in raw_lines
                ]
                return 20000.0, lines_final

            return best_cost, best_lines

        _, lines = solve(0, None)
        return lines

    def _hyphen_splits(self, word: str) -> List[Tuple[str, str]]:

        if self.dic is None:
            return []

        inserted = self.dic.inserted(word)
        parts = inserted.split("-")
        if len(parts) == 1:
            return []

        splits: List[Tuple[str, str]] = []
        current = parts[0]
        for p in parts[1:]:
            current += p
            left = current
            right = word[len(left):]
            if len(left) >= 2 and len(right) >= 2:
                splits.append((left, right))
        return splits

    @staticmethod
    def _line_cost(tokens: List[str], max_width: int) -> float:
        if not tokens:
            return 0.0
        total_chars = sum(len(t) for t in tokens)
        gaps = max(0, len(tokens) - 1)
        extra = max_width - total_chars - gaps
        if extra < 0:
            return float("inf")
        return float(extra) ** 3

    @staticmethod
    def _format_line(tokens: List[str], max_width: int, is_last: bool) -> str:
        if not tokens:
            return " " * max_width

        if len(tokens) == 1 or is_last:
            s = " ".join(tokens)
            return s.ljust(max_width)[:max_width]

        total_chars = sum(len(t) for t in tokens)
        gaps = len(tokens) - 1
        total_spaces = max_width - total_chars

        if total_spaces < gaps:
            s = " ".join(tokens)
            return s[:max_width]

        base = total_spaces // gaps
        extra = total_spaces % gaps

        parts: List[str] = []
        for i, w in enumerate(tokens):
            parts.append(w)
            if i == len(tokens) - 1:
                break
            spaces_here = base + (1 if extra > 0 else 0)
            if extra > 0:
                extra -= 1
            parts.append(" " * spaces_here)

        return "".join(parts)