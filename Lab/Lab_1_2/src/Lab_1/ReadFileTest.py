import unittest

from src.Lab_1.ReadFile import ReadFile


class TestReadFile(unittest.TestCase):
    EMPTY_FILE = "/home/leym1ion/IdeaProjects/Lab_1_Python/src/main/resources/EmptyTest.txt"
    NON_EMPTY_FILE = "/home/leym1ion/IdeaProjects/Lab_1_Python/src/main/resources/TextTest.txt"
    ZIP_FILE = "/home/leym1ion/IdeaProjects/Lab_1_Python/src/main/resources/Test.zip"

    def test_is_file_empty_should_throw_exception_for_zip_file(self):
        with self.assertRaises(ValueError) as context:
            ReadFile.is_file_empty(self.ZIP_FILE)
        self.assertIn("Only txt File", str(context.exception))

    def test_file_exists_should_return_false_for_missing_file(self):
        self.assertFalse(ReadFile.file_exists("Baihgui_File.txt"))

    def test_is_file_empty_should_return_true_for_empty_test_file(self):
        self.assertTrue(ReadFile.is_file_empty(self.EMPTY_FILE))

    def test_is_file_empty_should_return_false_for_non_empty_test_file(self):
        self.assertFalse(ReadFile.is_file_empty(self.NON_EMPTY_FILE))


if __name__ == "__main__":
    unittest.main()
