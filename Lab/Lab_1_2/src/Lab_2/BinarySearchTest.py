import unittest

from src.Lab_2 import BinarySearch


class TestReadFile(unittest.TestCase):

    def test_search(self):
        array = [0, 2, 3, 4, 5, 7, 8, 9]
        result = BinarySearch.search(array, 0, len(array) - 1, 5)
        self.assertEqual(4, result)

    def test_seearchEmpty(self):
        array = []
        result = BinarySearch.search(array, 0, len(array) - 1, 5)
        self.assertEqual(-1, result)

    def test_sortNotFound(self):
        array = [0, 2, 3, 4, 5, 7, 8, 9]
        result = BinarySearch.search(array, 0, len(array) - 1, 20)
        self.assertEqual(-1, result)

    def test_sortDuplicateValue(self):
        array = [3, 3, 3, 3, 3, 3, 3, 3]
        result = BinarySearch.search(array, 0, len(array) - 1, 3)
        self.assertEqual(3, result)


if __name__ == "__main__":
    unittest.main()
