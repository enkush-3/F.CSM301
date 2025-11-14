import unittest

from src.Lab_2 import DivideSearch


class TestReadFile(unittest.TestCase):

    def test_search(self):
        array = [0, 2, 3, 4, 5, 7, 8, 9]
        result = DivideSearch.search(array, 0, len(array) - 1)
        self.assertEqual(9, result)

    def test_seearchEmpty(self):
        array = []
        result = DivideSearch.search(array, 0, len(array) - 1)
        self.assertEqual(-1, result)

    def test_searchDuplicate(self):
        array = [9, 9, 9, 9, 9, 9, 9, 9, 9]
        result = DivideSearch.search(array, 0, len(array) - 1)
        self.assertEqual(9, result)

    def test_maxKey(self):
        result = DivideSearch.max_value(1, 2)
        self.assertEqual(2, result)


if __name__ == "__main__":
    unittest.main()
