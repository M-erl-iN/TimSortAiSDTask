package SemesterAssignments._1_Timsort;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class TimSort {
    static int RUN;
    public static void getMinRun(int N) {
        int r = 0; // r = 1 если среди отброшенных будет ненулевой бит
        while (N >= 64) {
            r |= N & 1; //побитовое или
            N >>= 1; //сдвигаем биты на 1 (делим на 2 и отбрасываем дробную часть)
        } RUN = N + r;
    }

    public static void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int temp = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > temp && j >= left) {
                arr[j + 1] = arr[j];
                j--;
            } arr[j + 1] = temp;
        }
    }

    // слияние соседних run
    public static void merge(int[] arr, int left, int mid, int right) {
        int leftArrLen = mid - left + 1, rightArrLen = right - mid;
        int[] leftArr = new int[leftArrLen];
        int[] rightArr = new int[rightArrLen];

        for (int x = 0; x < leftArrLen; x++) {
            leftArr[x] = arr[left + x];
        }

        for (int x = 0; x < rightArrLen; x++) {
            rightArr[x] = arr[mid + 1 + x];
        }

        int i = 0, j = 0, k = left;

        while (i < leftArrLen && j < rightArrLen) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            } k++;
        }

        // перенос оставшегося
        while (i < leftArrLen) {
            arr[k] = leftArr[i];
            k++; i++;
        }

        // перенос оставшегося
        while (j < rightArrLen) {
            arr[k] = rightArr[j];
            k++; j++;
        }
    }

    public static void sort(int[] arr) {
        int length = arr.length;
        getMinRun(length);
        if (length <= RUN) {
            insertionSort(arr, 0, length - 1);
            return;
        }

        for (int i = 0; i < length; i += RUN) {
            insertionSort(arr, i, Math.min((i + 31), (length - 1)));
        }

        for (int size = RUN; size < length; size = 2 * size) {
            for (int left = 0; left < length; left += 2 * size) {
                int right = Math.min((left + 2 * size - 1), (length - 1));
                int mid = (left + right) / 2;
                merge(arr, left, mid, right);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner massives_file = new Scanner(new File("massives.txt"));
        ArrayList<int[]> massives = new ArrayList<>();
        while (true) {
            try {
                int size = massives_file.nextInt();
                int[] temp_massive = new int[size];
                for (int i = 0; i < size; i++) {
                    temp_massive[i] = massives_file.nextInt();
                }
                massives.add(temp_massive);
            } catch (NoSuchElementException e) {
                break;
            }
        }
        int j = 0;
        long all_times = 0;
        int accuracy = 40;
        for (int[] massive: massives) {
            for (int i = 0; i < accuracy; i++) {
                long start = System.nanoTime();
                sort(massive);
                long end = System.nanoTime();
                all_times += end - start;
            }
            if (j == 24) {
                System.out.println(massive.length + " | " + all_times / accuracy / 25 +
                        " наносекунд | " + (massive.length * 1.0 / all_times * accuracy * 25));
                all_times = 0;
                j = 0;
            } else {j += 1;}
        }
    }
}
