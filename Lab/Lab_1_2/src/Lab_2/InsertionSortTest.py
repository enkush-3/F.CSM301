import unittest

from src.Lab_2 import InsertionSort


class TestReadFile(unittest.TestCase):

    def test_sortInsertion(self):
        array = [1, 5, 2, 7, 3, 9, 4, 6, 8, 10]
        resultArray = InsertionSort.sort(array, 0, len(array) - 1)
        self.assertEqual([1, 2, 3, 4, 5, 6, 7, 8, 9, 10], resultArray)

    def test_sortInsertionEmpty(self):
        array = []
        resultArray = InsertionSort.sort(array, 0, len(array) - 1)
        self.assertEqual([], resultArray)

    def test_sortDuplicateValue(self):
        array = [3, 3, 3, 3, 3, 3, 3, 1, 3, 3]
        resultArray = InsertionSort.sort(array, 0, len(array) - 1)
        self.assertEqual([1, 3, 3, 3, 3, 3, 3, 3, 3, 3], resultArray)


if __name__ == "__main__":
    unittest.main()
