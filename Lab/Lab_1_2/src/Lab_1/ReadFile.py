import os
from pathlib import Path


class ReadFile:

    @staticmethod
    def file_exists(file_path: str) -> bool:
        path = Path(file_path)
        return path.exists() and path.is_file()

    @staticmethod
    def is_file_empty(file_path: str) -> bool:
        if not file_path.endswith(".txt"):
            raise ValueError(f"Only Txt File: {file_path}")
        return os.path.getsize(file_path) == 0

    @staticmethod
    def read_whole_file(file_path: str) -> str:
        with open(file_path, "r", encoding="utf-8") as f:
            return f.read()

    @staticmethod
    def print_file_content(file_path: str):
        if not ReadFile.file_exists(file_path):
            print(f"Can't Find File: {file_path}")
            return

        try:
            if ReadFile.is_file_empty(file_path):
                print(f"Empty File: {file_path}")
                return

            content = ReadFile.read_whole_file(file_path)
            return content.split(" ")  # list of strings

        except Exception as e:
            print(f"Can't Read File: {e}")

    @staticmethod
    def first_main():
        file_path = input("Insert File Path: ")
        str_list = ReadFile.print_file_content(file_path)
        int_list = [int(x) for x in str_list]
        return int_list


if __name__ == "__main__":
    print(ReadFile.first_main())
