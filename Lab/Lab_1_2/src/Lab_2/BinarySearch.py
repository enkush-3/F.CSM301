def search(arr, start, end, key):
    if (start < end):
        mid = start + (end - start) // 2

        if (key == arr[mid]):
            return mid
        elif key > arr[mid]:
            return search(arr, mid + 1, end, key)
        else:
            return search(arr, start, mid, key)
    return -1


array = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

result = search(array, 0, len(array) - 1, 9)
print(result)
