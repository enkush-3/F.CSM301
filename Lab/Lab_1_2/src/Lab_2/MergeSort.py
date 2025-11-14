from src.Lab_1.ReadFile import ReadFile


def merge(arr, start, mid, end):
    if len(arr) == 0:
        return arr

    lenArrL = mid - start + 1
    lenArrR = end - mid

    arrL = arr[start:mid + 1]
    arrR = arr[mid + 1:end + 1]

    i = 0
    j = 0
    k = start

    while i < lenArrL and j < lenArrR:
        if arrL[i] < arrR[j]:
            arr[k] = arrL[i]
            k = k + 1
            i = i + 1
        else:
            arr[k] = arrR[j]
            k = k + 1
            j = j + 1

    while i < lenArrL:
        arr[k] = arrL[i]
        k = k + 1
        i = i + 1
    while j < lenArrR:
        arr[k] = arrR[j]
        k = k + 1
        j = j + 1

    return arr


def sort(arr, start, end):
    if (start < end):
        mid = start + (end - start) // 2
        sort(arr, start, mid)
        sort(arr, mid + 1, end)
        merge(arr, start, mid, end)
    return arr


array = ReadFile.first_main()

print(array)
start = 0;
end = len(array) - 1
mid = start + (end - start) // 2
print("Sorted: ")
print(sort(array, start, end))
