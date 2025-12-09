from greedy_justifier import GreedyJustifier
from dp_justifier import DPJustifier
from hyphenating_greedy_justifier import HyphenatingGreedyJustifier
from hyphenating_dp_justifier import HyphenatingDPJustifier


def read_input_text() -> str:
    print("Текстээ оруулна уу (хоосон мөр оруулбал дуусна):")
    lines = []
    while True:
        line = input()
        if line.strip() == "":
            break
        lines.append(line)
    return " ".join(lines)


def run_mode_mn_en():
    max_width = int(input("Мөрийн хамгийн их урт (тоо): "))
    text = read_input_text()

    words = text.split()
    if not words:
        print("Текст оруулаагүй байна!")
        return

    longest = max(len(w) for w in words)
    if max_width < longest:
        print(f"Алдаа: Мөрийн урт {max_width} нь хамгийн урт үгний уртаас {longest} бага байна!")
        return

    greedy = GreedyJustifier()
    dp = DPJustifier()

    print("\n--- Greedy (Монгол + English, hyphenation-гүй) ---")
    for line in greedy.justify(text, max_width):
        print(repr(line))

    print("\n--- DP (Монгол + English, hyphenation-гүй) ---")
    for line in dp.justify(text, max_width):
        print(repr(line))


def run_mode_en_hyphen():
    max_width = int(input("Line width (English + hyphenation): "))
    text = read_input_text()

    greedy = HyphenatingGreedyJustifier(lang="en_US")
    dp = HyphenatingDPJustifier(lang="en_US")

    try:
        print("\n--- Greedy (English + Word-like hyphenation) ---")
        for line in greedy.justify(text, max_width):
            print(repr(line))

        print("\n--- DP (English + Word-like hyphenation) ---")
        for line in dp.justify(text, max_width):
            print(repr(line))
    except ValueError as e:
        print("error", e)


def main():
    print("Сонголтоо сонгоно уу:")
    print("  1. Монгол + English (зөвхөн зайгаар, hyphenation-гүй)")
    print("  2. English + Hyphenation (Word/LibreOffice mode) + DP & Greedy")
    choice = input("Таны сонголт (1/2): ").strip()

    if choice == "2":
        run_mode_en_hyphen()
    else:
        run_mode_mn_en()


if __name__ == "__main__":
    main()
