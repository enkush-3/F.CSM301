from src.Lab_1.ReadFile import ReadFile


def sort(arr):
    for i in arr:
        temp = i;
        j = arr.index(i)

        while (j > 0 and arr[j - 1] > temp):
            arr[j] = arr[j - 1]
            j = j - 1
        arr[j] = temp
    return arr


array = ReadFile.first_main()

print(array)
start = 0;
end = len(array) - 1
mid = start + (end - start) // 2
print("Sorted: ")
print(sort(array))
