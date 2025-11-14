import unittest

from src.Lab_2 import MergeSort


class TestReadFile(unittest.TestCase):

    def test_sortMerge(self):
        array = [1, 5, 2, 7, 3, 9, 4, 6, 8, 10]
        resultArray = MergeSort.sort(array, 0, len(array) - 1)
        self.assertEqual([1, 2, 3, 4, 5, 6, 7, 8, 9, 10], resultArray)

    def test_sortMergeEmpty(self):
        array = []
        resultArray = MergeSort.sort(array, 0, len(array) - 1)
        self.assertEqual([], resultArray)

    def test_sortDuplicateValue(self):
        array = [3, 3, 3, 3, 3, 3, 3, 1, 3, 3]
        resultArray = MergeSort.sort(array, 0, len(array) - 1)
        self.assertEqual([1, 3, 3, 3, 3, 3, 3, 3, 3, 3], resultArray)

    def test_merge(self):
        array = [3, 2, 5, 6, 1, 2, 4, 9]
        start = 0
        end = len(array) - 1
        mid = start + (end - start) // 2

        resultArray = MergeSort.merge(array, start, mid, end)
        self.assertEqual([1, 2, 3, 2, 4, 5, 6, 9], resultArray)

    def test_mergeEmpty(self):
        array = []
        start = 0
        end = len(array) - 1
        mid = start + (end - start) // 2

        resultArray = MergeSort.merge(array, start, mid, end)
        self.assertEqual([], resultArray)


if __name__ == "__main__":
    unittest.main()
