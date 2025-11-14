def search(arr, start, end):
    if (len(arr) == 0):
        return -1
    if (start < end):
        mid = start + (end - start) // 2
        maxKeyL = search(arr, start, mid)
        maxKeyR = search(arr, mid + 1, end)

        return max_value(maxKeyR, maxKeyL)
    return arr[start]


def max_value(x, y):
    return x if x > y else y


array = [1, 2, 3, 4, 20, 6, 7, 8, 9, 10]

result = search(array, 0, len(array) - 1)
print(result)
