/**
 As quick as a flash
 Challenge Description:
 We have just received an extremely important information that needs to be sorted. The amount of information is huge, and we need to sort it as soon as possible. That is why we decided to use a quick sort algorithm.

 Input sample:
 The first argument is a path to a file. Every row includes a test case with numbers that you need to sort using quick sort algorithm.
 5 2 6 1 3 4
 1 2 3 4
 4 3 2 1
 3 1 2 4
 1 3 2 4

 Output sample:
 You need to calculate and print number of pivots in the array during sorting. This would mean how many times the array was divided into left and right subarrays.
 Pivot is an element which divides an array. If array/subarray has only one element this element is not a pivot.
 4
 3
 3
 2
 2

 Constraints:
 1.The test case can include from 1 to 30 elements.
 2.The number of test cases is 40.
 */
package asQuickAsFlash;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by Robert on 1.9.2016.
 */
public class Main {
    /**
     * Weird implementation of QuickSort, haven't seen that. Actually, this is the only hard challenge which I do not bother to understand completely. The gif is so fast it is very frustrating to observe the changes during their version of QuickSort.
     * @param a
     * @return
     */
    public static int quickSortPivotsNum(int[] a){
        return quickSortPivotsNum(a, 0, a.length - 1);
    }

    private static int quickSortPivotsNum(int[] a, int start, int end){
        int pivot = 0, pivotPos = 0, left = 0, right = 0;
        if(start < end) {
            pivotPos = start;
            left = start + 1;
            right = end;
            pivot = a[pivotPos];

            while (left < right) {
                while (left < right && a[right] >= pivot) --right;
                if (a[right] < pivot) {
                    swap(a, pivotPos, right);
                    pivotPos = right;
                    pivot = a[pivotPos];
                }

                while (left < right && a[left] <= pivot) ++left;
                if (left < right && a[left] > pivot) {
                    swap(a, pivotPos, left);
                    pivotPos = left;
                    pivot = a[pivotPos];
                }
            }
            return 1 + quickSortPivotsNum(a, start, pivotPos - 1) + quickSortPivotsNum(a, pivotPos + 1, end);
        }
        else return 0;
    }

    private static void swap(int[] a, int id1, int id2){
        int temp = a[id1];
        a[id1] = a[id2];
        a[id2] = temp;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/asQuickAsFlash/input.txt"));

        while(textScan.hasNextLine()){
            String line = textScan.nextLine().trim();
            //System.out.println(line);
            String[] nums = line.split(" ");
            int[] a = new int[nums.length];
            for(int i = 0; i < nums.length; i++) a[i] = Integer.parseInt(nums[i]);
            System.out.println(quickSortPivotsNum(a));
        }
    }
}
